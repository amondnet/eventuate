package net.amond.eventuate.getevenstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.msemys.esjc.EventData;
import com.github.msemys.esjc.EventStore;
import com.github.msemys.esjc.ExpectedVersion;
import com.github.msemys.esjc.ResolvedEvent;
import com.github.msemys.esjc.StreamEventsSlice;
import com.github.msemys.esjc.StreamPosition;
import com.github.msemys.esjc.WriteResult;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import net.amond.eventuate.EntityNotFoundException;
import net.amond.eventuate.common.Aggregate;
import net.amond.eventuate.common.Event;
import net.amond.eventuate.eventsourcing.AggregateRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Minsu Lee
 */
public class GetEventStoreRepository<T extends Aggregate> implements AggregateRepository {

  protected static final String EventClrTypeHeader = "EventClrTypeName";
  private static final String AggregateClrTypeHeader = "AggregateClrTypeName";
  private static final String CommitIdHeader = "CommitId";

  private static Logger LOGGER = LoggerFactory.getLogger(GetEventStoreRepository.class);

  private Class<T> clasz;
  private EventStore eventStore;
  private ObjectMapper objectMapper;
  protected GetEventStoreEventMapper eventMapper;

  /**
   * Constructs a new GetEventStoreRepository for the specified aggregate class and aggregate store
   *
   * @param clasz the class of the aggregate
   * @param eventStore the aggregate store
   */
  public GetEventStoreRepository(Class<T> clasz, EventStore eventStore, ObjectMapper objectMapper) {
    this.clasz = clasz;
    this.eventStore = eventStore;
    this.objectMapper = objectMapper;
    this.eventMapper = new GetEventStoreEventMapper(objectMapper);
  }

  public CompletableFuture<T> findAsync(UUID id) {
    return CompletableFuture.supplyAsync(() -> find(id));
  }

  @Override
  public @Nullable T find(@NotNull UUID id) {
    T aggregate;
    try {
      Constructor<T> constructor = clasz.getConstructor(UUID.class);
      aggregate = constructor.newInstance(id);
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    List<Event> events = new ArrayList<>();
    StreamEventsSlice currentSlice = null;
    int nextSliceStart = StreamPosition.START;
    String streamName = getStreamName(id);

    try {
      do {
        currentSlice =
            eventStore.readStreamEventsForward(streamName, nextSliceStart, 200, false).get();
        nextSliceStart = currentSlice.nextEventNumber;

        events.addAll(currentSlice.events.stream()
            .map(event -> eventMapper.toEvent(event))
            .collect(Collectors.toList()));
        for (ResolvedEvent event : currentSlice.events) {
          aggregate.applyEvent(eventMapper.toEvent(event));
          //Aggregates.recreateAggregate(clasz, events);
        }
      } while (!currentSlice.isEndOfStream);
    } catch (InterruptedException | ExecutionException e ) {
      e.printStackTrace();
    }
    return (T) aggregate;
  }

  @Override
  public @NotNull T get(UUID id) throws EntityNotFoundException {
    T entity = this.find(id);
    if (entity == null) {
      throw new EntityNotFoundException(id, clasz.getSimpleName());
    }
    return entity;
  }

  @Override
  public void save(Aggregate aggregate, String correlationId) {

  }

  /**
   * Create a new Aggregate by processing a command and persisting the events
   *
   * @return the newly persisted aggregate
   */
  public CompletableFuture<WriteResult> save(Aggregate aggregate) {
    String commitId = UUID.randomUUID().toString();
    Collection<Event> events = aggregate.getUncommittedEvents();
    String aggregateType = aggregate.getClass().getCanonicalName();
    int originalVersion = aggregate.version() - events.size();
    ExpectedVersion expectedVersion =
        originalVersion == 0 ? ExpectedVersion.NO_STREAM : ExpectedVersion.of(originalVersion - 1);

    if (events.isEmpty()) {
      LOGGER.info("uncommitted events is empty");
      return CompletableFuture.completedFuture(null);
    }
    Map<String, Object> commitHeaders = ImmutableMap.<String, Object>builder()
        .put(CommitIdHeader, commitId)
        .put(AggregateClrTypeHeader, aggregateType).build();
    List<EventData> eventsToSave =
        events.stream()
            .map(e -> eventMapper.toEventData(e, commitHeaders))
            .collect(Collectors.toList());

    String streamName = getStreamName(aggregate.getClass(), aggregate.id());

    LOGGER.info("stream: {}", streamName);
    try {
      LOGGER.info("events to save: {}", eventsToSave.get(0).type);
      LOGGER.info("events to save: {}", objectMapper.readTree(eventsToSave.get(0).metadata));
      LOGGER.info("events to save: {}", objectMapper.readTree(eventsToSave.get(0).data));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return eventStore.appendToStream(streamName, expectedVersion, eventsToSave);
  }

  private String getStreamName(Serializable id) {
    return getStreamName(clasz, id);
  }

  private String getStreamName(Class type, Serializable id) {
    return String.format("%s-%s", type.getSimpleName(), id);
  }
}

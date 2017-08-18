package net.amond.eventuate.getevenstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.msemys.esjc.EventData;
import com.github.msemys.esjc.ResolvedEvent;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.amond.eventuate.common.Event;
import net.amond.eventuate.eventsourcing.EventMapper;
import net.amond.eventuate.eventsourcing.EventMappingException;
import net.amond.eventuate.messaging.StandardMetadataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.amond.eventuate.getevenstore.GetEventStoreRepository.EventClrTypeHeader;

/**
 * Created by amond on 17. 8. 8.
 *
 * @author amond
 */
public class GetEventStoreEventMapper implements EventMapper<EventData, ResolvedEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(GetEventStoreEventMapper.class);
  private final ObjectMapper objectMapper;

  public GetEventStoreEventMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override public EventData toEventData(Event message, Map<String, Object> headers)
      throws EventMappingException {
    StandardMetadataProvider provider = new StandardMetadataProvider();
    Map<String, String> standardMetadata = provider.getMetadata(message);

    if (headers == null) {
      headers = new HashMap<>();
    }
    Map<String, Object> eventHeaders = ImmutableMap.<String, Object>builder()
        .putAll(headers)
        .put(EventClrTypeHeader, message.getClass().getCanonicalName())
        .putAll(standardMetadata)
        .build();
    try {

      String metadata = objectMapper.writeValueAsString(eventHeaders);
      String data = objectMapper.writeValueAsString(message);
      String typeName = message.getClass().getSimpleName();
      UUID eventId = UUID.randomUUID();

      return EventData.newBuilder()
          .eventId(eventId)
          .type(typeName)
          .jsonData(data)
          .jsonMetadata(metadata)
          .build();
    } catch (JsonProcessingException e) {
      LOGGER.error("eventdata 변환 에러", e);
      throw new EventMappingException(e);
    }
  }

  @Override public Event toEvent(ResolvedEvent resolvedEvent) throws EventMappingException {
    try {
      LOGGER.info("event is resolved: {}", resolvedEvent.isResolved());
      LOGGER.info("event is {}", resolvedEvent.event.eventId);
      LOGGER.info("event stream {}", resolvedEvent.event.eventStreamId);
      LOGGER.info("event type {}", resolvedEvent.event.eventType);

      String eventClrTypeName = objectMapper.readTree(resolvedEvent.event.metadata)
          .findValue(EventClrTypeHeader)
          .textValue();
      if (Strings.isNullOrEmpty(eventClrTypeName)) {
        throw new EventMappingException("event class is null or empty");
      }
      JavaType type = objectMapper.getTypeFactory().constructFromCanonical(eventClrTypeName);
      return objectMapper.readValue(resolvedEvent.event.data, type);
    } catch (IOException e) {
      LOGGER.error("event 변환 에러", e);
      throw new EventMappingException(e);
    } catch (NullPointerException e) {
      LOGGER.error("EventClrTypeHeader is null", e);
      throw new EventMappingException("EventClrTypeHeader is null");
    }
  }
}

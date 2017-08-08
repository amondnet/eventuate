package net.amond.eventuate.getevenstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.msemys.esjc.EventData;
import com.github.msemys.esjc.ResolvedEvent;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import net.amond.eventuate.common.Event;
import net.amond.eventuate.eventsourcing.EventMapper;
import net.amond.eventuate.messaging.StandardMetadataProvider;

import static net.amond.eventuate.getevenstore.GetEventStoreRepository.EventClrTypeHeader;

/**
 * Created by amond on 17. 8. 8.
 *
 * @author amond
 */
public class GetEventStoreEventMapper implements EventMapper<EventData, ResolvedEvent> {

  private final ObjectMapper objectMapper;

  public GetEventStoreEventMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override public EventData toEventData(Event message, Map<String, Object> headers) {
    StandardMetadataProvider provider = new StandardMetadataProvider();
    Map<String, String> standardMetadata = provider.getMetadata(message);

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
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override public Event toEvent(ResolvedEvent resolvedEvent) {
    try {
      String eventClrTypeName = objectMapper.readTree(resolvedEvent.originalEvent().metadata)
          .findValue(EventClrTypeHeader)
          .textValue();
      JavaType type = objectMapper.getTypeFactory().constructFromCanonical(eventClrTypeName);
      return objectMapper.readValue(resolvedEvent.originalEvent().data, type);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}

package net.amond.eventuate.azure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.util.Map;
import net.amond.eventuate.MetadataProvider;
import net.amond.eventuate.common.Aggregate;
import net.amond.eventuate.getevenstore.GetEventStoreRepository;
import net.amond.eventuate.common.Event;
import net.amond.eventuate.messaging.Envelope;
import net.amond.eventuate.messaging.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by amond on 17. 3. 16.
 *
 * An event bus that sends serialized object payloads through a {@link MessageSender}.
 *
 * Note that {@link Aggregate} entities persisted through the {@link
 * GetEventStoreRepository} do not use the {@link EventBus}, but has its own event publishing
 * mechanism.
 *
 * @author amond
 */
public class AzureEventBus implements EventBus {

  private static final Logger LOGGER = LoggerFactory.getLogger(AzureEventBus.class);

  private final MessageSender sender;
  private final MetadataProvider metadataProvider;
  private final ObjectMapper objectMapper;

  /**
   * Initializes a new instance of the <see cref="EventBus"/> class.
   *
   * @param objectMapper The serializer to sue for the message body.
   */
  @Autowired
  public AzureEventBus(MessageSender sender, MetadataProvider metadataProvider,
      ObjectMapper objectMapper) {
    this.sender = sender;
    this.metadataProvider = metadataProvider;
    this.objectMapper = objectMapper;
  }

  /**
   * Publishes the specified event.
   */
  @Override public void publish(Envelope<Event> event) {
    this.sender.send(() -> buildMessage(event));
  }

  /**
   * Publishes the specified events;
   */
  @Override public void publish(Iterable<Envelope<Event>> events) {
    events.forEach(this::publish);
  }

  private BrokeredMessage buildMessage(Envelope<Event> envelope) {
    Event event = envelope.body();

    try {
      BrokeredMessage message = new BrokeredMessage(objectMapper.writeValueAsBytes(event));
      // message.setSessionId(event.sourceId().toString());

      if (!Strings.isNullOrEmpty(envelope.getMessageId())) {
        message.setMessageId(envelope.getMessageId());
      }

      if (!Strings.isNullOrEmpty(envelope.getCorrelationId())) {
        message.setCorrelationId(envelope.getCorrelationId());
      }

      Map<String, String> metadata = this.metadataProvider.getMetadata(event);
      if (metadata != null) {
        metadata.forEach(message::setProperty);
      }

      return message;
    } catch (JsonProcessingException e) {
      LOGGER.error("build message failed", e);
      throw new RuntimeException(e);
    }
  }
}

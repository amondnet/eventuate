package net.amond.eventuate.azure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import net.amond.eventuate.MetadataProvider;
import net.amond.eventuate.common.Command;
import net.amond.eventuate.messaging.CommandBus;
import net.amond.eventuate.messaging.Envelope;
import net.amond.eventuate.messaging.MessageSessionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public class AzureCommandBus implements CommandBus {

  private static final Logger LOGGER = LoggerFactory.getLogger(AzureCommandBus.class);

  private final ObjectMapper objectMapper;
  private MetadataProvider metadataProvider;
  private MessageSender messageSender;

  @Autowired
  public AzureCommandBus(ObjectMapper objectMapper, MetadataProvider metadataProvider,
      MessageSender messageSender) {
    this.objectMapper = objectMapper;
    this.messageSender = messageSender;
    this.metadataProvider = metadataProvider;
  }

  @Override public void send(Envelope<Command> command) {
    this.messageSender.send(() -> buildMessage(command));
  }

  @Override public void send(Iterable<Envelope<Command>> commands) {
    for (Envelope<Command> command : commands) {
      this.send(command);
    }
  }

  private BrokeredMessage buildMessage(Envelope<Command> command) {
    try {

      BrokeredMessage message = new BrokeredMessage(objectMapper.writeValueAsBytes(command));

      if (!Strings.isNullOrEmpty(command.getMessageId())) {
        message.setMessageId(message.getMessageId());
      } else if (command.body().getId() != null) {
        message.setMessageId(command.body().getId().toString());
      }

      if (!Strings.isNullOrEmpty(command.getCorrelationId())) {
        message.setCorrelationId(command.getCorrelationId());
      }

      MessageSessionProvider sessionProvider = (MessageSessionProvider) command.body();
      if (sessionProvider != null) {
        message.setSessionId(sessionProvider.sessionId());
      }

      Map<String, String> metadata = this.metadataProvider.getMetadata(command.body());
      if (metadata != null) {
        metadata.forEach(message::setProperty);
      }

      if (!command.getDelay().isZero() && !command.getDelay().isNegative()) {
        Instant.now().plus(command.getDelay());
        message.setScheduledEnqueueTimeUtc(Date.from(Instant.now().plus(command.getDelay())));
      }

      if (command.getTimeToLive() > 0) {
        message.setTimeToLive(command.getTimeToLive());
      }
      return message;
    } catch (IOException e) {
      LOGGER.error("build message failed", e);
      throw new RuntimeException(e);
    }
  }
}

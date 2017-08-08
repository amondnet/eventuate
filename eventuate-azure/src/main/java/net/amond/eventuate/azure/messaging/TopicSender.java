package net.amond.eventuate.azure.messaging;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import net.amond.eventuate.messaging.Envelope;
import net.amond.eventuate.messaging.handling.EventHandler;

/**
 * Created by amond on 17. 3. 21.
 *
 * @author amond
 */
public class TopicSender implements MessageSender {
  ServiceBusSettings settings;
  ServiceBusContract service;
  private String topic;

  public TopicSender(ServiceBusSettings settings, String topic) {
    Configuration config =
        ServiceBusConfiguration.configureWithSASAuthentication(
            "livepass-message",
            "RootManageSharedAccessKey",
            "2VhfJC74tuMtCAfcsL6MmV9cB4OVPSLCT8jBhQqYyF0=",
            ".servicebus.windows.net"
        );
    service = ServiceBusService.create(config);
    this.topic = topic;
    this.settings = settings;
  }

  @Override public void send(Supplier<BrokeredMessage> messageSupplier) {
    try {
      service.sendTopicMessage(topic, messageSupplier.get());
    } catch (ServiceException e) {
      e.printStackTrace();
      throw new MessageSendException();
    }
  }

  @Override public CompletableFuture sendAsync(Supplier<BrokeredMessage> messageFactory) {
    return CompletableFuture.runAsync(new Runnable() {
      @Override public void run() {
        //send(messageFactory);
      }
    });
  }

  public void SendAsync(Function<Envelope, BrokeredMessage> messageFactory,
      SuccessCallback successCallback, ExceptionCallback exceptionCallback) {
  }

  @Override public EventHandler retrying() {
    return null;
  }
}

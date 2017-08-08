package net.amond.eventuate.azure.messaging;

import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.amond.eventuate.messaging.handling.EventHandler;

/**
 * Created by amond on 17. 3. 16.
 *
 * Abstracts the behavior of sending a message.
 *
 * @author amond
 */
public interface MessageSender {

  /**
   * Sends the specified message synchronously.
   */
  void send(Supplier<BrokeredMessage> message);

  /**
   * Sends the specified message asynchronously.
   */
  CompletableFuture sendAsync(Supplier<BrokeredMessage> messageFactory);

  /**
   * Notifies that the sender is retrying due to a transient fault.
   */
  EventHandler retrying();
}

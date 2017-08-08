package net.amond.eventuate.azure.messaging;

import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.util.function.Function;

/**
 * Created by amond on 17. 3. 16.
 *
 * Abstracts the behavior of a receiving component that raises an event for every received event.
 *
 * @author amond
 */
public interface MessageReceiver {

  /**
   * Start the listener
   */
  void start(Function<BrokeredMessage, MessageReleaseAction> messageHandler);

  /**
   * Stop the listener.
   */
  void stop();
}

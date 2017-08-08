package net.amond.eventuate.azure.messaging;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.CreateSubscriptionResult;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveSubscriptionMessageResult;
import com.microsoft.windowsazure.services.servicebus.models.SubscriptionInfo;
import org.junit.Test;

/**
 * Created by amond on 17. 3. 21.
 *
 * @author amond
 */
public class TopicSenderTest {

  @Test
  public void sendTest() throws ServiceException {

    TopicSender topicSender = new TopicSender(null, "commands-dev");

    topicSender.send(() -> new BrokeredMessage("{"
        + "\"name\":\"test\"}"));

    Configuration config =
        ServiceBusConfiguration.configureWithSASAuthentication(
            "livepass-message",
            "RootManageSharedAccessKey",
            "2VhfJC74tuMtCAfcsL6MmV9cB4OVPSLCT8jBhQqYyF0=",
            ".servicebus.windows.net"
        );
    ServiceBusContract service = ServiceBusService.create(config);

    SubscriptionInfo subInfo = new SubscriptionInfo("commands-dev-sub");
    CreateSubscriptionResult result = service.createSubscription("commands-dev", subInfo);

    try {
      ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
      opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

      while (true) {
        ReceiveSubscriptionMessageResult resultSubMsg =
            service.receiveSubscriptionMessage("commands-dev", "commands-dev-sub", opts);
        BrokeredMessage message = resultSubMsg.getValue();
        if (message != null && message.getMessageId() != null) {
          System.out.println("MessageID: " + message.getMessageId());
          // Display the topic message.
          System.out.print("From topic: ");
          byte[] b = new byte[200];
          String s = null;
          int numRead = message.getBody().read(b);
          while (-1 != numRead) {
            s = new String(b);
            s = s.trim();
            System.out.print(s);
            numRead = message.getBody().read(b);
          }
          System.out.println();
          System.out.println("Custom Property: " +
              message.getProperty("MessageNumber"));
          // Delete message.
          System.out.println("Deleting this message.");
          service.deleteMessage(message);
        } else {
          System.out.println("Finishing up - no more messages.");
          break;
          // Added to handle no more messages.
          // Could instead wait for more messages to be added.
        }
      }
    } catch (ServiceException e) {
      System.out.print("ServiceException encountered: ");
      System.out.println(e.getMessage());
      System.exit(-1);
    } catch (Exception e) {
      System.out.print("Generic exception encountered: ");
      System.out.println(e.getMessage());
      System.exit(-1);
    }
  }
}

package net.amond.eventuate.azure.messaging;

/**
 * Created by amond on 17. 3. 16.
 *
 * Specifies how the {@link com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage}
 * should be released.
 *
 * @author amond
 */
public class MessageReleaseAction {

  public static final MessageReleaseAction CompleteMessage =
      new MessageReleaseAction(Kind.Complete);
  public static final MessageReleaseAction AbandonMessage = new MessageReleaseAction(Kind.Abandon);

  private final Kind kind;
  protected String deadLetterReason = "";
  protected String deadLetterDescription = "";

  protected MessageReleaseAction(Kind kind) {
    this.kind = kind;
  }

  public Kind kind() {
    return this.kind;
  }

  public String deadLetterReason() {
    return this.deadLetterReason;
  }

  private void deadLetterReason(String reason) {
    this.deadLetterReason = reason;
  }

  public String deadLetterDescription() {
    return this.deadLetterDescription;
  }

  private void deadLetterDescription(String description) {
    this.deadLetterDescription = description;
  }

  public static MessageReleaseAction DeadLetterMessage(String reason, String description) {
    MessageReleaseAction action = new MessageReleaseAction(Kind.DeadLetter);
    action.deadLetterDescription(description);
    action.deadLetterReason(reason);
    return action;
  }

  public enum Kind {
    Complete, Abandon, DeadLetter
  }
}

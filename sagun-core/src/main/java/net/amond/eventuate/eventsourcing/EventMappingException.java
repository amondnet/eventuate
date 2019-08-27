package net.amond.eventuate.eventsourcing;

/**
 * Created by amond on 17. 8. 14.
 *
 * @author amond
 */
public class EventMappingException extends RuntimeException {
  public EventMappingException() {
  }

  public EventMappingException(String message) {
    super(message);
  }

  public EventMappingException(String message, Throwable cause) {
    super(message, cause);
  }

  public EventMappingException(Throwable cause) {
    super(cause);
  }

  public EventMappingException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

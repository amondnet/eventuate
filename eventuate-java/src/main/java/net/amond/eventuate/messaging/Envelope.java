package net.amond.eventuate.messaging;

import java.time.Period;

/**
 * Created by amond on 17. 3. 15.
 */
public class Envelope<T> {

  private final T body;

  private Period delay = Period.ZERO;
  private double timeToLive = 0;

  private String correlationId;
  private String messageId;

  public Envelope(T body) {
    this.body = body;
  }

  public T body() {
    return body;
  }

  public Period getDelay() {
    return delay;
  }

  public void setDelay(Period delay) {
    this.delay = delay;
  }

  public double getTimeToLive() {
    return timeToLive;
  }

  public void setTimeToLive(double timeToLive) {
    this.timeToLive = timeToLive;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }
}



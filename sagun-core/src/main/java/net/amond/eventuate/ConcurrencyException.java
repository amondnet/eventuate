package net.amond.eventuate;

import java.io.Serializable;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public class ConcurrencyException extends Exception implements Serializable {
  public ConcurrencyException() {
  }

  public ConcurrencyException(String message) {
    super(message);
  }

  public ConcurrencyException(String message, Exception inner) {
    super(message, inner);
  }
}

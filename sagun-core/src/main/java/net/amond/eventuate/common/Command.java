package net.amond.eventuate.common;

import java.util.UUID;

/**
 * @author Minsu Lee
 */
public interface Command extends Message {

  UUID getId();
}

package net.amond.eventuate.messaging;

/**
 * If a command message implements {@link MessageSessionProvider}, it hints implementatons
 * of {@link CommandBus} to assign the specified sessionId to the outgoing message if supported.
 *
 * @author amond
 */
public interface MessageSessionProvider {

  String sessionId();
}

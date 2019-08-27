package net.amond.eventuate.messaging;

import net.amond.eventuate.common.Command;

/**
 * Created by amond on 17. 3. 16.
 */
public interface CommandBus {

  void send(Envelope<Command> command);

  void send(Iterable<Envelope<Command>> commands);
}

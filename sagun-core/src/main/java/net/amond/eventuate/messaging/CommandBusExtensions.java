package net.amond.eventuate.messaging;

import java.util.Collection;
import java.util.stream.Collectors;
import net.amond.eventuate.common.Command;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public class CommandBusExtensions {

  public static void send(CommandBus commandBus, Command command) {
    commandBus.send(new Envelope<Command>(command));
  }

  public static void send(CommandBus bus, Collection<Command> commands) {
    bus.send(commands.stream().map(Envelope::new).collect(Collectors.toList()));
  }
}

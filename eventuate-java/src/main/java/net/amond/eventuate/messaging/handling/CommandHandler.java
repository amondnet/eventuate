package net.amond.eventuate.messaging.handling;

import net.amond.eventuate.common.Command;

/**
 * Created by amond on 17. 3. 16.
 *
 * Marker interface that makes it easier to discover handlers via reflection.
 *
 * @author amond
 */
public interface CommandHandler<T extends Command> {
  void handle(T command);
}

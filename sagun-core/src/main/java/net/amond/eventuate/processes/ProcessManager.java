package net.amond.eventuate.processes;

import java.util.UUID;
import net.amond.eventuate.common.Command;
import net.amond.eventuate.messaging.Envelope;

/**
 * Interface implemented by process managers (also known as Sagas in the CQRS community) thatpublish
 * commands to the command bus.
 *
 * @author amond
 * @see <a href="http://go.microsoft.com/fwlink/p/?LinkID=258564">Reference 6</a>
 * @see <a href="http://go.microsoft.com/fwlink/p/?LinkID=258558">Journey lessons learnt</a>
 */
public interface ProcessManager {

  /**
   * Gets the process manager identifier.
   */
  UUID id();

  /**
   * Gets a value indicating whether the process manager workflow is completed and the state can
   * be archived.
   */
  boolean completed();

  /**
   * Gets a collection of commands that need to be sent when the state of the process manager is
   * persisted.
   */
  Iterable<Envelope<Command>> commands();
}

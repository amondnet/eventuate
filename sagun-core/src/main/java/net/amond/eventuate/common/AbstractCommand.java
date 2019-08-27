package net.amond.eventuate.common;

import java.util.UUID;

/**
 * Created by amond on 17. 3. 24.
 *
 * @author amond
 */
public class AbstractCommand implements Command {

  private final UUID id;

  public AbstractCommand() {
    this.id = UUID.randomUUID();
  }

  public AbstractCommand(UUID id) {
    this.id = id;
  }

  @Override public UUID getId() {
    return this.id;
  }
}

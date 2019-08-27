package net.amond.eventuate.common;

import java.util.UUID;

/**
 * Created by amond on 17. 3. 15.
 */
public abstract class AbstractVersionedEvent implements VersionedEvent {
  private int version;

  private UUID sourceId;

  @Override
  public int version() {
    return version;
  }

  public void version(int version) {
    this.version = version;
  }

  public UUID sourceId() {
    return sourceId;
  }

  public void sourceId(UUID sourceId) {
    this.sourceId = sourceId;
  }
}

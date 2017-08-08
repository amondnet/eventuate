package net.amond.eventuate;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public class EntityNotFoundException extends Exception implements Serializable {

  private UUID entityId;
  private String entityType;

  public EntityNotFoundException() {

  }

  public EntityNotFoundException(UUID entityId) {
    super(entityId.toString());
    this.entityId = entityId;
  }

  public EntityNotFoundException(UUID entityId, String entityType) {
    super(entityType + ": " + entityId.toString());
    this.entityId = entityId;
    this.entityType = entityType;
  }

  public EntityNotFoundException(UUID entityId, String entityType, String message
      , Exception e) {
    super(message, e);
  }

  public UUID getEntityId() {
    return entityId;
  }

  public String getEntityType() {
    return entityType;
  }
}

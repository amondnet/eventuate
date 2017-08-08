package net.amond.eventuate.eventsourcing;

import java.util.UUID;
import net.amond.eventuate.EntityNotFoundException;
import net.amond.eventuate.common.Aggregate;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public interface AggregateRepository<T extends Aggregate> {

  /**
   * @param id The id of the entity
   * @return The hydrated entity, or null if it does not exist.
   */
  T find(UUID id);

  /**
   * @param id The id of the entity
   * @return The hydrated entity
   * @throws EntityNotFoundException If the entity is not found.
   */
  T get(UUID id) throws EntityNotFoundException;

  /**
   * Saves the event sourced entity.
   *
   * @param aggregate The entity.
   * @param correlationId A Correlation id to use publishing events.
   */
  void save(T aggregate, String correlationId);
}

package net.amond.eventuate.messaging;

import java.util.HashMap;
import java.util.Map;
import net.amond.eventuate.MetadataProvider;
import net.amond.eventuate.common.Command;
import net.amond.eventuate.common.Event;

/**
 * Extracts metadata about a payload so that it's placed in the
 * message envelope.
 *
 * @author amond
 */
public class StandardMetadataProvider implements MetadataProvider {

  /**
   * Gets metadata associated with the payload, which can be
   * used by processors to filter and selectively subscribe to
   * messages.
   */
  @Override public Map<String, String> getMetadata(Object payload) {
    Map<String, String> metadata = new HashMap<>();
    Class type = payload.getClass();

    // The standard metadata could be used as a sort of partitioning already,
    // maybe considering different assembly names as being the area/subsystem/bc

    metadata.put(StandardMetadata.NAME, type.getName());
    metadata.put(StandardMetadata.CANONICAL_NAME, type.getCanonicalName());
    metadata.put(StandardMetadata.NAMESPACE, type.getPackage().getName());
    metadata.put(StandardMetadata.TYPE_NAME, type.getSimpleName());

    if (payload instanceof Event) {
      Event e = (Event) payload;
      // metadata.put(StandardMetadata.SOURCE_ID, e.sourceId().toString());
      metadata.put(StandardMetadata.KIND, StandardMetadata.EVENT_KIND);
    }

    if (payload instanceof Command) {
      Command c = (Command) payload;
      metadata.put(StandardMetadata.KIND, StandardMetadata.COMMAND_KIND);
    }

    return metadata;
  }
}

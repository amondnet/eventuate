package net.amond.eventuate;

import java.util.Map;

/**
 * Created by amond on 17. 3. 15.
 *
 * Extracts metadata about a payload so that it's placed in the message envelope;
 *
 * @author amond@amond.net
 * @since 1.0.1
 */
public interface MetadataProvider {

  /**
   * Gets metadata associated with the payload, which can be used by processors to filter and
   * selectively subscribe to messages.
   */

  Map<String, String> getMetadata(Object payload);
}

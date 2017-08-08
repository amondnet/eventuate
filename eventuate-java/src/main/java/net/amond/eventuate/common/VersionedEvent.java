package net.amond.eventuate.common;

/**
 * Created by amond on 17. 3. 15.
 */
public interface VersionedEvent extends Event {

  int version();
}

package net.amond.eventuate.messaging.handling;

import net.amond.eventuate.common.Event;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public interface EnvelopedEventHandler<T extends Event> extends EventHandler<T> {
}

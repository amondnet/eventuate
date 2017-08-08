package net.amond.eventuate.eventsourcing;

import java.util.Map;
import net.amond.eventuate.common.Event;

/**
 * Created by amond on 17. 8. 8.
 *
 * @author amond
 */
public interface EventMapper<T, U> {

  T toEventData(Event event, Map<String, Object> headers);

  Event toEvent(U data);
}

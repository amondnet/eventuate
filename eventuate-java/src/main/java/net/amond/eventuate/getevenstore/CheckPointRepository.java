package net.amond.eventuate.getevenstore;

import com.github.msemys.esjc.EventReadResult;
import com.github.msemys.esjc.EventStore;
import com.github.msemys.esjc.ExpectedVersion;
import com.github.msemys.esjc.StreamPosition;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import net.amond.eventuate.common.Checkpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by amond on 17. 8. 14.
 *
 * @author amond
 */
public class CheckPointRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(CheckPointRepository.class);
  private final String stream;
  private final EventStore eventStore;
  private final GetEventStoreEventMapper eventMapper;

  public CheckPointRepository(String stream, EventStore eventStore,
      GetEventStoreEventMapper eventMapper) {
    this.stream = stream;
    this.eventStore = eventStore;
    this.eventMapper = eventMapper;
  }

  public Checkpoint getLastCheckpoint() {
    LOGGER.debug("getLastCheckpoint");
    try {
      EventReadResult eventReadResult =
          eventStore.readEvent(stream, StreamPosition.END, true).get();

      LOGGER.info("read checkpoint");
      switch (eventReadResult.status) {
        case StreamDeleted:
        case NotFound:
        case NoStream:
        default:
          return new Checkpoint(StreamPosition.START);
        case Success:
          return (Checkpoint) eventMapper.toEvent(eventReadResult.event);
      }
    } catch (InterruptedException | ExecutionException e) {
      LOGGER.error("read checkpoint error", e);
      throw new CheckPointException();
    }
  }

  public void save(Checkpoint checkpoint) {
    this.save(checkpoint, new HashMap<>());
  }

  public void save(Checkpoint checkPoint, Map<String, Object> headers) {
    try {
      eventStore.appendToStream(stream, ExpectedVersion.ANY,
          eventMapper.toEventData(checkPoint, headers)).get();
    } catch (InterruptedException | ExecutionException e) {
      LOGGER.error("save checkpoint error", e);
      throw new CheckPointException();
    }
  }
}

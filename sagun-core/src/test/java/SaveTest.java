import co.test.Asdf;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.msemys.esjc.CatchUpSubscription;
import com.github.msemys.esjc.CatchUpSubscriptionListener;
import com.github.msemys.esjc.CatchUpSubscriptionSettings;
import com.github.msemys.esjc.EventData;
import com.github.msemys.esjc.EventStore;
import com.github.msemys.esjc.EventStoreBuilder;
import com.github.msemys.esjc.EventStoreException;
import com.github.msemys.esjc.ExpectedVersion;
import com.github.msemys.esjc.ResolvedEvent;
import com.github.msemys.esjc.StreamMetadata;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import net.amond.eventuate.getevenstore.GetEventStoreEventMapper;
import net.amond.eventuate.getevenstore.GetEventStoreRepository;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Minsu Lee
 */
public class SaveTest {

  private ObjectMapper objectMapper;

  @BeforeClass
  public void setUp() {
    this.objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
  }

  @Test
  public void saveTest() throws ExecutionException, InterruptedException, JsonProcessingException {

    EventStore eventStore = EventStoreBuilder.newBuilder()
        .singleNodeAddress("127.0.0.1", 1113)
        .userCredentials("admin", "changeit")
        .build();

    GetEventStoreRepository<TestAggregate> getEventStoreRepository =
        new GetEventStoreRepository(TestAggregate.class, eventStore, new ObjectMapper());

    UUID aggregateId = UUID.randomUUID();
    TestAggregate testAggregate = new TestAggregate(aggregateId);
    testAggregate.ProduceEvents(20);

    getEventStoreRepository.save(testAggregate).get();

    System.out.println(
        new ObjectMapper().writeValueAsString(getEventStoreRepository.find(aggregateId)));
  }

  public void prjectionTest()
      throws ExecutionException, InterruptedException, JsonProcessingException {
    EventStore eventStore = EventStoreBuilder.newBuilder()
        .singleNodeAddress("127.0.0.1", 1113)
        .userCredentials("admin", "changeit")
        .build();
  }
}

import java.util.UUID;
import net.amond.eventuate.common.AbstractAggregate;

/**
 * @author Minsu Lee
 */
public class TestAggregate extends AbstractAggregate {

  public TestAggregate(UUID id) {
    super(id);
  }

  public void ProduceEvents(int count) {
    for (int i = 0; i < count; i++)
      raiseEvent(new WoftamEvent("Woftam1-" + i, "Woftam2-" + i));
  }
}

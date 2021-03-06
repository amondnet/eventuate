import java.util.UUID;
import net.amond.eventuate.common.Event;

public class WoftamEvent implements Event {
  private UUID id;

  public WoftamEvent() {
    this.id = UUID.randomUUID();
  }

  public WoftamEvent(String property1, String property2) {
    this.property1 = property1;
    this.property2 = property2;
  }

  String property1;
  String property2;

  public String getProperty1() {
    return property1;
  }

  public void setProperty1(String property1) {
    this.property1 = property1;
  }

  public String getProperty2() {
    return property2;
  }

  public void setProperty2(String property2) {
    this.property2 = property2;
  }

}
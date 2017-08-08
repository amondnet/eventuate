package net.amond.eventuate.azure.messaging;

/**
 * Created by amond on 17. 3. 16.
 *
 * @author amond
 */
public class ServiceBusConfig {
  private final String RuleName = "Custom";
  private boolean initialized;
  private ServiceBusSettings settings;

  public ServiceBusConfig(ServiceBusSettings settings) {
    this.settings = settings;
  }

  public void initialize() {

  }
}

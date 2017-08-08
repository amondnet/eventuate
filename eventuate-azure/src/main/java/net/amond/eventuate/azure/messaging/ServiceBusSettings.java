package net.amond.eventuate.azure.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by amond on 17. 3. 16.
 * Simple settings class to configure the connection to the Windows Azure Service Bus.
 *
 * @author amond
 */
public class ServiceBusSettings {
  @Value("azure.servicebus.serviceUriScheme")
  private String serviceUriScheme = "";
  @Value("azure.servicebus.serviceNamespace")
  private String serviceNamespace = "";
  @Value("azure.servicebus.servicePath")
  private String servicePath = "";
  @Value("azure.servicebus.tokenIssuer")
  private String tokenIssuer = "";
  @Value("azure.servicebus.tokenAccessKey")
  private String tokenAccessKey = "";

  public ServiceBusSettings() {

  }

  public String getServiceUriScheme() {
    return serviceUriScheme;
  }

  public void setServiceUriScheme(String serviceUriScheme) {
    this.serviceUriScheme = serviceUriScheme;
  }

  public String getServiceNamespace() {
    return serviceNamespace;
  }

  public void setServiceNamespace(String serviceNamespace) {
    this.serviceNamespace = serviceNamespace;
  }

  public String getServicePath() {
    return servicePath;
  }

  public void setServicePath(String servicePath) {
    this.servicePath = servicePath;
  }

  public String getTokenIssuer() {
    return tokenIssuer;
  }

  public void setTokenIssuer(String tokenIssuer) {
    this.tokenIssuer = tokenIssuer;
  }

  public String getTokenAccessKey() {
    return tokenAccessKey;
  }

  public void setTokenAccessKey(String tokenAccessKey) {
    this.tokenAccessKey = tokenAccessKey;
  }
}

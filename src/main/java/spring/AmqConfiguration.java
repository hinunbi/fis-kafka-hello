package spring;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

@Configuration
@ConditionalOnProperty(value = "camel.amq.enabled", matchIfMissing = true)
public class AmqConfiguration {

  final
  ConnectionFactory connectionFactory;

  @Autowired
  public AmqConfiguration(ConnectionFactory connectionFactory) {
    this.connectionFactory = connectionFactory;
  }

  @Bean
  ActiveMQComponent amq() {
    ActiveMQComponent activemq = new ActiveMQComponent();
    activemq.setConnectionFactory(connectionFactory);

    return activemq;
  }
}

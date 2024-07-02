package org.demo.batch.master.config;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import com.ibm.msg.client.jms.JmsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@EnableJms
@Configuration
@Slf4j
public class JMSConfig {

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerFactory(@Qualifier("mqConnectionFactory")
                                                                 ConnectionFactory connection,
                                                                 DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connection);
        factory.setConcurrency("3-10");
        factory.setReceiveTimeout(10000L);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier("mqConnectionFactory") ConnectionFactory connection) {
        return new JmsTemplate(connection);
    }

    @Bean
    @Primary
    public MQConnectionFactory mqConnectionFactory(@Qualifier("propertiesMQ") MQConfigurationProperties properties) throws
                                                                                                                    JMSException {
        MQConnectionFactory connectionFactory = new MQConnectionFactory();
        connectionFactory.setHostName(properties.getConnName().split("\\(")[0]);
        connectionFactory.setPort(Integer.parseInt(properties.getConnName().split("\\(")[1].replace(")", "")));
        connectionFactory.setQueueManager(properties.getQueueManager());
        connectionFactory.setChannel(properties.getChannel());
        connectionFactory.setTransportType(1); // Usar MQ_CLIENT
        connectionFactory.setStringProperty(JmsConstants.USERID, properties.getUser());
        connectionFactory.setStringProperty(JmsConstants.PASSWORD, properties.getPassword());

        log.info("MQConnectionFactory configurado com host: {}", properties.getConnName());

        return connectionFactory;
    }

    @Bean
    public MQConfigurationProperties propertiesMQ() {
        return new MQConfigurationProperties();
    }
}

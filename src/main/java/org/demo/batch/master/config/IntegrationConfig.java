package org.demo.batch.master.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

import javax.jms.ConnectionFactory;

@Configuration
@EnableIntegration
@EnableBatchProcessing
@EnableBatchIntegration
public class IntegrationConfig {

    @Bean("directChannel")
    public DirectChannel directChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundJmsRequests(@Qualifier("mqConnectionFactory")
                                               ConnectionFactory connectionFactory,
                                               @Qualifier("directChannel")
                                               DirectChannel directChannel) {
        return IntegrationFlows.from(directChannel)
                               .handle(Jms.outboundAdapter(connectionFactory)
                                          .destination("DEMO_BATCH")
                                      )
                               .get();
    }

    @Bean
    public MessagingTemplate messagingTemplate(@Qualifier("directChannel") DirectChannel channel) {
        MessagingTemplate template = new MessagingTemplate();
        template.setDefaultChannel(channel);
        return template;
    }
}

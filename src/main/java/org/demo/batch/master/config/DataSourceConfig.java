package org.demo.batch.master.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = "org.demo.batch.master.repository",
                       transactionManagerRef = "transactionDemo")
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.demo")
    public DataSourceProperties dataSourceDemo(){
        return new DataSourceProperties();
    }

    @Bean("dbDemo")
    public DataSource dataSource() {
        return dataSourceDemo().initializeDataSourceBuilder().build();
    }

    @Bean("transactionDemo")
    public PlatformTransactionManager transactionManager(@Qualifier("dbDemo") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


}

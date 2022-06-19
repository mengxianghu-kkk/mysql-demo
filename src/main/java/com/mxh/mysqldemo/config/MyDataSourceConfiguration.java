package com.mxh.mysqldemo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyDataSourceConfiguration {

    /**
     * 使用Primary来表示主数据有
     *
     * @return
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.first")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource routingDataSource() {
        AbstractRoutingDataSource dataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return "second";
            }
        };
        Map<Object, Object> targetDataSources = new HashMap<>(5);
        targetDataSources.put("first", primaryDataSource());
        targetDataSources.put("second", secondDataSource());
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }
}
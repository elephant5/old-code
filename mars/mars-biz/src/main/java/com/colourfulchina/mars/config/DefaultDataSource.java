package com.colourfulchina.mars.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

//@Getter
//@Setter
//@ConfigurationProperties(prefix = "spring.shardingsphere.datasource.mars")
@Slf4j
@Component
@Configuration
@Data
//@ConfigurationProperties(prefix = "spring.shardingsphere.datasource.mars")
public class DefaultDataSource {

//    @Value("${spring.shardingsphere.datasource.mars.jdbc-url}")
//    private String url;
//    @Value("${spring.shardingsphere.datasource.mars.username}")
//    private String username;
//    @Value("${spring.shardingsphere.datasource.mars.password}")
//    private String password;
//    @Value("${spring.shardingsphere.datasource.mars.driver-class-name}")
//    private String driverClassName;
//    @Value("${spring.shardingsphere.sharding.default-data-source-name}")
//    private String databaseName;
//    @Value("${spring.shardingsphere.datasource.mars.initialSize}")
//    private Integer initialSize;
//    @Value("${spring.shardingsphere.datasource.mars.minIdle}")
//    private Integer minIdle;
//    @Value("${spring.shardingsphere.datasource.mars.maxActive}")
//    private Integer maxActive;
//    @Value("${spring.shardingsphere.datasource.mars.maxWait}")
//    private Integer maxWait;
//    @Value("${spring.shardingsphere.datasource.mars.timeBetweenEvictionRunsMillis}")
//    private Long timeBetweenEvictionRunsMillis;
//    @Value("${spring.shardingsphere.datasource.mars.minEvictableIdleTimeMillis}")
//    private Long minEvictableIdleTimeMillis;
//    @Value("${spring.shardingsphere.datasource.mars.validationQuery}")
//    private String validationQuery;
//    @Value("${spring.shardingsphere.datasource.mars.testWhileIdle}")
//    private Boolean testWhileIdle;
//    @Value("${spring.shardingsphere.datasource.mars.testOnBorrow}")
//    private Boolean testOnBorrow;
//    @Value("${spring.shardingsphere.datasource.mars.testOnReturn}")
//    private Boolean testOnReturn;
//    @Value("${spring.shardingsphere.datasource.mars.poolPreparedStatements}")
//    private Boolean poolPreparedStatements;
//    @Value("${spring.shardingsphere.datasource.mars.maxPoolPreparedStatementPerConnectionSize}")
//    private Integer maxPoolPreparedStatementPerConnectionSize;


//    @Bean(name = "dataSource"  , initMethod = "getConnection")
//    public DataSource createDataSource() {
//        HikariDataSource result = new HikariDataSource();
//        result.setDriverClassName(getDriverClassName());
//        result.setJdbcUrl(getUrl());
//        result.setUsername(getUsername());
//        result.setPassword(getPassword());
//
//        result.setMinimumIdle(initialSize);
//        result.setMaximumPoolSize(maxActive);
//        result.setConnectionTimeout(maxWait);
//        result.setMaxLifetime(1800000);
//        result.setMaximumPoolSize(50);
//        return result;
//    }


}

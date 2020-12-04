package com.colourfulchina.mars.config;

import com.colourfulchina.mars.sharding.DatePreciseShardingAlgorithm;
import com.colourfulchina.mars.sharding.DateRangeShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
//import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
//import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
//import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
//import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
//@MapperScan(value = "com.colourfulchina.pangu.taishang.mapper",
//        sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfiguration  {

//
//	@Autowired
//    private DefaultDataSource defaultDataSource;
//
//    @Bean(name = "shardingDatasource")
//	@Primary
//	public DataSource dataSource() throws SQLException {
//		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
//        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
//        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("create_time", new DatePreciseShardingAlgorithm(),  new DateRangeShardingAlgorithm()));
//        Properties props = new Properties();
//        props.put("sql.show", false);
//        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, props);
//	}
//
//	private KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
//		KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "id");
//		return result;
//	}
//
//	TableRuleConfiguration getOrderTableRuleConfiguration() {
//		TableRuleConfiguration result = new TableRuleConfiguration("bosc_bank_txt");
//		result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
//		return result;
//	}
//
//	private Map<String, DataSource> createDataSourceMap() {
//		Map<String, DataSource> result = new HashMap<>();
//		result.put("dataSource", defaultDataSource.createDataSource());
//		return result;
//	}
//	/**
//	 * 创建 SqlSessionFactory
//	 */
//	@Bean(name = "sqlSessionFactory")
//	@Primary
//	public SqlSessionFactory sqlSessionFactoryImassLoanBean(@Qualifier("shardingDatasource") DataSource dataSource)
//			throws Exception {
//		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//		bean.setDataSource(dataSource);
//		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*Mapper.xml"));
//		return bean.getObject();
//	}
//
//	@Bean
//	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
//
//	/**
//	 * 配置事务管理
//	 */
//	@Bean(name = "transactionManager")
//	@Primary
//	public DataSourceTransactionManager testTransactionManager(@Qualifier("shardingDatasource") DataSource dataSource) {
//		return new DataSourceTransactionManager(dataSource);
//	}

}

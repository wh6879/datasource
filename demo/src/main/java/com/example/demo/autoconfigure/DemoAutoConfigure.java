package com.example.demo.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.datasource.MyDatasource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.dto")
// @EnableTransactionManagement
public class DemoAutoConfigure {


    /*@Bean
    public DataSource dataSource() {
        return new MyDatasource();
    }*/

    @Bean(name = "dataSourceDruid")
    public DataSource dataSourceDruid() {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl("jdbc:mysql://192.168.96.138:3306/wh?serverTimezone=PRC");
        dataSource.setUsername("root");
        dataSource.setPassword("12qw!@QW");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }

    @Value("${jpa.mgt.entity.basepath:com.example.demo.**}")
    public String[] entityBasePath;

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSourceDruid")DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);

        System.out.println("----------"+entityBasePath.toString());
        entityManagerFactoryBean.setPackagesToScan(entityBasePath);
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        // hibernateJpaVendorAdapter.setGenerateDdl(false);
        entityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);

        Properties properties = new Properties();
        properties.putIfAbsent(AvailableSettings.AUTO_EVICT_COLLECTION_CACHE, "true");
        properties.putIfAbsent(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.putIfAbsent(AvailableSettings.SHOW_SQL, false);

        entityManagerFactoryBean.setJpaProperties(properties);
        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory masterEntityManagerFactory) {
        return new JpaTransactionManager(masterEntityManagerFactory);
    }
}

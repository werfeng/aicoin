package com.example.aicoin.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
            basePackages = "com.example.aicoin.dao.zet",
        entityManagerFactoryRef = "zetEntityManagerFactory",
        transactionManagerRef = "zetTransactionManager"
)
public class ZETConfig {
    @Resource
    @Qualifier("zetDataSource")
    private DataSource zetDataSource;


    @Primary
    @Bean("zetEntityManager")
    public EntityManager zetEntityManager(EntityManagerFactoryBuilder builder) {
        return zetEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Resource
    private Properties jpaProperties;

    @Primary
    @Bean(name = "zetEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean zetEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(zetDataSource)
                .packages("com.example.aicoin.entity.zet")
                .persistenceUnit("zetPersistenceUnit")
                .build();
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    @Primary
    @Bean(name = "zetTransactionManager")
    public PlatformTransactionManager zetTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(zetEntityManagerFactory(builder).getObject());
    }
}
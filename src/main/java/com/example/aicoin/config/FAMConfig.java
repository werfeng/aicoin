package com.example.aicoin.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "com.example.aicoin.dao.fam",
        entityManagerFactoryRef = "famEntityManagerFactory",
        transactionManagerRef = "famTransactionManager"
)
public class FAMConfig {

    @Resource
    @Qualifier("famDataSource")
    private DataSource famDataSource;


    @Bean("famEntityManager")
    public EntityManager famEntityManager(EntityManagerFactoryBuilder builder) {
        return famEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Resource
    private Properties jpaProperties;

    @Bean(name = "famEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean famEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(famDataSource)
                .packages("com.example.aicoin.entity.fam")
                .persistenceUnit("famPersistenceUnit")
                .build();
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    @Bean(name = "famTransactionManager")
    public PlatformTransactionManager famTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(famEntityManagerFactory(builder).getObject());
    }
}
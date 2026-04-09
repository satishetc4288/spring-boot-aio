package com.satish.exp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Custom DataSource configuration.
 *
 * Spring Boot's DataSourceAutoConfiguration and HibernateJpaAutoConfiguration
 * are excluded in Application.java so that this class takes full control of:
 *  - HikariCP connection pool
 *  - JPA EntityManagerFactory
 *  - JpaTransactionManager
 *
 * All settings are read from the custom "app.datasource.*" and "app.jpa.*"
 * namespaces defined in application.properties.
 */
@Configuration
public class DatabaseConfig {

    // ----------------------------------------------------------------
    // DataSource properties
    // ----------------------------------------------------------------
    @Value("${app.datasource.url}")
    private String jdbcUrl;

    @Value("${app.datasource.username}")
    private String username;

    @Value("${app.datasource.password}")
    private String password;

    @Value("${app.datasource.driver-class-name}")
    private String driverClassName;

    // ----------------------------------------------------------------
    // HikariCP pool properties
    // ----------------------------------------------------------------
    @Value("${app.datasource.hikari.pool-name:AIO-HikariPool}")
    private String poolName;

    @Value("${app.datasource.hikari.maximum-pool-size:10}")
    private int maximumPoolSize;

    @Value("${app.datasource.hikari.minimum-idle:2}")
    private int minimumIdle;

    @Value("${app.datasource.hikari.idle-timeout:30000}")
    private long idleTimeout;

    @Value("${app.datasource.hikari.connection-timeout:20000}")
    private long connectionTimeout;

    @Value("${app.datasource.hikari.max-lifetime:1800000}")
    private long maxLifetime;

    // ----------------------------------------------------------------
    // JPA / Hibernate properties
    // ----------------------------------------------------------------
    @Value("${app.jpa.dialect:org.hibernate.dialect.PostgreSQLDialect}")
    private String hibernateDialect;

    @Value("${app.jpa.ddl-auto:update}")
    private String ddlAuto;

    @Value("${app.jpa.show-sql:true}")
    private boolean showSql;

    // ----------------------------------------------------------------
    // Beans
    // ----------------------------------------------------------------

    /**
     * Builds a HikariCP DataSource from the custom app.datasource.* properties.
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setPoolName(poolName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setIdleTimeout(idleTimeout);
        config.setConnectionTimeout(connectionTimeout);
        config.setMaxLifetime(maxLifetime);

        // Optional: health check query
        config.setConnectionTestQuery("SELECT 1");

        return new HikariDataSource(config);
    }

    /**
     * Configures the JPA EntityManagerFactory, pointing at the base package
     * where all @Entity classes live.
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        // Scan this package for all @Entity classes
        em.setPackagesToScan("com.satish.exp");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(showSql);
        em.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", hibernateDialect);
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        jpaProperties.setProperty("hibernate.show_sql", String.valueOf(showSql));
        jpaProperties.setProperty("hibernate.format_sql", "true");
        em.setJpaProperties(jpaProperties);

        return em;
    }

    /**
     * Wires the JpaTransactionManager with the custom EntityManagerFactory.
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

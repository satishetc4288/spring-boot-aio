package com.satish.exp.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Custom JTA / XA DataSource configuration using Atomikos.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.satish.exp.repo",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class DatabaseConfig {

    // ----------------------------------------------------------------
    // DataSource properties (primary DB)
    // ----------------------------------------------------------------
    @Value("${app.datasource.url}")
    private String jdbcUrl;

    @Value("${app.datasource.url2}")
    private String jdbcUrl2;

    @Value("${app.datasource.username}")
    private String username;

    @Value("${app.datasource.password}")
    private String password;

    @Value("${app.datasource.driver-class-name}")
    private String driverClassName;

    // ----------------------------------------------------------------
    // HikariCP pool properties (re-purposed for Atomikos pool size)
    // ----------------------------------------------------------------
    @Value("${app.datasource.hikari.maximum-pool-size:10}")
    private int maximumPoolSize;

    @Value("${app.datasource.hikari.minimum-idle:2}")
    private int minimumIdle;

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
     * Builds an Atomikos JTA XA DataSource from the custom app.datasource.* properties.
     */
    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
    public DataSource dataSource() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("primaryXA");
        ds.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");

        Properties properties = new Properties();
        properties.setProperty("url", jdbcUrl);
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        ds.setXaProperties(properties);

        ds.setMinPoolSize(minimumIdle);
        ds.setMaxPoolSize(maximumPoolSize);
        return ds;
    }

    /**
     * Configures the JPA EntityManagerFactory, pointing at the base package
     * where all @Entity classes live.
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setJtaDataSource(dataSource);
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
        jpaProperties.setProperty("hibernate.transaction.coordinator_class", "jta");
        em.setJpaProperties(jpaProperties);

        return em;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager userTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean
    public UserTransactionImp userTransaction() throws Exception {
        UserTransactionImp userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(300);
        return userTransaction;
    }

    /**
     * Wires the JtaTransactionManager.
     */
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager() throws Exception {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransaction());
        return jtaTransactionManager;
    }

    @Bean
    @Primary
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    // ================================================================
    // Secondary DataSource — india1 (app.datasource.url2)
    // ================================================================

    /**
     * Secondary Atomikos JTA XA DataSource pointing at the india1 database.
     */
    @Bean(name = "dataSource2", initMethod = "init", destroyMethod = "close")
    public DataSource dataSource2() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("secondaryXA");
        ds.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");

        Properties properties = new Properties();
        properties.setProperty("url", jdbcUrl2);
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        ds.setXaProperties(properties);

        ds.setMinPoolSize(minimumIdle);
        ds.setMaxPoolSize(maximumPoolSize);
        return ds;
    }

    /**
     * Secondary EntityManagerFactory for the india1 database.
     */
    @Bean(name = "entityManagerFactory2")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory2(
            @Qualifier("dataSource2") DataSource dataSource2) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJtaDataSource(dataSource2);
        em.setPersistenceUnitName("india1PU");
        em.setPackagesToScan("com.satish.exp");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(showSql);
        em.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", hibernateDialect);
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        jpaProperties.setProperty("hibernate.show_sql", String.valueOf(showSql));
        jpaProperties.setProperty("hibernate.format_sql", "true");
        jpaProperties.setProperty("hibernate.transaction.coordinator_class", "jta");
        em.setJpaProperties(jpaProperties);

        return em;
    }

    @Bean(name = "jdbcTemplate2")
    public NamedParameterJdbcTemplate jdbcTemplate2(@Qualifier("dataSource2") DataSource dataSource2) {
        return new NamedParameterJdbcTemplate(dataSource2);
    }
}

package org.project.exchange.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import javax.sql.DataSource;

@Component
public class DataSourceCleanup {

    @Autowired
    private DataSource dataSource;

    @PreDestroy
    public void cleanUp() {
        try {
            if (dataSource instanceof com.zaxxer.hikari.HikariDataSource) {
                ((com.zaxxer.hikari.HikariDataSource) dataSource).close();
                System.out.println("HikariCP connection pool closed successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error while closing HikariCP connection pool: " + e.getMessage());
        }
    }
}

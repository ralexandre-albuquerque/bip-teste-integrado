package com.beneficio.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(DataSource dataSource) {
        return args -> {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            
            // Executa schema.sql primeiro
            populator.addScript(new FileSystemResource("db/schema.sql"));
            
            // Depois executa seed.sql
            populator.addScript(new FileSystemResource("db/seed.sql"));
            
            populator.execute(dataSource);
            
            System.out.println("✅ Scripts executados com sucesso: schema.sql e seed.sql");
        };
    }
}

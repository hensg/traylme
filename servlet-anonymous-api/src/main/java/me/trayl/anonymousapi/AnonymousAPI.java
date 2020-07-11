package me.trayl.anonymousapi;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"me.trayl.common.dao", "me.trayl.anonymousapi.shortedpath"})
@EnableJpaRepositories(basePackages = {"me.trayl.common.dao", "me.trayl.anonymousapi.shortedpath"})
public class AnonymousAPI {

    public static void main(String[] args) {
        SpringApplication.run(AnonymousAPI.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000", "https://trayl.me");
            }
        };
    }

    @Bean
    public Module hibernateAware() {
        return new Hibernate5Module();
    }

}

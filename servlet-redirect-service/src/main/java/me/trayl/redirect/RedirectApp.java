package me.trayl.redirect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"me.trayl.common.dao"})
@EnableJpaRepositories(basePackages = {"me.trayl.common.dao"})
public class RedirectApp {

    public static void main(String[] args) {
        SpringApplication.run(RedirectApp.class, args);
    }
}

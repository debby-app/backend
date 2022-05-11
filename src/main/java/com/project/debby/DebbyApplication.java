package com.project.debby;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class DebbyApplication {

    public static void main(String[] args) {
        log.info(System.getenv());
        SpringApplication.run(DebbyApplication.class, args);
    }

}

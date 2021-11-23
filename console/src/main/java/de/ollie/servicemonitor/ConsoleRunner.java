package de.ollie.servicemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ollie (23.11.2021)
 */
@SpringBootApplication
@ComponentScan("de.ollie")
public class ConsoleRunner {
    
    public static void main(String[] args) {
        SpringApplication.run(ConsoleRunner.class, args);
        System.out.println("Hello World!");
        // 1. Read configuration from file defined by a args parameter.
        // 2. Convert configuration to a list of CheckRequest.
        // 3. Call the MonitorServce.monitor method.
        // 4. Print the result to the console.
    }

}
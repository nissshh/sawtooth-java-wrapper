/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Starter application for Spring Boot.
 * 
 * @author
 *
 */
@SpringBootApplication
@ComponentScan("com.mycompany.blockchain.sawtooth")
@Slf4j
public class Application {
	public static void main(String[] args) {
		log.info("Starting Spring Boot Application....");
		new SpringApplication(Application.class).run(args);
		log.info("Started Spring Boot Application....");
	}
}

/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.config;

import java.util.stream.Stream;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.mycompany.blockchain.sawtooth.app.data.SigningUserRepository;
import com.mycompany.blockchain.sawtooth.app.model.SigningUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Starter application for Spring Boot.
 * 
 * @author  Nishant Sonar <nishant_sonar@yahoo.com>
 *
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class,UserDetailsServiceAutoConfiguration.class }
, scanBasePackages = "com.mycompany.blockchain.sawtooth.app")
@Slf4j
public class Application {
	public static void main(String[] args) {
		log.info("Starting Spring Boot Application....");
		new SpringApplication(Application.class).run(args);
		log.info("Started Spring Boot Application....");
	}
	
	//@Bean
    /*ApplicationRunner init(SigningUserRepository repository) {
		String[][] users = { 
				{ "nishant", "sonar", "036144311d1978082293e3fe7d6b1aa4a5fcff65e8b2680c12f9974c2ff9ca89a1", "bff194b6698b5a20ba531eaa0411fea57c4a7289fa31e474ca68a13a9bf9f4e7" },
				{ "prashant", "sonar", "0325eae0a1801924ae3e7cbe5f4644be828b964b616926462d983ed23fc9366b98", "be10e1ddafa8e9b89fc647ad8e53cc1d1d60a3365df41cdebea72c11bed29b34" },
				{ "sandip", "nirmal", "028b0d1c373eeb0b6d789178e7a11954368fdf660c8d9c9be31403365f73e86a5d", "c5dda7827d683ee5e1ca61ed8a6145def3e075946f0ece95be829cf4459e65cf" },
				{ "synechron", "synechron", "039390578f25ac4eaec37bd1f92e3b7d68e231bbf0061839f6a29748650fbf4359", "cc344d4948f2e26f646f3123c64cca1e8da0de6d823e4ee3e1f242a2abe02dbc" }
				}; 
        return args -> {
            Stream.of(users).forEach(name -> {
                SigningUser user = new SigningUser();
                user.setUsername(name[0]);
                user.setPassword(name[1]);
                user.setPubKey(name[2]);
                user.setPvtKey(name[3]);
                repository.save(user);
            });
            repository.findAll().forEach(user -> {
            	log.info("User {}  configured!",user);
            });
        };
    }*/
}

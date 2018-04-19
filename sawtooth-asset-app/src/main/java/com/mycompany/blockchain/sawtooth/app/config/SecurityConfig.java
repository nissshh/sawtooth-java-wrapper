/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Simple  configuration for security 
 * @author Nishant Sonar
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	//@Autowired
	private SimpleJPAAuthenticationProvider authProvider;
 
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        inMemorySetup(auth);
        //jpaSetup(auth);
    }
    private void jpaSetup(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(authProvider);
    }
    
	private void inMemorySetup(AuthenticationManagerBuilder auth) throws Exception {
		auth
          .inMemoryAuthentication()
          .passwordEncoder(passwordEncoder())
          .withUser("user")
          	.password("{noop}password")
          	.roles("USER")
          .and()
          .withUser("admin")
          	.password("{noop}admin")
          	.roles("USER", "ADMIN");
	}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic()
			.and()
	          .authorizeRequests()
	          .antMatchers("/").anonymous()
	          .antMatchers("/healthcheck/").anonymous()
	          .anyRequest()
	          .authenticated()
	        .and()
	        	.csrf().disable();
		}
}

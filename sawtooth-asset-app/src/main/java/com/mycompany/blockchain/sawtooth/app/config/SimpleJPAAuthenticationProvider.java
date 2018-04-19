/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.mycompany.blockchain.sawtooth.app.data.SigningUserRepository;
import com.mycompany.blockchain.sawtooth.app.model.SigningUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * A custom authentication provider that used JPA Repository 
 * 
 * @author Nishant Sonar
 *
 */
@Slf4j
//@Component
public class SimpleJPAAuthenticationProvider implements AuthenticationProvider {

	//@Autowired
	SigningUserRepository repository;

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication){
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		//List<SigningUser> users = repository.findAll();
		List<SigningUser> users = null;
		Optional<SigningUser> optionalUser = users.stream().filter(u -> {
			return u.getUsername().equals(name) && u.getPassword().equals(password);
		}).findFirst();
 
		if (!optionalUser.isPresent()) {
			log.error("Authentication failed for user = " + name);
			throw new BadCredentialsException("Authentication failed for user = " + name);
		}
 
		// find out the exited users
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(optionalUser.get().getRole()));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(name, password,
				grantedAuthorities);
 
		log.info("Succesful Authentication with user = " + name);
		return auth;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

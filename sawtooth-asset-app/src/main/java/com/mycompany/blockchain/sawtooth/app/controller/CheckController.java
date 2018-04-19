/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * A simple controller for health check
 * 
 * @author Nishant Sonar
 *
 */
@RestController
public class CheckController {

	@GetMapping("/healthcheck")
	public int retrieveCoursesForStudent() {
		return 1;
	}
}

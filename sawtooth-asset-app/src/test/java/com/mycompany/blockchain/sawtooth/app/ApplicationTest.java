/**
 * 
 */
package com.mycompany.blockchain.sawtooth.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mycompany.blockchain.sawtooth.app.config.Application;

/**
 * 
 * Only tests the application load
 * 
 * @author dev
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

	@Test
	public void contextLoads() {
	}

}

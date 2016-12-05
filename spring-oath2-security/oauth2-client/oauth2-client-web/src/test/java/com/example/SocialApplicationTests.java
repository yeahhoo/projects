package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"spring.config.location = classpath:client-test.yml"})
public class SocialApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(SocialApplicationTests.class);

	@Test
	public void contextLoads() {
            logger.info("TESTS STARTED");
	}

}

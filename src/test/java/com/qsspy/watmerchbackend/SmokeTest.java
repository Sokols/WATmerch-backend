package com.qsspy.watmerchbackend;

import com.qsspy.watmerchbackend.controller.standard.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SmokeTest {

	@Autowired
	private HelloController helloController;

	@Test
	void contextLoads() {
		assertThat(helloController).isNotNull();
	}

}

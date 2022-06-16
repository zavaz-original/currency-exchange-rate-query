package com.sb_juhav.siili_exchange_rates_test;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sb_juhav.siili_exchange_rates.SiiliExchangeRatesApplication;


@WebAppConfiguration
@ContextConfiguration(classes = SiiliExchangeRatesApplication.class)
@SpringBootTest
class SiiliExchangeRatesApplicationTests {

	@Test
	void contextLoads() {
	}

}


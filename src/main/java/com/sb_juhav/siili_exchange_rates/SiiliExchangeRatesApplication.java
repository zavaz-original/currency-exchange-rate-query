package com.sb_juhav.siili_exchange_rates;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;	// 2022-06-13

@SpringBootApplication
@ComponentScan(basePackages = {"com.sb_juhav.siili_exchange_rates"}) // 2022-06-13
public class SiiliExchangeRatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiiliExchangeRatesApplication.class, args);
	}
}


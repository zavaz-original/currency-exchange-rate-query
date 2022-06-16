package com.sb_juhav.siili_exchange_rates;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb_juhav.siili_exchange_rates.DAO.ExchangeRateDao;
import com.sb_juhav.siili_exchange_rates.DTO.ExchangeRateRequestDTO;

// https://apilayer.com/marketplace/description/exchangerates_data-api

@RestController
public class Controller {

	// You need to set your own apikey via GET endpoint
	// set_apikey.

	// This apikey won't work for a month. The monthly
	// free amount of requests was exceeded during development
	// of the Quartz Cron-Job for the same API site.
	// The API site has also some other controls than cookies.
	// A new apikey generated with all 3 different 
	// (different computer & different valid email address &
	// of course a different apikey + cookies deleted) gets rejected 
	// from this laptop.
	// The rejection has just a different cause code.
	private static String apikey = "HZOwa1dYHjD4MEEA0KNPsOiOEwqhQcvt";
	private static final int apikeyLength = apikey.length();

	/* 
	   		.../set_apikey?apikey="???..."
	 */
	
	@GetMapping("/set_apikey")
	public String setApikey(
			@RequestParam(value="apikey", required=false) String newApikey) {
		if ( newApikey == null ) {
			return ".../set_apikey?apikey=YourNewApiKey expected in http address";
		}
		if ( newApikey.length() == apikeyLength ) {
			apikey = newApikey;
			return "apikey updated to: " + apikey;
		}
		if ( newApikey.length() < apikeyLength ) {
			return "Too short apikey: length " + newApikey.length() + " while " + apikeyLength + " expected";  
		}
		return "Too long apikey: length " + newApikey.length() + " while " + apikeyLength + " expected";  
	}

	@GetMapping("/exchange_amount")
	public String fetchExchangeInformation(
			@RequestParam(value="from", required=false) String fromCurrency,
			@RequestParam(value="to", required=false) String toCurrency,
					// only an integer accepted by remote API server
			@RequestParam(value="amount", required=false) String fromAmountAsString ) 
					throws ParseException, URISyntaxException {

		System.out.println("### Entered Controller @GetMapping endpoint for /exchange_amount");
		
		
		String exampleFormat = ".../exchange_amount?from=USD&to=EUR&amount=10 is an example end of a valid http link here";
		
		System.out.println(exampleFormat);
		
		if ( fromCurrency == null || toCurrency == null || fromAmountAsString == null ) {
			return exampleFormat;
		}
		int fromAmount;
		try {
			fromAmount = Integer.parseInt(fromAmountAsString);
		}
		catch( Exception e) {
			return "Please, give a whole number without decimals after \"amount=\" in the link (e.g. amount=10 instead of 10.25 or 10,25)";
		}
		
		ExchangeRateRequestDTO exchangeRateRequestDTO = new ExchangeRateRequestDTO();
		exchangeRateRequestDTO.setFrom( fromCurrency );
		exchangeRateRequestDTO.setTo( toCurrency );
		exchangeRateRequestDTO.setFromAmount( fromAmount );

		Optional<Object> response;

		try {
			response = ExchangeRateDao.fetch(exchangeRateRequestDTO, apikey);

			if (response.isPresent()) {
				if ( ((Object) response.get()).getClass() == String.class ) {
					return (String) response.get();
				}
				return response.get().toString();
			}

		} catch (Exception e) {
			System.out.println("--- Exception in ExchangeRateDao.fetch : " + e);
		}
		return "Error in getting data with from=" + fromCurrency + " amount=" + fromAmount + " to=" + toCurrency + " (in any order)";
	}

	public Controller() {}

}

// POSTMAN:
// http://api.apilayer.com/exchangerates_data/convert?to=USD&from=EUR&amount=1&apikey=HZOwa1dYHjD4MEEA0KNPsOiOEwqhQcvt

/*
Endpoints:

GET exchange_amount
	// EUR, SEK and USD need to be supported
	Request:
		from
		to
		from_amount
	Response:
		from
		to
		to_amount
		exchange_rate

  +++++++

    Cron job:
	Quartz to fetch and store exchange rates once an hour
	EUR, SEK and USD need to be supported
	Service where exchange rates can be fetched
	https://apilayer.com/marketplace/description/exchangerates_data-api
 */




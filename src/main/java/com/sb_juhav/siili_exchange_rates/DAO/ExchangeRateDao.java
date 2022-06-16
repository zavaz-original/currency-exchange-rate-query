package com.sb_juhav.siili_exchange_rates.DAO;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.client.RestTemplate;

import com.sb_juhav.siili_exchange_rates.DTO.ExchangeRateRequestDTO;
import com.sb_juhav.siili_exchange_rates.DTO.ExchangeRateResponseDTO;

/*
Example of a json response from the url:

{
"success": true,
"query": {
	"from": "EUR",
	"to": "USD",
	"amount": 1
},
"info": {
	"timestamp": 1652957583,
	"rate": 1.052443
},
"date": "2022-05-19",
"result": 1.052443
}
 */

// only static methods and static final constants on class level here

public class ExchangeRateDao {

	// "http://api.apilayer.com/exchangerates_data/convert?from=EUR&to=USD&amount=1&apikey=HZOwa1dYHjD4MEEA0KNPsOiOEwqhQcvt";
	// example old apikey = "HZOwa1dYHjD4MEEA0KNPsOiOEwqhQcvt";

	private static final String requestUrlTemplate = "http://api.apilayer.com/exchangerates_data/convert?from=<fromCurrency>&to=<toCurrency>&amount=<fromAmount>&apikey=<apikey>";

	private static String getUrlString(ExchangeRateRequestDTO exchangeRateRequestDTO, String apikey) {

		String url = requestUrlTemplate
				.replace("<fromCurrency>", exchangeRateRequestDTO.getFrom() )
				.replace("<toCurrency>", exchangeRateRequestDTO.getTo())
				.replace("<fromAmount>", "" + exchangeRateRequestDTO.getFromAmount())
				.replace("<apikey>", apikey);

		return url;
	}

	private static Optional<Object> parseServerError(String serverError) {

		Optional<Object> returnValueIfNull = Optional.ofNullable((Object) serverError);

		if (returnValueIfNull.isEmpty()) {
			String bug = "--- BUG1 outside of remote Exchange Rate API server: ExchangeRateDao.parseServerError called with a null String";
			Optional<Object> optionalBug = Optional.of( (Object) bug);
			return optionalBug;
		}

		// my current API server side error:
		// ... "{"message":"You have exceeded your daily\/monthly API rate limit. Please review and upgrade your subscription plan at https:\/\/promptapi.com\/subscriptions to continue."}"

		String msg = serverError.replace("\"", "");
		msg = msg.replace(":", "");
		msg = msg.replace("{", "");
		msg = msg.replace("}", "");
		String expectedSubstring = "message";
		int index = msg.indexOf(expectedSubstring);
		if (index >= 0 ) {
			msg = msg.substring(index + expectedSubstring.length());
		}
		return Optional.of(msg);
	}

	private static ExchangeRateResponseDTO exchangeRateResponseDTO = new ExchangeRateResponseDTO();


	// Beef of the burger {

	public static final Optional<Object> fetch( 
			ExchangeRateRequestDTO exchangeRateRequestDTO,
			String apikey) 
					throws ParseException
					, URISyntaxException
					, Exception {

		String json = null;
		String  url = null;
		URI     uri = null;

		try {
			url = getUrlString(exchangeRateRequestDTO, apikey);

			System.out.println("INFO: fetch url: " + url);

			uri = new URI(url);

			RestTemplate restTemplate = new RestTemplate();

			json = restTemplate.getForObject( uri, String.class ); // !!!

			// No Exception so the uri returned a string as response

			System.out.println("+++ json: " + json  );

		} catch (Exception e) { 
			// no way to continue processing without a json String 
			Optional<Object> errorMsg = parseServerError(e.toString());
			System.out.println("--- Exception in ExchangeRateDao.fetch: " + e);
			if (errorMsg.isPresent()) {
				return errorMsg;
			}
			// this should never happen unless a new bug in code here
			return Optional.of("--- BUG2 outside of remote Exchange Rate API server in ExchangeRateDao.fetch");	
		}

		// from here on any problem is in parsing of the json format	

		Optional<Object> response = Optional.ofNullable(null);

		try {
			JsonParser springParser = JsonParserFactory.getJsonParser();

			Map <String,Object> map = springParser.parseMap(json);	// !!!

			boolean success = (boolean) map.get("success");

			Map<String, Object> query = (Map<String, Object>) map.get("query");

			String fromCurrency = (String) query.get("from");
			String toCurrency   = (String) query.get("to");
			int fromAmount = (int) query.get("amount");

			Map<String, Object> info = (Map<String, Object>) map.get("info");

			int timestamp = (int) info.get("timestamp");

			double exchangeRate = (double) info.get("rate");

			String dateString = (String) map.get("date");

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			Date date = dateFormat.parse(dateString);  

			double toAmount = (double) map.get("result");	// to-amount

			exchangeRateResponseDTO.setDate(date);
			exchangeRateResponseDTO.setFrom(fromCurrency);
			exchangeRateResponseDTO.setTo(toCurrency);
			exchangeRateResponseDTO.setFromAmount(fromAmount);
			exchangeRateResponseDTO.setToAmount(toAmount);
			exchangeRateResponseDTO.setExchangeRate(exchangeRate);

			System.out.println("+++ " + exchangeRateResponseDTO.toString() );

			response = Optional.of(exchangeRateResponseDTO);
		} catch (Exception e) {
			String jsonParsingErrorMessage = "--- BUG3 outside of remote Exchange Rate API server: Exception while parsing json in ExchangeRateDao.fetch() : " + e.toString();
			System.out.println(jsonParsingErrorMessage + " : " + e.toString() );
			return Optional.of(jsonParsingErrorMessage);
		}

		return response;
	}

	// Beef of the burger }

}

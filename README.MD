Virnex Oy / juha valimaki 2022-06-13

# User Guide for ExchangeRates App

### Note 1: The name siili will be removed from the java/Spring Boot source codes:

There is a company named Siili Solutions Oyj and their web site is siili.com, but this App is NOT from the Siili Solutions Oyj / Siili.com

I will handle the renaming in the software ASAP and update Github accordingly.

### Note 2: The directory (folder) screenshots contains a Postman screenshot example

That example shows the response when the amount of queries has exceeded the daily or monthly limit.

### What does this App do?
This App is a currency "translator". You can find out how much an amount of money in currency X corresponds to in currency Y. You can do this from your web browser. The App queries the Currency Exchange Rate from a server API in the internet. The correctness of the response depends on the API server described at:

<https://apilayer.com/marketplace/exchangerates_data-api>
  
At least following currency translations are supported at the time of writing:
- USD to EUR
- USD to SEK
- EUR to USD
- EUR to SEK
- SEK to USD
- SEK to EUR

### apikey is your unique id needed in the queries

apikey is described further below.

### Unless you already have one, please create your user account at:
<https://apilayer.com/marketplace/exchangerates_data-api>

At the time of writing a free user account allows up to 250 queries per month. Only your valid email address is required to create a free account - no credit/debit card information required. They offer subscriptions with higher max amount of queries. There can be both a daily limit and a different monthly total limit depending on your subscription. You will see the up-to-date limits when you create/upgrade your account or when you just log in there.

### After logging in to your account you can copy the value of your unique apikey parameter

The value of the apikey is a string of characters. You can also replace your existing apikey with a new value by clicking the "regenerate" button provided that you have not exceeded any limit of your account type. Change of apikey does not help to exceed the limits of your subscription. 

#### This App requires your apikey to work:

### Set your apikey to this App using the below link:
<http://localhost:8080/set_apikey?apikey="YourApikeyHere">

Replace the "YourApiKey" with the value shown while logged in to your account. 

### Click any of the links provided further down below to run a query

Your browser address bar you will show the same link.

In the link the changable values are behind the questionmark character "?":

### from
The "from-currency" of the currency translation.

### to
The "to-currency" of the currency translation.

### amount
The amount of "from-Currency" you want to translate to "to-currency".

## Change the amount value in the browser address bar to any whole amount (1, 2, ... ) of "from Currency" you want to "translate" to "to Currency"

### Do not use any decimals in the amount:

Instead of 100.25 use 100

Instead of 100,25 use 100

Because amounts with decimals are not supported.

### Ready links to use and you can replace the amount in your browser address bar:

USD->EUR:
How much EUR would you get for 1 USD ?
<http://localhost:8080/exchange_amount?from=USD&to=EUR&amount=1>

USD->SEK:
How much SEK would you get for 1 USD ?
<http://localhost:8080/exchange_amount?from=USD&to=SEK&amount=1>

EUR->USD:
How much USD would you get for 1 EUR ?
<http://localhost:8080/exchange_amount?from=EUR&to=USD&amount=1>

EUR->SEK:
How much SEK would you get for 1 EUR ?
<http://localhost:8080/exchange_amount?from=EUR&to=SEK&amount=1>

SEK->USD:
How much USD would you get for 1 SEK ?
<http://localhost:8080/exchange_amount?from=SEK&to=USD&amount=1>

SEK->EUR:
How much EUR would you get for 1 SEK ?
<http://localhost:8080/exchange_amount?from=SEK&to=EUR&amount=1>

## Change the amount value in the browser address bar to any whole amount (1, 2, ... ) of "from Currency" you want to "translate" to "to Currency"

You can modify the values of "from", "to" and "amount" in the address bar.

The order of these 3 input (parameter) "fields" behind the "?" in the address (URL) can be freely changed:

...?from=USD&to=SEK&amount=100

...?from=USD&amount=100&to=SEK

...?to=SEK&from=USD&amount=100

... any other order

are all valid and equal 

E.g. the amount part is here in the middle:
<http://localhost:8080/exchange_amount?from=USD&amount=100&to=SEK>

That's all for now.

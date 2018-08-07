package com.example.orin1.moneyconverter;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UrlInfoGet extends AsyncTask<Void, Void, List<Currency>> {

    private String resultJson = "";
    private List<Currency> currencyList = new ArrayList<>();
    private DataLoadedCallback dataLoadedCallback;
    private final String urlStr = "https://exchangeratesapi.io/api/latest";


    public UrlInfoGet(DataLoadedCallback dataLoadedCallback) {
        this.dataLoadedCallback = dataLoadedCallback;
    }

    @Override
    protected List<Currency> doInBackground(Void... params) {
        // получаем данные с внешнего ресурса
        try {
            URL url = new URL(urlStr);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("loaded", "_____________________5");

//        dataLoadedCallback.onDataloadedCallback();

        try {

            JSONObject dataJsonObj = new JSONObject(resultJson);
            String currency = dataJsonObj.getString("rates");

            JSONObject dataJsonObj1 = new JSONObject(currency);
            String aud = dataJsonObj1.getString("AUD");
            String czk = dataJsonObj1.getString("CZK");
            String gbp = dataJsonObj1.getString("GBP");
            String usd = dataJsonObj1.getString("USD");
            String rub = dataJsonObj1.getString("RUB");

            String jpy = dataJsonObj1.getString("JPY");
            String bgn = dataJsonObj1.getString("BGN");
            String chf = dataJsonObj1.getString("CHF");
            String ils = dataJsonObj1.getString("ILS");
            String krw = dataJsonObj1.getString("KRW");

            currencyList.add(new Currency("AUD", Double.valueOf(aud)));
            currencyList.add(new Currency("CZK", Double.valueOf(czk)));
            currencyList.add(new Currency("GBP", Double.valueOf(gbp)));
            currencyList.add(new Currency("USD", Double.valueOf(usd)));
            currencyList.add(new Currency("RUB", Double.valueOf(rub)));

            currencyList.add(new Currency("JPY", Double.valueOf(jpy)));
            currencyList.add(new Currency("BGN", Double.valueOf(bgn)));
            currencyList.add(new Currency("CHF", Double.valueOf(chf)));
            currencyList.add(new Currency("ILS", Double.valueOf(ils)));
            currencyList.add(new Currency("KRW", Double.valueOf(krw)));

            currencyList.add(new Currency("EUR", 1.0));

            Collections.sort(currencyList);

            dataLoadedCallback.onDataloadedCallback();

            Log.i("loaded", "_____________________4");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return currencyList;
    }

//    @Override
//    protected void onPostExecute(String strJson) {
//        super.onPostExecute(strJson);
//
//        try {
//
//            JSONObject dataJsonObj = new JSONObject(strJson);
//            String currency = dataJsonObj.getString("rates");
//
//            JSONObject dataJsonObj1 = new JSONObject(currency);
//            String aud = dataJsonObj1.getString("AUD");
//            String czk = dataJsonObj1.getString("CZK");
//            String gbp = dataJsonObj1.getString("GBP");
//            String usd = dataJsonObj1.getString("USD");
//            String rub = dataJsonObj1.getString("RUB");
//
//            String jpy = dataJsonObj1.getString("JPY");
//            String bgn = dataJsonObj1.getString("BGN");
//            String chf = dataJsonObj1.getString("CHF");
//            String ils = dataJsonObj1.getString("ILS");
//            String krw = dataJsonObj1.getString("KRW");
//
//            currencyList.add(new Currency("AUD", Double.valueOf(aud)));
//            currencyList.add(new Currency("CZK", Double.valueOf(czk)));
//            currencyList.add(new Currency("GBP", Double.valueOf(gbp)));
//            currencyList.add(new Currency("USD", Double.valueOf(usd)));
//            currencyList.add(new Currency("RUB", Double.valueOf(rub)));
//
//            currencyList.add(new Currency("JPY", Double.valueOf(jpy)));
//            currencyList.add(new Currency("BGN", Double.valueOf(bgn)));
//            currencyList.add(new Currency("CHF", Double.valueOf(chf)));
//            currencyList.add(new Currency("ILS", Double.valueOf(ils)));
//            currencyList.add(new Currency("KRW", Double.valueOf(krw)));
//
//            currencyList.add(new Currency("EUR", 1.0));
//
//            Collections.sort(currencyList);
//
//            dataLoadedCallback.onDataloadedCallback();
//
//            Log.i("loaded", "_____________________4");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }
}

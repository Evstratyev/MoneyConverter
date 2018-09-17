package com.example.orin1.moneyconverter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UrlInfoGet extends AsyncTask<Void, Void, List<Currency>> {

    private String resultJson = "";
    private final List<Currency> currencyRatesList = new ArrayList<>();
    private DataLoadedCallback dataLoadedCallback;
    //    private final String urlStr = "https://exchangeratesapi.io/api/latest";
    private final String urlStr = "https://api.exchangeratesapi.io/latest";

    private final List<String> currencyNameList = new ArrayList<>();


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

            Log.i("Json", resultJson);

        } catch (Exception e) {
            e.printStackTrace();

            resultJson = "";
        }

        try {

//            Log.i("Json", resultJson);

            JSONObject dataJsonObj = new JSONObject(resultJson);
            String currency = dataJsonObj.getString("rates");

            currencyNameList(currency);

            JSONObject dataJsonObj1 = new JSONObject(currency);

            for (String currencyJson : currencyNameList) {
                if (!currencyJson.equals("EUR")) {
                    String s = dataJsonObj1.getString(currencyJson);
                    currencyRatesList.add(new Currency(currencyJson, Double.valueOf(s)));
                }
            }

            currencyRatesList.add(new Currency("EUR", 1.0));

            Collections.sort(currencyRatesList);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currencyRatesList;

    }

    @Override
    protected void onPostExecute(List<Currency> currencyList) {
        super.onPostExecute(currencyList);

        dataLoadedCallback.onDataloadedCallback();
    }

    public List<Currency> getCurrencyRatesList() {
        return currencyRatesList;
    }

    private void currencyNameList(String json) {

        String[] words = json.split("[a-z0-9{}.:\",]");

        currencyNameList.add("EUR");

        for (String word : words) {
            if (!word.equals("") & (word.length() == 3)) {
                currencyNameList.add(word);
            }
        }
        Collections.sort(currencyNameList);
        Log.i("ARR", currencyNameList.toString());
    }

    public List<String> getCurrencyNameList() {
        return currencyNameList;
    }
}

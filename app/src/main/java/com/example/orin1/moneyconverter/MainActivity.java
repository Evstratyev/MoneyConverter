package com.example.orin1.moneyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] data =
            {"AUD", "CZK", "GBP",
                    "USD", "RUB", "EUR",
                    "JPY", "BGN", "CHF",
                    "ILS", "KRW"};

    private EditText editTextUp;
    private EditText editTextDown;
    private Spinner spinner;
    private Spinner spinner2;
    private ProgressBar progressBar;

    private List<Currency> currencyList = new ArrayList<>();
    private RecyclerView recyclerView;

    private UrlInfoGet urlInfoGet;

    private boolean onDataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        editTextUp = findViewById(R.id.editText);
        editTextDown = findViewById(R.id.editText2);
        editTextUp.setEnabled(false);
        editTextDown.setEnabled(false);

        urlInfoGet = new UrlInfoGet(new DataLoadedCallback() {
            @Override
            public void onDataloadedCallback() {
                onDataLoaded = true;

//                spinner.setSelection(1);
                currencyList = urlInfoGet.getCurrencyList();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                editTextUp.setEnabled(true);
                spinner.setEnabled(true);
                spinner2.setEnabled(true);

                /*
                Ошибка

                initRecycleView(currencyList, 5);
                */
            }
        });

        urlInfoGet.execute();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter1);
        spinner.setSelection(5);
        spinner.setEnabled(false);

        spinner2 = findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(4);
        spinner2.setEnabled(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                editTextDown.setText(String.format("%.2f", +converter()).replace(",", "."));

                initRecycleView(currencyList, spinner.getSelectedItemPosition());

                Log.i("loaded", "_____________________6");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                editTextDown.setText(String.format("%.2f", +converter()).replace(",", "."));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        editTextUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editTextUp.getText().toString().equals("")) {

                    editTextDown.setText(String.format("%.2f", +converter()).replace(",", "."));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private double converter() {

        Double convertResult = 0.0;
        Double firstCurrency = 0.0;
        Double secondCurrency = 0.0;

        String firstCurrencyName = data[spinner.getSelectedItemPosition()];
        String secondCurrencyName = data[spinner2.getSelectedItemPosition()];

        for (Currency currency : currencyList) {
            if (currency.getName().equals(firstCurrencyName)) {
                firstCurrency = currency.getRate();
            }
        }
        for (Currency currency : currencyList) {
            if (currency.getName().equals(secondCurrencyName)) {
                secondCurrency = currency.getRate();
            }
        }

        if (!(editTextUp.getText().toString().equals(""))) {
            convertResult = Double.valueOf(editTextUp.getText().toString()) * (secondCurrency / firstCurrency);
        }
        return convertResult;
    }

    private void initRecycleView(List<Currency> currencyList, int currencyType) {

            String mainCurrency = data[currencyType];
            Double relativelyIndex = 0.0;

            for (Currency currency : currencyList) {
                if (currency.getName().equals(mainCurrency)) {
                    relativelyIndex = currency.getRate();
                    break;
                }
            }

            for (Currency currency : currencyList) {
                Double oldValue = 0.0;
                Double newRelativeValue = 0.0;

                oldValue = currency.getRate();

                newRelativeValue = Math.rint(1000.0 * (oldValue / relativelyIndex)) / 1000.0;

                currency.setRate(newRelativeValue);

            }

            recyclerView = findViewById(R.id.currency_recycle_view);
            CurrencyAdapter currencyAdapter = new CurrencyAdapter(currencyList);
            recyclerView.setAdapter(currencyAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> currencyNameList = new ArrayList<>();

    private EditText editTextUp;
    private EditText editTextDown;
    private Spinner spinner;
    private Spinner spinner2;
    private ProgressBar progressBar;

    private List<Currency> currencyList = new ArrayList<>();
    private RecyclerView recyclerView;

    private UrlInfoGet urlInfoGet;

    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;

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

        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        spinner.setEnabled(false);
        spinner2.setEnabled(false);

        urlInfoGet = new UrlInfoGet(new DataLoadedCallback() {
            @Override
            public void onDataloadedCallback() {

                onDataLoaded = true;
                currencyList = urlInfoGet.getCurrencyRatesList();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                editTextUp.setEnabled(true);
                spinner.setEnabled(true);
                spinner2.setEnabled(true);

                currencyNameList = urlInfoGet.getCurrencyNameList();

                initSpinnerAdapters(currencyNameList);
                /*
                Ошибка возникает если dataLoadedCallback.onDataloadedCallback(); срабатывает в бэкграунде
                как понять по стеку ошибок что проблема в том, что список валют еще не успел загрузиться

                initRecycleView(currencyList, 5);
                */

                initRecycleView(currencyList, 0);

            }
        });

        urlInfoGet.execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                spinnerAdapterData(spinner.getSelectedItemPosition());
                editTextDown.setText(String.format("%.2f", +converter()).replace(",", "."));
                initRecycleView(currencyList, spinner.getSelectedItemPosition());

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

        String firstCurrencyName = adapter1.getItem(spinner.getSelectedItemPosition());
        Log.i("MY_LOG", firstCurrencyName);
        String secondCurrencyName = adapter2.getItem(spinner2.getSelectedItemPosition());
        Log.i("MY_LOG", secondCurrencyName);

        for (Currency currency : currencyList) {
            if (currency.getName().equals(firstCurrencyName)) {
                firstCurrency = currency.getRate();
                Log.i("MY_LOG", firstCurrency.toString());
            }
        }
        for (Currency currency : currencyList) {
            if (currency.getName().equals(secondCurrencyName)) {
                secondCurrency = currency.getRate();
                Log.i("MY_LOG", secondCurrency.toString());
            }
        }

        if (!(editTextUp.getText().toString().equals(""))) {
            convertResult = Double.valueOf(editTextUp.getText().toString()) * (secondCurrency / firstCurrency);
        }
        Log.i("MY_LOG", convertResult.toString());
        return convertResult;
    }

    private void initRecycleView(List<Currency> currencyList, int currencyType) {

        List<Currency> tempCurrencyList = new ArrayList<>(currencyList);

        String mainCurrency = currencyNameList.get(currencyType);
        Double relativelyIndex = 0.0;

        for (Currency currency : currencyList) {

            if (currency.getName().equals(mainCurrency)) {
                relativelyIndex = currency.getRate();
                tempCurrencyList.remove(currency);

                break;
            }
        }

        for (Currency currency : currencyList) {
            Double oldValue = 0.0;
            Double newRelativeValue = 0.0;

            oldValue = currency.getRate();
            newRelativeValue = (oldValue / relativelyIndex);
            currency.setRate(newRelativeValue);

        }

        recyclerView = findViewById(R.id.currency_recycle_view);
        CurrencyAdapter currencyAdapter = new CurrencyAdapter(tempCurrencyList);
        recyclerView.setAdapter(currencyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setHasFixedSize(false);

    }

    private void initSpinnerAdapters(List<String> list) {

        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter1);
        spinner.setSelection(0);

        spinner2.setAdapter(adapter2);
        spinner2.setSelection(1);

    }

    private void spinnerAdapterData(int i) {
        List<String> temp = new ArrayList<>(currencyNameList);

        for (String s : temp) {
            if (s.equals(currencyNameList.get(i))) {
                temp.remove(s);
                Log.i("delete", s);
                adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, temp);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                Log.i("adapter2", adapter2.getItem(spinner2.getSelectedItemPosition()));

                break;
            }
        }
        /*
        при перезадании adapter2 , а именно массива значений для него (в зависимости от первого спиннера)
        что происходит с первоначально созданым adapter2 ? С тем у которого в параметры был передан
        изначальный массив ?
        вопрос от того, что пока не была продублирована spinner2.setAdapter(adapter2); в этом методе
        использовался первоначальный массив (в котором было повторение выбранного значения спинера1)
         */
    }
}

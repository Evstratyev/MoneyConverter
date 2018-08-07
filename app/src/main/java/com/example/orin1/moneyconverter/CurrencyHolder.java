package com.example.orin1.moneyconverter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CurrencyHolder extends  RecyclerView.ViewHolder{

    private TextView moneyNameTextView;
    private TextView rateTextView;

    public CurrencyHolder(View itemView) {
        super(itemView);

        moneyNameTextView = itemView.findViewById(R.id.money_id);
        rateTextView = itemView.findViewById(R.id.money_currency);
    }

    public void bind(Currency currency) {
        moneyNameTextView.setText(currency.getName());
        rateTextView.setText(String.valueOf(currency.getRate()));
    }

}

package com.example.orin1.moneyconverter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyHolder>{
    private List<Currency> currencyList;

    public CurrencyAdapter(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    @NonNull
    @Override
    public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_money, parent, false);
        return new CurrencyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyHolder holder, int position) {
        Currency currency = currencyList.get(position);
        holder.bind(currency);
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }
}

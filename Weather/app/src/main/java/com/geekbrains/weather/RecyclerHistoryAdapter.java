package com.geekbrains.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.HistoryHolder> {

    private List<String> listDate;
    SimpleDateFormat formatForDateNow;


    public RecyclerHistoryAdapter(@NonNull Context context, String cityName) {
        formatForDateNow = new SimpleDateFormat("E dd.MM.yyyy");
        initListDate();
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new HistoryHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder historyHolder, int position) {
        historyHolder.txtDate.setText(listDate.get(position));
    }

    @Override
    public int getItemCount() {
        return listDate.size();
    }


    public class HistoryHolder extends RecyclerView.ViewHolder{

        private TextView txtDate;

        public HistoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.history_item, parent, false));
            txtDate = itemView.findViewById(R.id.tv_date);
        }
    }


    private void initListDate(){
        listDate = new ArrayList<>();
        for (long i = 0; i < 30; i++) {
            long timeSpan = (1000*60*60*24)*i;
            listDate.add(formatForDateNow.format(new Date(System.currentTimeMillis() - timeSpan)));
        }
    }
}

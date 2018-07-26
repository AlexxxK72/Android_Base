package com.geekbrains.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CityHolder> {

    private Context mContext;
    private List<String> cityList;
    private List<String> selectedList;
    private OnClickCityListener mListener;

    public interface OnClickCityListener{
        void onClickCity(List<String> cityList);
    }

    public RecyclerViewAdapter(Context context, OnClickCityListener listener, List<String> cityList){
        mContext = context;
        mListener = listener;
        this.cityList = cityList;
        selectedList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CityHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final CityHolder cityHolder, int position) {
        cityHolder.txtCityName.setText(cityList.get(position));
        cityHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String item : selectedList){
                    if(item.equals(cityHolder.txtCityName.getText())){
                        cityHolder.mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.grey));
                        cityHolder.txtCityName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                        selectedList.remove(item);
                        mListener.onClickCity(selectedList);
                        return;
                    }
                }
                cityHolder.mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                cityHolder.txtCityName.setTextColor(mContext.getResources().getColor(R.color.grey));
                selectedList.add(cityHolder.txtCityName.getText().toString());
                mListener.onClickCity(selectedList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class CityHolder extends RecyclerView.ViewHolder{

        private CardView mCardView;
        private TextView txtCityName;

        public CityHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));

            mCardView = itemView.findViewById(R.id.item_card_view);
            txtCityName = itemView.findViewById(R.id.textview_item);
        }
    }
}


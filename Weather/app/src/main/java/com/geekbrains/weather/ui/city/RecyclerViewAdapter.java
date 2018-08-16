package com.geekbrains.weather.ui.city;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.R;
import com.geekbrains.weather.data.PrefsData;
import com.geekbrains.weather.data.PrefsHelper;
import com.geekbrains.weather.model.SelectedCity;
import com.geekbrains.weather.ui.base.BaseActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CityHolder> {

    private Context mContext;
    private List<SelectedCity> cityList;
    private OnClickCityListener mListener;
    private PrefsHelper mPrefsHelper;

    public interface OnClickCityListener{
        void onClickCity(String cityList);
    }

    public RecyclerViewAdapter(Context context, BaseActivity baseActivity, OnClickCityListener listener, List<SelectedCity> cityList){
        mContext = context;
        mListener = listener;
        mPrefsHelper = new PrefsData(baseActivity);
        this.cityList = cityList;

    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CityHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final CityHolder cityHolder, int position) {
        final SelectedCity item = cityList.get(position);
        cityHolder.txtCityName.setText(cityList.get(position).getCity());
        if (!item.isSelected()) {
            cityHolder.mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.grey));
            cityHolder.txtCityName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        }
        else {
            cityHolder.mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            cityHolder.txtCityName.setTextColor(mContext.getResources().getColor(R.color.grey));
        }
        cityHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(item);
            }
        });
    }

    private void setSelected(SelectedCity selectedCity) {

        selectedCity.setSelected(!selectedCity.isSelected());
        cityList.stream().filter(s -> !s.getCity().equals(selectedCity.getCity())).forEach(s -> s.setSelected(false));

        if (selectedCity.isSelected()) {
            mListener.onClickCity(selectedCity.getCity());
            saveInPref(selectedCity.getCity());
        } else {
            mListener.onClickCity(null);
            deleteInPref();
        }


        notifyDataSetChanged();
    }

    private void deleteInPref() {
        mPrefsHelper.deleteSharedPreferences(Constants.CITY);
    }

    private void saveInPref(String city) {
        mPrefsHelper.saveSharedPreferences(Constants.CITY, city);
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


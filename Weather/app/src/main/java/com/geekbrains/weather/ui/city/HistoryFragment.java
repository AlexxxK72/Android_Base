package com.geekbrains.weather.ui.city;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.base.BaseFragment;

public class HistoryFragment extends BaseFragment {

    private String cityName;
    private TextView txtCityName;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab_back;
    private CallBackHF mCallBack;

    public static final String CITY_NAME = "city_name";

    public interface CallBackHF {
        void fab_back_onClick();
    }

    public static HistoryFragment newInstance(String cityName) {

        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = getBaseActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            cityName = getArguments().getString(CITY_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initLayout(view, savedInstanceState);
        return view;
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        txtCityName = view.findViewById(R.id.city_name_history);
        if(cityName != null) txtCityName.setText(cityName);

        mRecyclerView = view.findViewById(R.id.recycler_view_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        RecyclerHistoryAdapter adapter = new RecyclerHistoryAdapter(getActivity(), txtCityName.getText().toString());
        mRecyclerView.setAdapter(adapter);

        fab_back = view.findViewById(R.id.fab_back_history);
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.fab_back_onClick();
            }
        });
    }
}

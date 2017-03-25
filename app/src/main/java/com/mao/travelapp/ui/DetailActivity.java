package com.mao.travelapp.ui;

import android.os.Bundle;

import com.mao.travelapp.R;
import com.mao.travelapp.bean.TravelNote;

/**
 * Created by mao on 2017/3/25.
 */
public class DetailActivity extends BaseActivity {

    private TravelNote item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        item = (TravelNote) getIntent().getSerializableExtra("item");
        if(item == null) {
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        setActionBarCenterText("详情");


    }
}

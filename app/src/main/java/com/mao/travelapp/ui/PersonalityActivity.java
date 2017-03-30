package com.mao.travelapp.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mao.travelapp.R;
import com.mao.travelapp.manager.UserManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mao on 2017/3/28.
 */
public class PersonalityActivity extends BaseActivity {

    private static class Bean {

        private String key;
        private String value;

        private Bean(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private ListView mListView;
    private PersonalityListAdapter mAdapter;
    private List<Bean> mList = new ArrayList<Bean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personality);

        initData();

        setActionBarCenterText("个人信息");
        TextView tv = setActionBarLeftText("");
        tv.setBackgroundResource(R.drawable.app_back_arrow);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new PersonalityListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    //换头像


                }
            }
        });
    }

    private void initData() {
        mList.add(new Bean("头像", UserManager.getInstance().getPicture()));
        mList.add(new Bean("用户名", UserManager.getInstance().getUsername()));
        mList.add(new Bean("手机号", UserManager.getInstance().getPhone()));
    }

    private class PersonalityListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(PersonalityActivity.this).inflate(R.layout.layout_simple_list_item, parent, false);

            TextView keyTv = (TextView) convertView.findViewById(R.id.keyTv);

            Bean item = (Bean) getItem(position);
            keyTv.setText(item.key);
            if(position == 0) {
                //头像
                ImageView valueIv = (ImageView) convertView.findViewById(R.id.valueIv);
                if(!TextUtils.isEmpty(item.value)) {
                    ImageLoader.getInstance().displayImage(item.value, valueIv);
                } else {
                    valueIv.setImageDrawable(getDrawable(R.drawable.user_default_head_circle));
                }
            } else {
                TextView valueTv = (TextView) convertView.findViewById(R.id.valueTv);
                valueTv.setText(item.value);
            }

            return convertView;
        }
    }
}


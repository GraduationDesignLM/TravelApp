package com.mao.travelapp.ui;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mao.travelapp.R;
import com.mao.travelapp.utils.UnitsUtils;


public abstract class BaseActivity extends Activity {

    private LinearLayout mContentViewWrapper;
    private View mActionBarView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        //百度地图初始化
        //SDKInitializer.initialize(getApplicationContext());
        //百度地图初始化

        //ActionBar有问题
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null) {
//            actionBar.setDisplayOptions(0);
//            actionBar.setDisplayShowCustomEnabled(true);
//            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            mActionBarView = LayoutInflater.from(this).inflate(R.layout.app_common_actionbar, null);
//            ActionBar.LayoutParams params = new ActionBar.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            actionBar.setCustomView(mActionBarView, params);
//
//        }
//

        mContentViewWrapper = new LinearLayout(this);
        mContentViewWrapper.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.setContentView(mContentViewWrapper, params);

        //新的ActionBar实现
        mActionBarView = LayoutInflater.from(this).inflate(R.layout.app_common_actionbar, mContentViewWrapper, false);
        ViewGroup.LayoutParams layoutParams = mActionBarView.getLayoutParams();
        if(layoutParams != null) {
            layoutParams.height = UnitsUtils.dp2px(this, 55.0f);
        }
        mContentViewWrapper.addView(mActionBarView);

        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //设置当前Window背景颜色为白色
        getWindow().setBackgroundDrawableResource(R.color.white);

    }

    public TextView setActionBarLeftText(int id) {
        Resources resources = getResources();
        return setActionBarLeftText(resources.getString(id));
    }

    public TextView setActionBarLeftText(String text) {
        if(mActionBarView != null) {
            TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_left_tv);
            if(tv != null) {
                tv.setText(text);
                return tv;
            }
        }
        return null;
    }

    public TextView setActionBarCenterText(int id) {
        Resources resources = getResources();
        return setActionBarCenterText(resources.getString(id));
    }

    public TextView setActionBarCenterText(String text) {
        if(mActionBarView != null) {
            TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_center_tv);
            if(tv != null) {
                tv.setText(text);
                return tv;
            }
        }
        return null;
    }

    public TextView setActionBarRightText(int id) {
        Resources resources = getResources();
        return setActionBarRightText(resources.getString(id));
    }

    public TextView setActionBarRightText(String text) {
        if(mActionBarView != null) {
            TextView tv = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_right_tv);
            if(tv != null) {
                tv.setText(text);
            }
            return tv;
        }
        return null;
    }

    public void setActionBarBackgroundColor(int color) {
        if(mActionBarView != null) {
            mActionBarView.setBackgroundColor(color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, mContentViewWrapper, false);
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(view, params);
    }

    /**
     * 注意params必须为LinearLayout.LayoutParams对象
     *
     * @param view
     * @param params
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentViewWrapper.addView(view, params);
    }
}

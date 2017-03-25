package com.mao.travelapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mao.travelapp.App;
import com.mao.travelapp.R;
import com.mao.travelapp.bean.TravelNote;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.manager.UserManager;
import com.mao.travelapp.sdk.BaseObject;
import com.mao.travelapp.sdk.QueryCallback;
import com.mao.travelapp.utils.UnitsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private MainPageAdapter mAdapter;
    private List<TravelNote> mData = new ArrayList<TravelNote>();

    /** 第一次加载数据当成刷新 */
    private boolean isRefreshing = true;

    private TextView mFlusView;

    //分页
    private long offset = 0;
    private long limit = 20;

    private boolean mNoMoreData = false;

    //发布按钮
    private ImageView mPublishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        //第一次进来自动获取数据
        getData();

        initView();

    }

    private void initView() {
        //标题
        setActionBarCenterText(getString(R.string.app_name));
        //头像
        TextView tv = setActionBarLeftText("");
        ViewGroup.LayoutParams params = tv.getLayoutParams();
        if(params != null) {
            params.height = UnitsUtils.dp2px(this, 40);
            params.width = UnitsUtils.dp2px(this, 40);
        }
        tv.setBackgroundResource(R.drawable.user_default_head_circle);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //刷新按钮
        mFlusView = setActionBarRightText("");
        mFlusView.setBackgroundResource(R.drawable.flush);
        params = mFlusView.getLayoutParams();
        if(params != null) {
            params.height = UnitsUtils.dp2px(this, 40);
            params.width = UnitsUtils.dp2px(this, 40);
        }
        mFlusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flush();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new MainPageAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                int lastChildBottom = lastChildView.getBottom();
                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);

                //滑到底部，加载更多
                if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    getData();
                }
            }

        });


        mPublishBtn = (ImageView) findViewById(R.id.main_add);
        mPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PublishMainActivity.class));
            }
        });
    }

    private class MainPageAdapter extends RecyclerView.Adapter<MainPageViewHolder> {


        @Override
        public MainPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_mainpage_list_item, parent, false);
            return new MainPageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainPageViewHolder holder, int position) {
            TravelNote item = mData.get(position);
            String str = item.getPictureUrls();
            //先清除缓存，设置为默认图片
            holder.iv.setImageDrawable(getDrawable(R.drawable.background_white));
            if(!TextUtils.isEmpty(str)) {
                String[] arr = str.split("##");
                if (arr != null && arr.length > 0) {
                    ImageLoader.getInstance().displayImage(arr[0], holder.iv);
                }
            }
            holder.tv.setText(item.getText());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private static class MainPageViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tv;

        public MainPageViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }










    private void flush() {
        //如果正在刷新则return
        if(isRefreshing) {
            return;
        }
        isRefreshing = true;
        //开启旋转动画，   旋转不了？？后面再看吧
        rotateAnim(mFlusView);
        //开始获取数据
        getData();
    }

    //获取数据
    private void getData() {
        Map<String, String> where = new HashMap<String, String>();
        offset = mData.size();
        where.put("offset", offset + "");
        where.put("limit", limit + "");
        BaseObject.query(where, TravelNote.class, new QueryCallback<TravelNote>(){

            @Override
            public void onSuccess(List<TravelNote> list) {
                handleForGetDataSuccess(list);
            }

            @Override
            public void onFail(String error) {
                handleForGetDataFail();
            }
        });
    }

    private void handleForGetDataSuccess(List<TravelNote> list) {
        //停止旋转动画
        Animation animation = mFlusView.getAnimation();
        if(animation != null) {
            animation.cancel();
        }
        mFlusView.clearAnimation();
        //更新数据
        if(list.size() > 0) {
            mNoMoreData = false;
        }
        if(isRefreshing) {
            mData.clear();
        } else {
            if(list.size() > 0) {
                Toast.makeText(this, "加载" + list.size() + "条数据", Toast.LENGTH_SHORT).show();
            } else if(!mNoMoreData) {
                mNoMoreData = true;
                Toast.makeText(this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
            }
        }
        if(list != null && list.size() > 0) {
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
        isRefreshing = false;
    }

    private void handleForGetDataFail() {
        //停止旋转动画
        Animation animation = mFlusView.getAnimation();
        if(animation != null) {
            animation.cancel();
        }
        mFlusView.clearAnimation();
        //清除数据
        mData.clear();
        isRefreshing = false;
        mAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
    }

    public void rotateAnim(View view) {
        if(view == null) {
            return;
        }
        Animation anim =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(500); // 设置动画时间
        view.startAnimation(anim);
    }

    private void initData() {
        if(false && App.DEBUG) {
            //使用模拟数据
            mData.clear();
            mData.add(new TravelNote(
                    "测试数据",
                    "http://www.33lc.com/article/UploadPic/2012-8/201282413335761587.jpg",
                    "广州市",
                    1,
                    "3-24 14:17",
                    23,
                    115));
            mData.add(new TravelNote(
                    "测试数据",
                    "http://www.33lc.com/article/UploadPic/2012-8/201282413335761587.jpg",
                    "广州市",
                    1,
                    "3-24 14:17",
                    23,
                    115));
            mData.add(new TravelNote(
                    "测试数据",
                    "http://www.33lc.com/article/UploadPic/2012-8/201282413335761587.jpg",
                    "广州市",
                    1,
                    "3-24 14:17",
                    23,
                    115));

            for(int i = 0; i < 100; i++) {
                mData.add(new TravelNote(
                    "测试数据",
                    "http://www.33lc.com/article/UploadPic/2012-8/201282413335761587.jpg",
                    "广州市",
                    1,
                    "3-24 14:17",
                    23,
                    115));
            }
        }
    }
}
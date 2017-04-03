package com.mao.travelapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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
import com.mao.travelapp.manager.CacheCenter;
import com.mao.travelapp.manager.UserManager;
import com.mao.travelapp.sdk.BaseObject;
import com.mao.travelapp.sdk.CommonDBCallback;
import com.mao.travelapp.sdk.QueryCallback;
import com.mao.travelapp.utils.BitmapUtils;
import com.mao.travelapp.utils.MethodCompat;
import com.mao.travelapp.utils.TimeUtils;
import com.mao.travelapp.utils.UnitsUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
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

        //第一次进来自动获取数据
        getData();

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        flushDataIfNeed();
    }

    private void initView() {
        //标题
        setActionBarCenterText(getString(R.string.app_name));
        //头像
        TextView tv = setActionBarLeftText("");
        ViewGroup.LayoutParams params = tv.getLayoutParams();
        if(params != null) {
            params.height = UnitsUtils.dp2px(this, 30);
            params.width = UnitsUtils.dp2px(this, 30);
        }
        tv.setBackgroundResource(R.drawable.user_default_head_circle);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PersonalityActivity.class));
            }
        });
        //刷新按钮
        mFlusView = setActionBarRightText("");
        mFlusView.setBackgroundResource(R.drawable.flush);
        params = mFlusView.getLayoutParams();
        if(params != null) {
            params.height = UnitsUtils.dp2px(this, 30);
            params.width = UnitsUtils.dp2px(this, 30);
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
                if(lastChildView == null) {
                    return;
                }
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
        public void onBindViewHolder(final MainPageViewHolder holder, int position) {
            final TravelNote item = mData.get(position);
            String str = item.getPictureUrls();
            //先清除缓存，设置为默认图片
            holder.iv.setImageDrawable(MethodCompat.getDrawable(MainActivity.this, R.drawable.background_white));
            if(!TextUtils.isEmpty(str)) {
                String[] arr = str.split("##");
                if (arr != null && arr.length > 0) {
                    DisplayImageOptions opt = new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .build();
                    ImageLoader.getInstance().displayImage(arr[0], holder.iv, opt);
                }
            }
            holder.tv.setText(item.getText());
            holder.date.setText(item.getPublish_time());
            holder.location.setText(item.getLocation());

            Map<String, String> where = new HashMap<String, String>();
            where.put("id", item.getUserId() + "");
            BaseObject.query(where, User.class, new QueryCallback<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if(list != null && list.size() > 0) {
                        User user = list.get(0);
                        ImageLoader.getInstance().displayImage(user.getPicture(), holder.headpicture);
                        holder.username.setText(user.getUsername());
                    }
                }

                @Override
                public void onFail(String error) {

                }
            });

            //设置点击事件
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("item", item);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private static class MainPageViewHolder extends RecyclerView.ViewHolder {

        private View item;
        private ImageView iv;
        private TextView tv;
        private ImageView headpicture;
        private TextView username;
        private TextView date;
        private TextView location;

        public MainPageViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.tv);
            headpicture = (ImageView) itemView.findViewById(R.id.headpicture);
            username = (TextView) itemView.findViewById(R.id.username);
            date = (TextView) itemView.findViewById(R.id.date);
            location = (TextView) itemView.findViewById(R.id.location);
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
        if (view == null) {
            return;
        }
        Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(500); // 设置动画时间
        view.startAnimation(anim);
    }

    //刷新该刷新的东西，如头像等
    private void flushDataIfNeed() {
        //先看缓存
        if(CacheCenter.sHeadPictureCache != null) {
            MethodCompat.setBackground(setActionBarLeftText(""), new BitmapDrawable(getResources(), CacheCenter.sHeadPictureCache));
        }
        Map<String, String> where = new HashMap<String, String>();
        where.put("username", UserManager.getInstance().getUsername());
        BaseObject.query(where, User.class, new QueryCallback<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list != null && list.size() > 0) {
                    User user = list.get(0);
                    String url = user.getPicture();
                    if(!TextUtils.isEmpty(url) && !url.equals(UserManager.getInstance().getPicture())) {
                        UserManager.getInstance().setPicture(url);
                        loadHeadPicture(setActionBarLeftText(""), url);
                    }
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private void loadHeadPicture(final TextView tv, String url) {
        if(tv == null || TextUtils.isEmpty(url)) {
            return;
        }
        ImageLoader.getInstance().displayImage(url, new ImageView(MainActivity.this), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(bitmap != null) {
                    CacheCenter.sHeadPictureCache = bitmap;
                    bitmap = BitmapUtils.createCircleBitmap(bitmap);
                    MethodCompat.setBackground(setActionBarCenterText(""), new BitmapDrawable(getResources(), bitmap));
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }


}
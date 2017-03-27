package com.mao.travelapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mao.travelapp.R;
import com.mao.travelapp.bean.Comment;
import com.mao.travelapp.bean.TravelNote;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.manager.UserManager;
import com.mao.travelapp.sdk.BaseObject;
import com.mao.travelapp.sdk.CommonDBCallback;
import com.mao.travelapp.sdk.QueryCallback;
import com.mao.travelapp.utils.StringUtils;
import com.mao.travelapp.utils.UnitsUtils;
import com.mao.travelapp.view.WrapContentViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mao on 2017/3/25.
 */
public class DetailActivity extends BaseActivity {

    private TravelNote item;

    private WrapContentViewPager vp;
    private PagerAdapter mViewPagerAdapter;
    private String[] mPicUrls = new String[0];

    private TextView mDescriptionTv;
    private ImageView mHeadPictureIv;
    private TextView mUsernameTv;
    private TextView mLocationTv;
    private ListView mListView;
    private BaseAdapter mAdapter;
    private List<Comment> mCommentList = new ArrayList<Comment>();

    //发表评论
    private EditText mCommentEt;
    private Button mCommentSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        item = (TravelNote) getIntent().getSerializableExtra("item");
        if(item == null) {
            finish();
            return;
        }
        String urls = item.getPictureUrls();
        if(!TextUtils.isEmpty(urls)) {
            mPicUrls = urls.split("##");
        }

        initView();

        getData();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mCommentEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initView() {
        setActionBarCenterText("详情");

        vp = (WrapContentViewPager) findViewById(R.id.vp);
        //强制设置ViewPager高度
        vp.requestForceHeight(UnitsUtils.dp2px(this, 300));
        mViewPagerAdapter = new ViewPagerAdapter();
        vp.setAdapter(mViewPagerAdapter);

        mDescriptionTv = (TextView) findViewById(R.id.desciption);
        mHeadPictureIv = (ImageView) findViewById(R.id.headpicture);
        mUsernameTv = (TextView) findViewById(R.id.username);
        mLocationTv = (TextView) findViewById(R.id.location);

        mDescriptionTv.setText(item.getText());
        mLocationTv.setText(item.getLocation());
        int userId = item.getUserId();
        Map<String, String> where = new HashMap<String, String>();
        where.put("id", userId + "");
        BaseObject.query(where, User.class, new QueryCallback<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list != null && list.size() > 0) {
                    User user = list.get(0);
                    ImageLoader.getInstance().displayImage(user.getPicture(), mHeadPictureIv);
                    mUsernameTv.setText(user.getUsername());
                }
            }

            @Override
            public void onFail(String error) {

            }
        });

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new CommentListAdapter();
        mListView.setAdapter(mAdapter);

        mCommentEt = (EditText) findViewById(R.id.comment_et);
        mCommentSend = (Button) findViewById(R.id.comment_send);
        mCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mCommentEt.getText().toString();
                if(TextUtils.isEmpty(text)) {
                    Toast.makeText(DetailActivity.this, "请先输入评论内容", Toast.LENGTH_SHORT).show();
                } else {
                    final Comment comment = new Comment();
                    comment.setUsername(UserManager.getInstance().getUsername());
                    comment.setDate(com.mao.imageloader.utils.DateUtils.getDate());
                    comment.setPubId(item.getId());
                    comment.setText(text);
                    comment.save(new CommonDBCallback() {
                        @Override
                        public void onSuccess(int affectedRowCount) {
                            if(affectedRowCount == 1) {
                                Toast.makeText(DetailActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                                mCommentList.add(comment);
                                mAdapter.notifyDataSetChanged();
                                mCommentEt.setText("");
                                //隐藏软键盘
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mCommentEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            } else {
                                Toast.makeText(DetailActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            Toast.makeText(DetailActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    private void getData() {
        Map<String, String> where = new HashMap<String, String>();
        where.put("pubId", "" + item.getId());
        BaseObject.query(where, Comment.class, new QueryCallback<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if(list != null && list.size() > 0) {
                    mCommentList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });

    }

    private class ViewPagerAdapter extends PagerAdapter {

        private ImageView[] views = new ImageView[mPicUrls.length];

        private ViewPagerAdapter() {
            for(int i = 0; i < views.length; i++) {
                views[i] = new ImageView(DetailActivity.this);
                views[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                ViewGroup.LayoutParams params = new ViewPager.LayoutParams();
                views[i].setLayoutParams(params);
                ImageLoader.getInstance().displayImage(mPicUrls[i], views[i]);
            }
        }


        @Override
        public int getCount() {
            return mPicUrls.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View view, int position, Object object) {
            ((ViewPager) view).removeView(views[position]);
        }

        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(views[position], 0);

            return views[position];
        }
    }

    private class CommentListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mCommentList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCommentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                convertView = LayoutInflater.from(DetailActivity.this).inflate(R.layout.layout_comment_list_item, parent, false);
                holder = new ViewHolder();
                holder.headpicture = (ImageView) convertView.findViewById(R.id.headpicture);
                holder.username = (TextView) convertView.findViewById(R.id.username);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Comment comment = (Comment) getItem(position);

            //头像先不管
            holder.username.setText(comment.getUsername());
            holder.date.setText(comment.getDate());
            holder.content.setText(comment.getText());

            return convertView;
        }

        private class ViewHolder {
            private ImageView headpicture;
            private TextView username;
            private TextView date;
            private TextView content;
        }
    }
}

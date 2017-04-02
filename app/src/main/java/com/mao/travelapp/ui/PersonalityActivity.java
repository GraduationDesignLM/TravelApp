package com.mao.travelapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.mao.travelapp.R;
import com.mao.travelapp.bean.User;
import com.mao.travelapp.manager.ActivityRequestResultCode;
import com.mao.travelapp.manager.DiskCacheManager;
import com.mao.travelapp.manager.UserManager;
import com.mao.travelapp.sdk.CommonDBCallback;
import com.mao.travelapp.sdk.FileHelper;
import com.mao.travelapp.sdk.UploadFileCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

    private Uri mHeadPictureUri;

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
                    handleForClickHead();
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
            ImageView valueIv = (ImageView) convertView.findViewById(R.id.valueIv);
            TextView valueTv = (TextView) convertView.findViewById(R.id.valueTv);
            if(position == 0) {
                //头像
                valueIv.setVisibility(View.VISIBLE);
                valueTv.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(item.value)) {
                    ImageLoader.getInstance().displayImage(item.value, valueIv);
                } else {
                    valueIv.setImageDrawable(getDrawable(R.drawable.user_default_head_circle));
                }
            } else {
                valueIv.setVisibility(View.GONE);
                valueTv.setVisibility(View.VISIBLE);
                valueTv.setText(item.value);
            }

            return convertView;
        }
    }

    private void handleForClickHead() {

        final User user = UserManager.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] options = new String[]{"拍照", "从相册中选择"};
        builder.setTitle("请选择");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filePath = DiskCacheManager.getInstance().getCacheRootPath(getApplicationContext());
                String fileName = user.getUsername();
                switch(which)
                {
                    // 调用系统相机拍照更换头像
                    case 0:
                        mHeadPictureUri = PicturesHelper.takePhotoByCamera(PersonalityActivity.this, filePath, fileName, ActivityRequestResultCode.TAKE_PHOTO_ACTIVITY_REQUEST_CODE);
                        break;
                    // 从相册中选择更换头像
                    case 1:
                        PicturesHelper.takePhotoByGallery(PersonalityActivity.this, filePath, fileName, ActivityRequestResultCode.GALLERY_ACTIVITY_REQUEST_CODE);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == -1) {
            //拍照
            if (requestCode == ActivityRequestResultCode.TAKE_PHOTO_ACTIVITY_REQUEST_CODE) {
                PicturesHelper.crop(this, mHeadPictureUri, ActivityRequestResultCode.CROP_ACTIVITY_REQUEST_CODE);
                //从相册选择
            } else if(requestCode == ActivityRequestResultCode.GALLERY_ACTIVITY_REQUEST_CODE && data != null) {
                PicturesHelper.crop(this, data.getData(), ActivityRequestResultCode.CROP_ACTIVITY_REQUEST_CODE);
                //裁切
            } else if(requestCode == ActivityRequestResultCode.CROP_ACTIVITY_REQUEST_CODE && data != null) {
                Bitmap bm = data.getParcelableExtra("data");
                updateHeadPicture(bm);
            }
        }
    }

    private void updateHeadPicture(Bitmap bm) {
        if(bm == null) {
            Toast.makeText(this, "更换头像失败", Toast.LENGTH_SHORT).show();
            return;
        }
        OutputStream os = null;
        String tempPath = null;
        try {
            tempPath = DiskCacheManager.getInstance().getMainDirectory();
            File f = new File(tempPath);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            tempPath += "headpic.jpg";
            f = new File(tempPath);
            if(f.isFile()) {
                f.delete();
            }
            f.createNewFile();
            os = new FileOutputStream(f);
            if(!bm.compress(Bitmap.CompressFormat.JPEG, 100, os)) {
                Toast.makeText(this, "更换头像失败", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, "更换头像失败", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> paths = new ArrayList<String>();
        paths.add(tempPath);
        FileHelper.upload(paths, new UploadFileCallback() {
            @Override
            public void onSuccess(final List<String> urls) {
                if(urls != null && urls.size() > 0 && !TextUtils.isEmpty(urls.get(0))) {
                    User user = UserManager.getInstance();
                    user.setPicture(urls.get(0));
                    user.update(new CommonDBCallback() {
                        @Override
                        public void onSuccess(int affectedRowCount) {
                            if(affectedRowCount == 1) {
                                mList.remove(0);
                                UserManager.getInstance().setPicture(urls.get(0));
                                mList.add(0, new Bean("头像", UserManager.getInstance().getPicture()));
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(PersonalityActivity.this, "更换头像失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            Toast.makeText(PersonalityActivity.this, "更换头像失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(PersonalityActivity.this, "更换头像失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


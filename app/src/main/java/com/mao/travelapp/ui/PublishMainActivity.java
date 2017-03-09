package com.mao.travelapp.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.baidu.location.BDLocation;
import com.mao.travelapp.R;
import com.mao.travelapp.bean.TravelNote;
import com.mao.travelapp.sdk.CommonDBCallback;
import com.mao.travelapp.sdk.FileHelper;
import com.mao.travelapp.sdk.UploadFileCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PublishMainActivity extends LocationBaseActivity implements EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate, View.OnClickListener {

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    MaterialEditText et_publish_main_content;
    TextView tv_publish_main_location;
    TextView tv_publish_main_time;
    ImageView iv_publish_main_time;
    BGASortableNinePhotoLayout snpl_publish_main_picture;
    BottomDialog dialog;
    private static final String TAG = "PublishMainActivity";

    WeakHandler weakHandler;
    ImageView iv_publish_main_location_icon;
    ProgressDialog progressDialog;
    StringBuilder pictureUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_main_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }


    @Override
    void receiveLocation(BDLocation location) {
        Log.v("address", location.getAddrStr());
        tv_publish_main_location.setText(location.getAddrStr());
    }

    private void initView() {
        pictureUrls = new StringBuilder();
        weakHandler = new WeakHandler();
        iv_publish_main_location_icon = (ImageView) findViewById(R.id.iv_publish_main_location_icon);
        et_publish_main_content = (MaterialEditText) findViewById(R.id.et_publish_main_content);
        tv_publish_main_location = (TextView) findViewById(R.id.tv_publish_main_location);
        tv_publish_main_time = (TextView) findViewById(R.id.tv_publish_main_time);
        iv_publish_main_time = (ImageView) findViewById(R.id.iv_publish_main_time);
        snpl_publish_main_picture = (BGASortableNinePhotoLayout) findViewById(R.id.snpl_publish_main_picture);
        snpl_publish_main_picture.setDelegate(this);
        initEvent();
    }

    private void initEvent() {
        setActionBarRightText("发布").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTravelNote();
            }
        });

        iv_publish_main_time.setOnClickListener(this);
        iv_publish_main_location_icon.setOnClickListener(this);
    }

    private boolean setTravelNote() {
        if (isNoteComplete()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("上传文件");
            progressDialog.setTitle("wait......");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            TravelNote travelNote = new TravelNote();
            travelNote.setText(et_publish_main_content.getText().toString());
            travelNote.setLocation(tv_publish_main_location.getText().toString());
            travelNote.setPublish_time(tv_publish_main_time.getText().toString());
            responseUploadFile(snpl_publish_main_picture.getData(), travelNote);
        }
        return false;
    }

    private boolean isNoteComplete() {
        int NO_IMAGE_NO_TEXT = 0;
        int NO_IMAGE_HAS_TEXT = 1;
        int NO_TEXT_HAS_IMAGE = 2;
        int HAS_TEXT_HAS_IMAGE = 3;
        int INITIAL_STATE = NO_IMAGE_NO_TEXT;
        if (!tv_publish_main_location.getText().toString().equals("")) {
            if (!tv_publish_main_time.getText().toString().equals("")) {
                if (!et_publish_main_content.getText().toString().equals("")) {
                    if (snpl_publish_main_picture.getData().size() != 0) {
                        INITIAL_STATE = HAS_TEXT_HAS_IMAGE;
                    } else {
                        INITIAL_STATE = NO_IMAGE_HAS_TEXT;
                    }
                } else {
                    if (snpl_publish_main_picture.getData().size() != 0) {
                        INITIAL_STATE = NO_TEXT_HAS_IMAGE;
                    } else {
                        INITIAL_STATE = NO_IMAGE_NO_TEXT;
                    }
                }
                if (INITIAL_STATE != 0) {
                    return true;
                } else {
                    Toast.makeText(this, "内容或者图片还没有晒哦！", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, "选个时间呗！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "位置还没填哦！", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, snpl_publish_main_picture.getMaxItemCount() - snpl_publish_main_picture.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        snpl_publish_main_picture.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, snpl_publish_main_picture.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            snpl_publish_main_picture.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
            for (String s : snpl_publish_main_picture.getData()) {
                Log.v(TAG, s);
            }
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            snpl_publish_main_picture.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_publish_main_time:
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                final int year, month, day;
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String time = "";
                        time += year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
                        tv_publish_main_time.setText(time);
                    }
                }, year, month, day).show();
                break;
            case R.id.iv_publish_main_location_icon:
                dialog = new BottomDialog(PublishMainActivity.this);
                dialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
                    @Override
                    public void onAddressSelected(Province province, City city, County county, Street street) {
                        String location = "";
                        if (province != null) {
                            location += province.name;
                        }
                        if (city != null) {
                            location += city.name;
                        }
                        if (county != null) {
                            location += county.name;
                        }
                        if (street != null) {
                            location += street.name;
                        }
                        final String string = location;
                        weakHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                tv_publish_main_location.setText(string);
                            }
                        });
                    }
                });
                dialog.show();
                break;
//            case R.id.iv_publish_main_location_icon:
//                requestLocation();
//                break;
            default:
                break;
        }
    }

    private void responseUploadFile(final List<String> urls, final TravelNote travelNote) {
        FileHelper.upload(urls, new UploadFileCallback() {
            @Override
            public void onSuccess(List<String> urls) {
                for (String s : urls) {
                    pictureUrls.append(s);
                    pictureUrls.append("&");
                }
                travelNote.setPictureUrl(pictureUrls.toString());
                travelNote.save(new CommonDBCallback() {
                    @Override
                    public void onSuccess(int affectedRowCount) {
                        Log.v(TAG, "success");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFail(String error) {
                        Log.v(TAG, "failed");
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFail(String error) {

            }
        });

    }
}

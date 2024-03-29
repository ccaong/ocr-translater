package com.example.ocrandtranslate.common;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.base.BaseActivity;
import com.example.ocrandtranslate.util.ImageSelectUtil;
import com.example.ocrandtranslate.util.StartActivityUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * @author devel
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    private PromptDialog promptDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        promptDialog = new PromptDialog(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        SharedPreferences userSettings = getSharedPreferences("OCR_SETTING", 0);
        String path = userSettings.getString("header_path", "");
        String name = userSettings.getString("Name", "");
        String Signature = userSettings.getString("Signature", "");
        if (!path.equals("")) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.default_icon)
                    .error(R.mipmap.default_icon).fallback(R.mipmap.default_icon)
                    .transform(new CircleCrop());
            Glide.with(this)
                    .load(path)
                    .apply(options)
                    .into(ivHeader);
        }
        if (!name.equals("")) {
            tvName.setText(name);
        }
        if (!Signature.equals("")) {
            tvSignature.setText(Signature);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.iv_header, R.id.iv_update, R.id.tv_mine, R.id.tv_setting, R.id.tv_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
//                requestPermission();
                break;
            case R.id.iv_update:
                promptDialog.showLoading("正在同步");
                update();
                break;
            case R.id.tv_mine:
                StartActivityUtil.startActivity(SettingActivity.this, MineActivity.class);
                break;
            case R.id.tv_setting:
                StartActivityUtil.startActivity(SettingActivity.this, MineActivity.class);
                break;
            case R.id.tv_feedback:
                StartActivityUtil.startActivity(SettingActivity.this, FeedBackActivity.class);
                break;
            default:
                break;
        }
    }


    public void requestPermission() {
        RxPermissions permissions = new RxPermissions(SettingActivity.this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {

                if (aBoolean) {
                    ImageSelectUtil.selectMultipleImage(SettingActivity.this, 1);
                } else {
                    Toast.makeText(SettingActivity.this, "请先授予权限，否者该功能无法使用！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageSelectUtil.CHOOSE_MULTIPLE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    RequestOptions options = new RequestOptions();
                    options.placeholder(R.mipmap.default_icon)
                            .error(R.mipmap.default_icon).fallback(R.mipmap.default_icon)
                            .transform(new CircleCrop());
                    Glide.with(this)
                            .load(selectList.get(0).getCompressPath())
                            .apply(options)
                            .into(ivHeader);
                    saveImageHeader(selectList.get(0).getCompressPath());
                    break;
                default:
                    break;
            }
        }
    }

    private void saveImageHeader(String path) {
        SharedPreferences userSettings = getSharedPreferences("OCR_SETTING", 0);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString("header_path", path);
        editor.commit();
    }

    private void update() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //将要延迟执行的操作
                promptDialog.showLoading("同步完成");
                promptDialog.dismiss();
            }
        }, 1500);
    }

}

package com.example.ocrandtranslate.common;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.base.BaseActivity;
import com.example.ocrandtranslate.util.ImageSelectUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MineActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_signature)
    EditText etSignature;
    @BindView(R.id.iv_header)
    ImageView ivHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
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

    private void initView() {
        SharedPreferences userSettings = getSharedPreferences("OCR_SETTING", 0);
        String path = userSettings.getString("header_path", "");
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
    }

    @OnClick({R.id.iv_header, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                requestPermission();
                break;
            case R.id.btn_save:
                saveName();
                break;
        }
    }


    public void requestPermission() {
        RxPermissions permissions = new RxPermissions(MineActivity.this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {

                if (aBoolean) {
                    ImageSelectUtil.selectMultipleImage(MineActivity.this, 1);
                } else {
                    Toast.makeText(MineActivity.this, "请先授予权限，否者该功能无法使用！", Toast.LENGTH_SHORT).show();
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

    private void saveName() {
        SharedPreferences userSettings = getSharedPreferences("OCR_SETTING", 0);
        SharedPreferences.Editor editor = userSettings.edit();

        if (!"".equals(etName.getText().toString().trim())) {
            editor.putString("Name", etName.getText().toString().trim());

        }
        if (!"".equals(etSignature.getText().toString().trim())) {
            editor.putString("Signature", etSignature.getText().toString().trim());
        }

        editor.commit();

        Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
        finish();
    }
}

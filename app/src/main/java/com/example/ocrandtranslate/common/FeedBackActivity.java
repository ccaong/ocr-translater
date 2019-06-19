package com.example.ocrandtranslate.common;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * @author devel
 */
public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PromptDialog promptDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        promptDialog = new PromptDialog(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @OnClick(R.id.btn_feedback)
    public void onViewClicked() {
        promptDialog.showLoading("正在提交");
        update();
    }

    private void update() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //将要延迟执行的操作
                promptDialog.showLoading("提交成功");
                promptDialog.dismiss();
                finish();
            }
        }, 1500);
    }

}

package com.example.ocrandtranslate.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.adapter.FragmentViewPagerAdapter;
import com.example.ocrandtranslate.base.BaseActivity;
import com.example.ocrandtranslate.common.fragment.HistoryFragment;
import com.example.ocrandtranslate.common.fragment.MainFragment;
import com.example.ocrandtranslate.util.StartActivityUtil;
import com.example.ocrandtranslate.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author devel
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;

    FragmentViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();

        mFragmentList.add(new MainFragment());
        mFragmentList.add(new HistoryFragment());
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setScanScroll(false);
    }


    @OnClick({R.id.bt_more, R.id.bt_disc, R.id.bt_hist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_more:
                StartActivityUtil.startActivity(MainActivity.this, SettingActivity.class);
                break;
            case R.id.bt_disc:
                viewPager.setCurrentItem(0);
                break;
            case R.id.bt_hist:
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    /**
     * 记录用户首次点击返回键的时间
     */
    private long firstTime = 0;

    /**
     * 第一种解决办法 通过监听keyUp
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}

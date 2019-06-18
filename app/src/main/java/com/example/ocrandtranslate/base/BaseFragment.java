package com.example.ocrandtranslate.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author devel
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    private Unbinder mUnbinder;

    protected Activity activity;
    protected Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = getActivity();
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutResId() != 0) {
            mRootView = inflater.inflate(getLayoutResId(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
            initView(savedInstanceState);
//            initView();
        } else {
            mRootView = initView(savedInstanceState);
        }
        initData();

        return mRootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //butterknife解绑
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
        }
    }


    public abstract int getLayoutResId();

    protected abstract View initView(Bundle savedInstanceState);

    //    public abstract void initView();
    public abstract void initData();


}

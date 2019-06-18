package com.example.ocrandtranslate.common.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.adapter.HistoryAdapter;
import com.example.ocrandtranslate.base.BaseFragment;
import com.example.ocrandtranslate.ocr.HistoryResponse;
import com.example.ocrandtranslate.util.SqlUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author devel
 */
public class HistoryFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<HistoryResponse> list;
    private HistoryAdapter adapter;

    private AlertDialog.Builder alertDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_history;
    }

    @Override
    protected View initView(Bundle savedInstanceState) {
        initRecycleView();
        alertDialog = new AlertDialog.Builder(getActivity());
        return null;
    }

    @Override
    public void initData() {
        getList();

    }

    public void getList() {
        list.clear();
        list.addAll(SqlUtil.queryAll());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list = new ArrayList<>();
        adapter = new HistoryAdapter(context, list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new HistoryAdapter.onItemClickListener() {
            @Override
            public void onClick(int position, HistoryResponse bean) {
                alertText("识别结果", bean.getText());
            }
        });
    }

    private void alertText(final String title, final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

}

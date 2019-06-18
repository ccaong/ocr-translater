package com.example.ocrandtranslate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.ocr.HistoryResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author devel
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryResponse> list;
    private Context context;
    private onItemClickListener onItemClickListener;

    public HistoryAdapter(Context context, List<HistoryResponse> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_history, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final HistoryResponse response = list.get(0);
        if (response != null) {
            Glide.with(context).load(response.getImagePath()).into(holder.ivTitle);
            holder.textView.setText(response.getText());

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(position, response);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_title)
        ImageView ivTitle;
        @BindView(R.id.tv_text)
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface onItemClickListener {
        void onClick(int position, HistoryResponse bean);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

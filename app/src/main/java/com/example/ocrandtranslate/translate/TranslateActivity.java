package com.example.ocrandtranslate.translate;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.base.BaseActivity;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author devel
 */
public class TranslateActivity extends BaseActivity {

    @BindView(R.id.edit_input)
    EditText editInput;
    @BindView(R.id.tv_result)
    TextView tvResult;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_translate)
    public void onViewClicked() {
        String input = editInput.getText().toString().trim();
        if (input.equals("")) {
            Toast.makeText(this, "请输入要翻译的内容", Toast.LENGTH_SHORT).show();
        } else {
            translate(input);
        }

    }

    private void translate(final String text) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String resultJson = new TransApi().getTransResult(text, "auto", "en");
                //拿到结果，对结果进行解析。
                Gson gson = new Gson();
                TranslateResult translateResult = gson.fromJson(resultJson, TranslateResult.class);
                final List<TranslateResult.TransResultBean> trans_result = translateResult.getTrans_result();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String dst = "";
                        for (TranslateResult.TransResultBean s : trans_result
                                ) {
                            dst = dst + "\n" + s.getDst();
                        }

                        tvResult.setText(dst);
                        tvResult.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).start();
    }
}

package com.example.ocrandtranslate.ocr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;
import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.base.BaseActivity;
import com.example.ocrandtranslate.util.FileUtil;
import com.example.ocrandtranslate.util.HttpUtil;
import com.google.gson.Gson;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.ocrandtranslate.util.AuthService.getAuth;

/**
 * @author devel
 */
public class ShowImageActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FileUtil.decoderBase64File();
            }
        });

        Intent intent = getIntent();
        String path = intent.getStringExtra("PATH");
        contrastEnhance(path);
    }

    public void contrastEnhance(final String path) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 请求url
                String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/contrast_enhance";
                try {
                    // 本地文件路径
                    byte[] imgData = Util.readFileByBytes(path);
                    String imgStr = Base64Util.encode(imgData);
                    String imgParam = URLEncoder.encode(imgStr, "UTF-8");

                    String param = "image=" + imgParam;
                    String result = HttpUtil.post(url, getAuth(), param);

                    Gson gson = new Gson();
                    ImageSharpeningResponse imageSharpeningResponse = gson.fromJson(result, ImageSharpeningResponse.class);
                    if (imageSharpeningResponse != null) {
                        Bitmap bitmap = FileUtil.stringToBitmap(imageSharpeningResponse.getImage());
                        image.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }


}

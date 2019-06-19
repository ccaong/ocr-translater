package com.example.ocrandtranslate.sharpen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;
import com.bumptech.glide.Glide;
import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.base.BaseActivity;
import com.example.ocrandtranslate.ocr.ImageSharpeningResponse;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyImageProcessor.saveImageToGallery(ShowImageActivity.this, MyImageProcessor.drawableToBitmap(image.getDrawable()));
            }
        });

        Intent intent = getIntent();
        String path = intent.getStringExtra("PATH");
        Bitmap bitmap = BitmapFactory.decodeFile(path, getBitmapOption(1));
        image.setImageBitmap( bitmap2Gray(bitmap));

//        contrastEnhance(path);
//        sharpenPicture(path);
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

    private void sharpenPicture(String path) {

        Glide.with(this)
                .load(path)
                .into(image);

        Bitmap bitmap = BitmapFactory.decodeFile(path, getBitmapOption(1));
        Bitmap sharpenBitmap = MyImageProcessor.sharpenPicture(bitmap);

        image.setImageBitmap(sharpenBitmap);
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    /**
     * 图片灰度化处理
     *
     * @param bmSrc
     */
    public Bitmap bitmap2Gray(Bitmap bmSrc) {

        // 得到图片的长和宽
        int width = bmSrc.getWidth();
        int height = bmSrc.getHeight();
        // 创建目标灰度图像
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 创建画布
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);
        return bmpGray;

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

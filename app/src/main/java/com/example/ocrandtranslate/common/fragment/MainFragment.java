package com.example.ocrandtranslate.common.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.example.ocrandtranslate.R;
import com.example.ocrandtranslate.base.BaseFragment;
import com.example.ocrandtranslate.common.Common;
import com.example.ocrandtranslate.ocr.HistoryResponse;
import com.example.ocrandtranslate.ocr.OcrResponse;
import com.example.ocrandtranslate.ocr.RecognizeService;
import com.example.ocrandtranslate.ocr.ShowImageActivity;
import com.example.ocrandtranslate.translate.TranslateActivity;
import com.example.ocrandtranslate.util.FileUtil;
import com.example.ocrandtranslate.util.ImageSelectUtil;
import com.example.ocrandtranslate.util.SqlUtil;
import com.example.ocrandtranslate.util.StartActivityUtil;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

import static android.app.Activity.RESULT_OK;


/**
 * @author devel
 */
public class MainFragment extends BaseFragment {

    List<LocalMedia> selectList;
    private boolean hasGotToken = false;
    private AlertDialog.Builder alertDialog;

    private String token;
    private HistoryResponse historyResponse;
    private PromptDialog promptDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    protected View initView(Bundle savedInstanceState) {
        alertDialog = new AlertDialog.Builder(getActivity());
        promptDialog = new PromptDialog(getActivity());
        return null;
    }

    @Override
    public void initData() {
        selectList = new ArrayList<>();
        initAccessTokenWithAkSk();
    }

    @OnClick({R.id.bt_takep, R.id.tv_takep, R.id.bt_readp, R.id.tv_readp, R.id.bt_tran, R.id.tv_tran, R.id.bt_hjl, R.id.tv_hjl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_takep:
            case R.id.tv_takep:
                requestPermission(0, Common.REQUEST_CODE.PICTURE_RECOGNITION);
                break;
            case R.id.bt_readp:
            case R.id.tv_readp:
                requestPermission(1, Common.REQUEST_CODE.PICTURE_RECOGNITION);
                break;
            case R.id.bt_tran:
            case R.id.tv_tran:
                StartActivityUtil.startActivity(getActivity(), TranslateActivity.class);
                break;
            case R.id.bt_hjl:
            case R.id.tv_hjl:
                requestPermission(2, Common.REQUEST_CODE.PICTURE_SHARPENING);
                break;
            default:
                break;
        }
    }

    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(getActivity()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
//                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getActivity(), "2SVB9k03MTdcsGmXZS5C3c8b", "9o1XDVSK4D1DHtBGvvVRlgEkaqinofPL");
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getActivity(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }


    /**
     * 拍照
     *
     * @param code
     */
    public void requestPermission(final int flag, final int code) {
        RxPermissions permissions = new RxPermissions(getActivity());
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                switch (flag) {
                    case 0:
                        if (checkTokenStatus()) {
                            ImageSelectUtil.selectImageOfCamera(MainFragment.this, code);
                        }
                        break;
                    case 1:
                        if (checkTokenStatus()) {
                            ImageSelectUtil.selectImageFromAlbum(MainFragment.this, code);
                        }
                        break;
                    case 2:
                        ImageSelectUtil.selectImageFromAlbum(MainFragment.this, code);
                        break;
                    default:
                        break;
                }
                if (aBoolean) {
                } else {
                    Toast.makeText(activity, "请先授予权限，否者该功能无法使用！", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Common.REQUEST_CODE.PICTURE_RECOGNITION:
                    // 图片、视频、音频选择结果回调
                    if (PictureSelector.obtainMultipleResult(data) == null) {
                        Toast.makeText(activity, "获取照片识别，请检查权限后重试！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    promptDialog.showLoading("正在识别");
                    selectList = PictureSelector.obtainMultipleResult(data);

                    historyResponse = new HistoryResponse();
                    historyResponse.setImagePath(selectList.get(0).getPath());


                    RecognizeService.recGeneralBasic(getActivity(), FileUtil.getFile(selectList.get(0).getPath()).getAbsolutePath(),
                            new RecognizeService.ServiceListener() {
                                @Override
                                public void onResult(String result) {
                                    infoPopText(result);
                                }
                            });
                    break;
                case Common.REQUEST_CODE.PICTURE_SHARPENING:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                    intent.putExtra("PATH", selectList.get(0).getPath());
                    getActivity().startActivity(intent);
                    break;
                default:
                    break;
            }
        }

    }

    private void infoPopText(final String result) {
        Gson gson = new Gson();
        OcrResponse ocrResponse = gson.fromJson(result, OcrResponse.class);

        if (ocrResponse != null) {
            StringBuffer stringBuffer = new StringBuffer();
            List<OcrResponse.WordsResultBean> words_result = ocrResponse.getWords_result();
            for (OcrResponse.WordsResultBean wordsResultBean : words_result) {
                stringBuffer.append(wordsResultBean.getWords() + "\n");
            }

            if ("".equals(stringBuffer.toString().trim())) {
                promptDialog.dismissImmediately();
                alertText("识别失败", "没有识别到文字，请重新选择图片");
            } else {
                promptDialog.dismissImmediately();
                historyResponse.setText(stringBuffer.toString().trim());
                SqlUtil.insert(historyResponse);

                alertText("识别成功", stringBuffer.toString().trim());
                copyText(stringBuffer.toString().trim());
            }

        }
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

    private void copyText(String text) {

        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(activity, "文字已复制到粘贴板", Toast.LENGTH_SHORT).show();
    }
}

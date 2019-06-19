package com.example.ocrandtranslate.util;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.example.ocrandtranslate.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

/**
 * @author devel
 */
public class ImageSelectUtil {

    public static final int CHOOSE_MULTIPLE_REQUEST = 188;

    /**
     * 直接打开拍照功能，不能从相册中选取
     *
     * @param activity
     * @param requestCode
     */
    public static void selectImageOfCamera(final Fragment activity, final int requestCode) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(1)
                .minSelectNum(1)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .previewVideo(false)
                .enablePreviewAudio(true)
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .glideOverride(160, 160)
                .hideBottomControls(true)
                .isGif(true)
                .openClickSound(false)
                .previewEggs(true)
                .minimumCompressSize(1000)
                .forResult(requestCode);
    }


    /**
     * 从相册中选取多张照片
     *
     * @param activity
     * @param requestCode
     */
    public static void selectImageFromAlbum(final Fragment activity, final int requestCode) {
        PermissionCheckUtils.requestCamera(new PermissionCheckUtils.OnPermissionGrantedListener() {
            @Override
            public void onPermissionGranted() {
                PictureSelector.create(activity)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_default_style)
                        .maxSelectNum(1)
                        .minSelectNum(1)
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .previewVideo(false)
                        .enablePreviewAudio(true)
                        .isCamera(true)
                        .isZoomAnim(true)
                        .enableCrop(false)
                        .compress(true)
                        .glideOverride(160, 160)
                        .isGif(true)
                        .freeStyleCropEnabled(true)
                        .circleDimmedLayer(false)
                        .showCropFrame(false)
                        .showCropGrid(false)
                        .openClickSound(false)
                        .minimumCompressSize(1000)
                        .forResult(requestCode);
            }
        });
    }

    public static void selectMultipleImage(final Activity activity, final int num) {
        PermissionCheckUtils.requestCamera(new PermissionCheckUtils.OnPermissionGrantedListener() {
            @Override
            public void onPermissionGranted() {
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(activity)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_default_style)
                        .maxSelectNum(num)
                        .minSelectNum(1)
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .imageFormat(PictureMimeType.PNG)
                        .isZoomAnim(true)
                        .sizeMultiplier(0.5f)
                        .setOutputCameraPath("/CustomPath")
                        .enableCrop(false)
                        .compress(true)
                        .hideBottomControls(true)
                        .isGif(false)
                        .openClickSound(false)
                        .previewEggs(true)
                        .minimumCompressSize(100)
                        .synOrAsy(true)
                        .forResult(CHOOSE_MULTIPLE_REQUEST);
            }
        });
    }
}

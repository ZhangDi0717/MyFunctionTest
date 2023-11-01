package com.zhangdi.myfunctiontest.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.zhangdi.myfunctiontest.R;
import com.zhangdi.myfunctiontest.databinding.ActivityCameraBinding;
import java.nio.ByteBuffer;

public class CameraActivity extends AppCompatActivity  {

    private static final String TAG = CameraActivity.class.getSimpleName();

    private Context mContext;

    private Camera2Proxy mCameraProxy;

    private ActivityCameraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        mContext = this;
        initView();
    }

    private void initView() {
//        mPhoto = findViewById(R.id.bt_photo);
//        mPhoto.setOnClickListener(this);
//
//        mBtnSwitch = findViewById(R.id.bt_switch);
//        mBtnSwitch.setOnClickListener(this);
//
//
//
//        mTextureView = findViewById(R.id.camera_surface_view);
//        mCameraProxy = mTextureView.getCameraProxy();
        mCameraProxy = binding.cameraSurfaceView.getCameraProxy();

        binding.setAction(new Action());
    }


    public class Action{
        public void onPhotoClick(View v) {
            Log.d(TAG, "onPhotoClick: ");
            mCameraProxy.setImageAvailableListener(mOnImageAvailableListener);
            mCameraProxy.captureStillPicture(); // 拍照
        }

        public void onSwitchClick(View v){
            Log.d(TAG, "onSwitchClick: ");
            mCameraProxy.switchCamera(binding.cameraSurfaceView.getWidth(), binding.cameraSurfaceView.getHeight());
            mCameraProxy.startPreview();
        }
    }





    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = reader -> {
        new ImageSaveTask().execute(reader.acquireNextImage()); // 保存图片
    };


    private class ImageSaveTask extends AsyncTask<Image, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Image... images) {
            ByteBuffer buffer = images[0].getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            long time = System.currentTimeMillis();
            if (mCameraProxy.isFrontCamera()) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.d(TAG, "BitmapFactory.decodeByteArray time: " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
                // 前置摄像头需要左右镜像
                Bitmap rotateBitmap = ImageUtils.rotateBitmap(bitmap, 0, true, true);
                Log.d(TAG, "rotateBitmap time: " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
                ImageUtils.saveBitmap(mContext,rotateBitmap);
                Log.d(TAG, "saveBitmap time: " + (System.currentTimeMillis() - time));
                rotateBitmap.recycle();
            } else {
                ImageUtils.saveImage(mContext,bytes);
                Log.d(TAG, "saveBitmap time: " + (System.currentTimeMillis() - time));
            }
            images[0].close();
            return ImageUtils.getLatestThumbBitmap(mContext);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute: ");
//            mPictureIv.setImageBitmap(bitmap);
        }
    }
}
package com.zhangdi.myfunctiontest.camera;


import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;

import android.util.AttributeSet;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.widget.LinearLayout;


public class Camera2TextureView extends TextureView {

    private static final String TAG = "CameraTextureView";
    private Camera2Proxy mCameraProxy;
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;
    private float mOldDistance;
    private Context mContext;


    public Camera2TextureView(Context context) {
        this(context, null);
    }

    public Camera2TextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Camera2TextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    private int widthPixels;
    private int heightPixels;
    public Camera2TextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        heightPixels = mContext.getResources().getDisplayMetrics().heightPixels-50;
        Log.d(TAG, "Camera2TextureView: widthPixels=" + widthPixels + ", heightPixels = "+heightPixels);
        init(context);
    }

    private void init(Context context) {
        setSurfaceTextureListener(mSurfaceTextureListener);
        mCameraProxy = new Camera2Proxy((Activity) context);
    }


    private SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            /*
            * id =1: width: 720, height: 1042
            *
            * */
            Log.v(TAG, "onSurfaceTextureAvailable. width: " + width + ", height: " + height);// id=0:width: 720, height: 1042
            mCameraProxy.openCamera(width, height);
            mCameraProxy.setPreviewSurface(surface);
            // resize TextureView
            int previewWidth = mCameraProxy.getPreviewSize().getWidth();
            int previewHeight = mCameraProxy.getPreviewSize().getHeight();
            /**
             * id=1:previewWidth=1024, previewHeight = 768
             */
            Log.d(TAG, "onSurfaceTextureAvailable: previewWidth=" + previewWidth + ", previewHeight = " + previewHeight);
            if (width > height) {
                setAspectRatio(previewWidth, previewHeight);
                if(mCameraProxy.getCameraId() == 2){
                    Matrix matrix = new Matrix();
                    RectF viewRect = new RectF(0, 0, previewWidth, previewHeight);
                    float centerX = viewRect.centerX();
                    float centerY = viewRect.centerY();
                    matrix.postRotate(90, centerX, centerY);

                    setTransform(matrix);
                }
            } else {
                setAspectRatio(previewHeight, previewWidth);
                if(mCameraProxy.getCameraId() == 2){
                    Matrix matrix = new Matrix();
                    RectF viewRect = new RectF(0, 0, previewHeight, previewWidth);
                    float centerX = viewRect.centerX();
                    float centerY = viewRect.centerY();
                    matrix.postRotate(90, centerX, centerY);

                    setTransform(matrix);
                }
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            /**
             * id=0:width: 720, height: 960
             * id=1: width: 720, height: 960
             */
            Log.v(TAG, "onSurfaceTextureSizeChanged. width: " + width + ", height: " + height);//
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.v(TAG, "onSurfaceTextureDestroyed");
            mCameraProxy.releaseCamera();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    public Camera2Proxy getCameraProxy() {
        return mCameraProxy;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            Log.d(TAG, "onMeasure: mRatioWidth = "+mRatioWidth +"; mRatioHeight = "+mRatioHeight);
            if (mRatioWidth>widthPixels){//限制宽过大
                Log.d(TAG, "onMeasure: 限制宽过大");
                Log.d(TAG, "onMeasure: mRatioWidth = "+mRatioWidth +"; mRatioHeight = "+mRatioHeight);
                mRatioHeight = (int) (mRatioHeight*1.0f*widthPixels/mRatioWidth);
                mRatioWidth = widthPixels;
                Log.d(TAG, "onMeasure: mRatioWidth = "+mRatioWidth +"; mRatioHeight = "+mRatioHeight);
            }

            if (mRatioHeight>heightPixels){//限制高过大
                Log.d(TAG, "onMeasure: 限制高过大");
                Log.d(TAG, "onMeasure: mRatioWidth = "+mRatioWidth +"; mRatioHeight = "+mRatioHeight);
                mRatioWidth = (int)(mRatioWidth*heightPixels/mRatioHeight);
                mRatioHeight = heightPixels;
                Log.d(TAG, "onMeasure: mRatioWidth = "+mRatioWidth +"; mRatioHeight = "+mRatioHeight);
            }
            if (width < height * mRatioWidth / mRatioHeight) {
                Log.d(TAG, "onMeasure: width = "+width +"; height = "+(width * mRatioHeight / mRatioWidth));
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                Log.d(TAG, "onMeasure: width = "+(height * mRatioWidth / mRatioHeight) +"; height = "+height);
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getPointerCount() == 1) {
//            mCameraProxy.focusOnPoint((int) event.getX(), (int) event.getY(), getWidth(), getHeight());
//            return true;
//        }
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_POINTER_DOWN:
//                mOldDistance = getFingerSpacing(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float newDistance = getFingerSpacing(event);
//                if (newDistance > mOldDistance) {
//                    mCameraProxy.handleZoom(true);
//                } else if (newDistance < mOldDistance) {
//                    mCameraProxy.handleZoom(false);
//                }
//                mOldDistance = newDistance;
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

}
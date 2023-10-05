package com.zhangdi.utils.toast;




import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangdi.utils.R;

import java.util.Objects;


/**
 * Created by zhangdi45 on 2022/12/9.
 * Toast统一管理类
 */
public class ToastUtil {
    private static final String TAG = "ToastUtil";

    private static boolean isShow = true;//默认显示
    private static Toast mTextToast = null;//全局唯一的Toast
    private static Toast mViewToast = null;//全局唯一的Toast

    private static Runnable mRunnable = null;//
    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * private控制不应该被实例化
     */
    private ToastUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 全局控制是否显示Toast
     *
     * @param isShowToast a
     */
    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }



    /**
     * 取消Toast显示
     */
    public static void cancelToast() {
        if (isShow && mTextToast != null) {
            Log.d(TAG, "cancelToast: ");
            mTextToast.cancel();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context a
     * @param message a
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (mTextToast == null) {
                mTextToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                mTextToast.setText(message);
                mTextToast.setDuration(Toast.LENGTH_SHORT);
            }
            View view = mTextToast.getView();
            if (view!=null && view.isShown()) {
                mTextToast.cancel();
            }
            mTextToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context a
     * @param resId   资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(Context context, int resId) {
        if (isShow) {
            if (mTextToast == null) {
                mTextToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
            } else {
                mTextToast.setText(resId);
            }
            View view = mTextToast.getView();
            if (view!=null && view.isShown()) {
                mTextToast.cancel();
            }
            mTextToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context a
     * @param message a
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mTextToast == null) {
                        mTextToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    } else {
                        mTextToast.setText(message);
                    }
                    View view = mTextToast.getView();
                    if (view!=null && view.isShown()) {
                        mTextToast.cancel();
                    }
                    mTextToast.show();

                }
            });
        }
    }

    /**
     *
     * @param context Context
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(Context context, int resId) {
        if (isShow) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mTextToast == null) {
                        mTextToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
                    } else {
                        mTextToast.setText(resId);
                    }
                    View view = mTextToast.getView();
                    if (view!=null && view.isShown()) {
                        mTextToast.cancel();
                    }
                    mTextToast.show();
                }
            });
        }
    }


    /**
     * 自定义显示Toast时间
     *
     * @param duration 单位:毫秒
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (!isShow) {
            return;
        }
        if (mRunnable !=null){
            // mHandler.removeCallbacks(mRunnable);
            cancelToast();
        }
        mRunnable = () -> {
            Log.d(TAG, "show: ");
            if (mTextToast == null) {
                mTextToast = Toast.makeText(context, message, duration);
            } else {
                mTextToast.setText(message);
            }
            View view = mTextToast.getView();
            if (view!=null && view.isShown()) {
                mTextToast.cancel();
            }
            mTextToast.show();
        };
        mHandler.postDelayed(mRunnable,300);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context a
     * @param resId    资源ID:getResources().getString(R.string.xxxxxx);
     * @param duration 单位:毫秒
     */
    public static void show(Context context, int resId, int duration) {
        if (!isShow) {
            return;
        }
        if (mRunnable !=null){
            // mHandler.removeCallbacks(mRunnable);
            cancelToast();
        }
        mRunnable = () -> {
            Log.d(TAG, "show: ");
            if (mTextToast == null) {
                mTextToast = Toast.makeText(context, resId, duration);
            } else {
                mTextToast.setText(resId);
            }
            View view = mTextToast.getView();
            if (view!=null && view.isShown()) {
                mTextToast.cancel();
            }
            mTextToast.show();
        };
        mHandler.postDelayed(mRunnable,300);
    }

    /**
     * 自定义Toast的View
     *
     * @param context a
     * @param message a
     * @param duration 单位:毫秒
     * @param view     显示自己的View
     */
    public static void customToastView(Context context, CharSequence message, int duration, View view) {
        if (isShow) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mViewToast == null) {
                        mViewToast = Toast.makeText(context, message, duration);
                    } else {
                        mViewToast.setText(message);
                    }
                    if (view != null) {
                        mViewToast.setView(view);
                    }

                    View view = mTextToast.getView();
                    if (view!=null && view.isShown()) {
                        mTextToast.cancel();
                    }
                    mViewToast.show();
                }
            });
        }
    }


    /**
     * 居中显示
     * ag:ToastUtil.customToastViewAndGravity(mContext,getString(R.string.main_device_connect_fail_tip),
     *                                     Toast.LENGTH_LONG,R.layout.toast_view,R.id.toast_view_tip,Gravity.CENTER,0,0);
     * @param context a
     * @param message a
     * @param duration a
     * @param viewXMLId a
     * @param viewId a
     * @param gravity a
     * @param xOffset a
     * @param yOffset a
     */
    public static void customToastViewAndGravity(Context context, CharSequence message, int duration, int viewXMLId,int viewId, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mViewToast == null) {
                        mViewToast = Toast.makeText(context, message, duration);
                    }

                    View view = LayoutInflater.from(context).inflate(viewXMLId, null);
                    TextView toastView = view.findViewById(viewId);
                    toastView.setText(message);

                    mViewToast.setView(view);
                    mViewToast.setGravity(gravity, xOffset, yOffset);
                    View toastView1 = mTextToast.getView();
                    if (toastView1!=null && toastView1.isShown()) {
                        mTextToast.cancel();
                    }
                    mViewToast.show();
                }
            });
        }
    }



    /**
     * 自定义Toast的位置
     *
     * @param context a
     * @param message a
     * @param duration 单位:毫秒
     * @param gravity a
     * @param xOffset a
     * @param yOffset a
     */
    public static void customToastGravity(Context context, CharSequence message, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mTextToast == null) {
                        Log.d("ZD", "run: ");
                        mTextToast = Toast.makeText(context, message, duration);
                        Log.d("ZD", "run: mTextToast = "+mTextToast);

                    } else {
                        mTextToast.setText(message);
                    }
                    if (mTextToast != null){
                        mTextToast.setGravity(gravity, xOffset, yOffset);
                    }
                    View view = mTextToast.getView();
                    if (view!=null && view.isShown()) {
                        mTextToast.cancel();
                    }
                    mTextToast.show();
                }
            });
        }
    }


    /**
     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
     * @param context
     * @param message
     * @param icon
     * @param duration
     * @param viewId
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showToastWithImageAndText(Context context, CharSequence message, Drawable icon, int duration, int viewId, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mViewToast == null) {
                        mViewToast = Toast.makeText(context, message, duration);
                    }

                    View view = LayoutInflater.from(context).inflate(viewId, null);
                    TextView toastTip = view.findViewById(R.id.toast_view_icon_string_tip);
                    ImageView toastIcon = view.findViewById(R.id.toast_view_icon_string_icon);
                    toastTip.setText(message);
                    toastIcon.setImageDrawable(icon);
                    mViewToast.setView(view);
                    mViewToast.setGravity(gravity, xOffset, yOffset);

                    if (mViewToast.getView()!=null && mViewToast.getView().isShown()) {
                        mViewToast.cancel();
                    }
                    Log.d("TAG", "run: 6");
                    mViewToast.show();
                }
            });
        }
    }



//    /**
//     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
//     *
//     * @param context
//     * @param message
//     * @param iconResId 图片的资源id,如:R.drawable.icon
//     * @param duration
//     * @param gravity
//     * @param xOffset
//     * @param yOffset
//     */
//    @SuppressLint("ShowToast")
//    public static void showToastWithImageAndText(Context context, CharSequence message, int iconResId, int duration, int gravity, int xOffset, int yOffset) {
//        if (isShow) {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (mToast == null) {
//                        Log.d(TAG, "run: mToast == null");
//                        mToast = Toast.makeText(context, message, duration);
//                        Log.d(TAG, "run: mToast = "+mToast);
//                    } else {
//                        Log.d(TAG, "run: mToast = "+mToast);
//                        mToast.setText(message);
//                    }
//                    mToast.setGravity(gravity, xOffset, yOffset);
//                    LinearLayout toastView = (LinearLayout) mToast.getView();
//                    ImageView imageView = new ImageView(context);
//                    imageView.setImageResource(iconResId);
//                    toastView.addView(imageView, 0);
//                    if (mToast.getView().isShown()) {
//                        mToast.cancel();
//                    }
//                    mToast.show();
//                }
//            });
//        }
//    }

    /**
     * 自定义Toast,针对类型CharSequence
     *
     * @param context the context
     * @param message the message
     * @param duration  the duration
     * @param view the duration
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity a
     * @param xOffset a
     * @param yOffset a
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin a
     * @param verticalMargin c
     */
    @SuppressLint("ShowToast")
    public static void customToastAll(Context context, CharSequence message, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mViewToast == null) {
                        mViewToast = Toast.makeText(context, message, duration);
                    } else {
                        mViewToast.setText(message);
                    }
                    if (view != null) {
                        mViewToast.setView(view);
                    }
                    if (isMargin) {
                        mViewToast.setMargin(horizontalMargin, verticalMargin);
                    }
                    if (isGravity) {
                        mViewToast.setGravity(gravity, xOffset, yOffset);
                    }
                    View view = mTextToast.getView();
                    if (view!=null && view.isShown()) {
                        mTextToast.cancel();
                    }
                    mViewToast.show();
                }
            });
        }
    }

    /**
     * 自定义Toast,针对类型resId
     *
     * @param context the context
     * @param resId a
     * @param duration a
     * @param view             :应该是一个布局，布局中包含了自己设置好的内容
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity a
     * @param xOffset a
     * @param yOffset a
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin a
     * @param verticalMargin a
     */
    @SuppressLint("ShowToast")
    public static void customToastAll(Context context, int resId, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mViewToast == null) {
                        mViewToast = Toast.makeText(context, resId, duration);
                    } else {
                        mViewToast.setText(resId);
                    }
                    if (view != null) {
                        mViewToast.setView(view);
                    }
                    if (isMargin) {
                        mViewToast.setMargin(horizontalMargin, verticalMargin);
                    }
                    if (isGravity) {
                        mViewToast.setGravity(gravity, xOffset, yOffset);
                    }
                    View view = mTextToast.getView();
                    if (view!=null && view.isShown()) {
                        mTextToast.cancel();
                    }
                    mViewToast.show();
                }
            });
        }
    }
}

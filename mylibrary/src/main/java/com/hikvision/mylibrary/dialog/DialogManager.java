package com.hikvision.mylibrary.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hikvision.mylibrary.toast.ToastUtil;
import com.hikvision.mylibrary.R;

import java.util.ArrayList;
import java.util.List;


/**
 * FileName: DialogManager
 * Founder: LiuGuiLin
 * Profile: 提示框管理类
 */
public class DialogManager {
    private static final String TAG = "ZDDialogManager";
    private static volatile DialogManager mInstance = null;
    private static DialogView mSuccessView;
    private static ImageView loadingIcon;

    private DialogManager() {

    }

    public static DialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DialogManager.class) {
                if (mInstance == null) {
                    mInstance = new DialogManager();
                }
            }
        }
        return mInstance;
    }

    public DialogView initView(Context mContext, int layout) {
        return new DialogView(mContext, layout, R.style.Theme_Dialog, Gravity.CENTER);
    }

    public DialogView initView(Context mContext, int layout, int gravity) {
        return new DialogView(mContext, layout, R.style.Theme_Dialog, gravity);
    }

    public void show(DialogView view) {
        if (view != null) {
            if (!view.isShowing()) {
                view.show();
            }
        }
    }

    public void hide(DialogView view) {
        if (view != null) {
            if (view.isShowing()) {
                view.dismiss();
            }

        }
    }

    private static RoundProgressBarWidthNumber mRoundProgressBarWidthNumber;
    /**
     * 展示消息提示框弹框
     * @param activity
     */
    public static void showIconMessageDialog(Context activity, int progress) {
        mSuccessView = getInstance().initView(activity, R.layout.dialog_icon_message);
        mRoundProgressBarWidthNumber = mSuccessView.findViewById(R.id.dialog_icon_message_progress);
        mRoundProgressBarWidthNumber.setProgress(progress);
        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }

    /**
     * 加载中
     * @param activity
     */

    public static void showIconMessageDialogWithoutProgress(Context activity){
        mSuccessView = getInstance().initView(activity, R.layout.dialog_icon_loading);

        loadingIcon = mSuccessView.findViewById(R.id.dialog_icon_loading);
        Animation rotateAnimation = AnimationUtils.loadAnimation(activity, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();

        if (rotateAnimation != null){
            rotateAnimation.setInterpolator(lin);
        }else {
            loadingIcon.setAnimation(rotateAnimation);
        }
        loadingIcon.startAnimation(rotateAnimation);
        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }

    /**
     * 隐藏
     */
    public static void hideIconMessageDialogWithoutProgress(){
        loadingIcon.clearAnimation();
        DialogManager.getInstance().hide(mSuccessView);
    }






    /**
     * 展示消息提示框弹框
     * @param activity
     */
    public static void showIconMessageDialog(Context activity, int progress,int resourceId) {
        mSuccessView = getInstance().initView(activity, R.layout.dialog_icon_message);
        mRoundProgressBarWidthNumber = mSuccessView.findViewById(R.id.dialog_icon_message_progress);
        mRoundProgressBarWidthNumber.setProgress(progress);
        TextView textView =  mSuccessView.findViewById(R.id.dialog_icon_message);
        textView.setText(resourceId);
        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }

    public static void setProgress(int progress) {
        mRoundProgressBarWidthNumber.setProgress(progress);
        if(progress>=100){
            DialogManager.getInstance().hide(mSuccessView);
        }
    }

    public static void hideProgress(){
        DialogManager.getInstance().hide(mSuccessView);
    }


    /**
     * 展示确认取消选择框
     * @param activity 上下文
     * @param title 弹窗标题
     * @param tip 弹窗提示语
     * @param cancelStr 取消字符
     * @param confirmStr 确认位置字符
     * @param dialogCallback 回调
     */
    public static void showTwoResultDialog(Context activity,String title, String tip,String cancelStr, String confirmStr, DialogCallback dialogCallback){
        mSuccessView = getInstance().initView(activity, R.layout.dialog_two_result);
        TextView mTVTitle = mSuccessView.findViewById(R.id.dialog_two_result_title);
        TextView mTVTip = mSuccessView.findViewById(R.id.dialog_two_result_tip);
        TextView mTVCancel = mSuccessView.findViewById(R.id.dialog_two_result_cancel);
        TextView mTVConfirm = mSuccessView.findViewById(R.id.dialog_two_result_confirm);
        mTVTitle.setText(title);
        mTVTip.setText(tip);
        mTVCancel.setText(cancelStr);
        mTVConfirm.setText(confirmStr);

        mTVConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: confirm");
                DialogManager.getInstance().hide(mSuccessView);
                dialogCallback.onConfirm();

            }
        });

        mTVCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: cancel");
                DialogManager.getInstance().hide(mSuccessView);
                dialogCallback.onCancel();
            }
        });
        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }

    /**
     * 展示消息提示框弹框
     * @param activity
     * @param tip
     * @param hint
     * @param dialogCallback
     */
    public static void showMessageDialog(Activity activity,String tip,String hint, DialogStringCallback dialogCallback) {
        mSuccessView = getInstance().initView(activity, R.layout.dialog_message_tip);
        TextView mTVTip = mSuccessView.findViewById(R.id.dialog_message_tip);
        TextView mTVHint = mSuccessView.findViewById(R.id.dialog_message_hint);


        if(tip!=null){
            mTVTip.setText(tip);
        }

        if(hint!=null){
            mTVHint.setText(hint);
        }


        TextView mIvDestroy = mSuccessView.findViewById(R.id.dialog_message_destroy);
        mIvDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mSuccessView);
                dialogCallback.onConfirm("");

            }
        });
        TextView mIvCancel = mSuccessView.findViewById(R.id.dialog_message_cancel);
        mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mSuccessView);
                dialogCallback.onCancel();
            }
        });
        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }


    //展示数据输入弹框
    public static void showEditNumberDialog(Activity activity,String tip,String hint,String value, DialogStringCallback dialogCallback) {
        mSuccessView = getInstance().initView(activity, R.layout.dialog_edit_text);
        TextView mTVTip = mSuccessView.findViewById(R.id.dialog_edit_text_tip);
        TextView mTVHint = mSuccessView.findViewById(R.id.dialog_edit_text_hint);
        EditText mITValue = mSuccessView.findViewById(R.id.dialog_edit_text_value);

        if(tip!=null){
            mTVTip.setText(tip);
        }

        if(hint!=null){
            mTVHint.setText(hint);
        }

        if(value!=null){
            mITValue.setHint(value);
        }

        TextView mIvConfirm = mSuccessView.findViewById(R.id.dialog_change_bt_name_confirm);
        mIvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mSuccessView);
                String value = mITValue.getText().toString().trim();
                dialogCallback.onConfirm(value);

            }
        });
        TextView mIvCancel = mSuccessView.findViewById(R.id.dialog_change_bt_name_cancel);
        mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mSuccessView);
                dialogCallback.onCancel();
            }
        });
        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }


    //展示数据选择弹框
    public static void showOptionSelectDialog(Activity activity, String tip, List<String> stringList, String value, DialogStringCallback dialogCallback) {
        if(stringList==null){
            Log.d(TAG, "showOptionSelectDialog: stringList==null");
            return;
        }
        mSuccessView = DialogManager.getInstance().initView(activity, R.layout.dialog_option_select);
        ListView optionListView = mSuccessView.findViewById(R.id.dialog_option_select_list);
        TextView tipView = mSuccessView.findViewById(R.id.dialog_option_select_tip);
        tipView.setText(tip);
        StringIconAdapter connectTimeAdapter = new StringIconAdapter(activity,stringList,value);
        optionListView.setAdapter(connectTimeAdapter);

        optionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogManager.getInstance().hide(mSuccessView);
                dialogCallback.onConfirm(stringList.get(position));
            }
        });

        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }

    //展示数据多选弹框
    public static void showOptionMulSelectDialog(Activity activity, String tip,String nullTip, List<String> stringList,Drawable selectedIcon,Drawable unselectedIcon, DialogMultiCallback dialogCallback) {
        if(stringList==null){
            Log.d(TAG, "showOptionMulSelectDialog: stringList==null");
            return;
        }
        List<Boolean> isSelectedList = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            isSelectedList.add(false);
        }
        mSuccessView = DialogManager.getInstance().initView(activity, R.layout.dialog_multi_option_select);
        ListView multiOptionListView = mSuccessView.findViewById(R.id.dialog_multi_option_select_list);
        TextView confirm = mSuccessView.findViewById(R.id.dialog_multi_option_select_confirm);
        TextView cancel = mSuccessView.findViewById(R.id.dialog_multi_option_select_cancel);
        TextView tipView = mSuccessView.findViewById(R.id.dialog_multi_option_select_tip);
        tipView.setText(tip);
        StringMultiIconAdapter stringMultiIconAdapter = new StringMultiIconAdapter(activity,stringList,isSelectedList,selectedIcon,unselectedIcon);
        multiOptionListView.setAdapter(stringMultiIconAdapter);

        multiOptionListView.setOnItemClickListener((parent, view, position, id) -> {
            isSelectedList.set(position,!isSelectedList.get(position));
            stringMultiIconAdapter.notifyDataSetChanged();
        });


        confirm.setOnClickListener(v -> {
            List<Integer> selectedOptionList = new ArrayList<>();
            for (int i = 0; i < isSelectedList.size(); i++) {
                if(isSelectedList.get(i)){
                    selectedOptionList.add(i);
                }
            }
            if(selectedOptionList.size()==0){
                 ToastUtil.customToastViewAndGravity(activity,nullTip,
                                                    Toast.LENGTH_LONG,R.layout.dialog_icon_loading,R.id.toast_view_tip,Gravity.CENTER,0,0);

            }else {
                DialogManager.getInstance().hide(mSuccessView);
                dialogCallback.onConfirm(selectedOptionList);
            }

        });

        cancel.setOnClickListener(v -> {
            dialogCallback.onCancel();
            DialogManager.getInstance().hide(mSuccessView);
        });


        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }

    public static class Entity{
        public int key;
        public String value;

        public Entity(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    //展示数据多选弹框
    public static void showEntityOptionMulSelectDialog(Activity activity, String tip,String nullTip, List<Entity> stringList,Drawable selectedIcon,Drawable unselectedIcon, DialogMultiCallback dialogCallback) {
        if(stringList==null){
            Log.d(TAG, "showOptionMulSelectDialog: stringList==null");
            return;
        }
        List<Boolean> isSelectedList = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            isSelectedList.add(false);
        }
        mSuccessView = DialogManager.getInstance().initView(activity, R.layout.dialog_multi_option_select);
        ListView multiOptionListView = mSuccessView.findViewById(R.id.dialog_multi_option_select_list);
        TextView confirm = mSuccessView.findViewById(R.id.dialog_multi_option_select_confirm);
        TextView cancel = mSuccessView.findViewById(R.id.dialog_multi_option_select_cancel);
        TextView tipView = mSuccessView.findViewById(R.id.dialog_multi_option_select_tip);
        tipView.setText(tip);
        EntityMultiIconAdapter entityMultiIconAdapter = new EntityMultiIconAdapter(activity,stringList,isSelectedList,selectedIcon,unselectedIcon);
        multiOptionListView.setAdapter(entityMultiIconAdapter);

        multiOptionListView.setOnItemClickListener((parent, view, position, id) -> {
            isSelectedList.set(position,!isSelectedList.get(position));
            entityMultiIconAdapter.notifyDataSetChanged();
        });


        confirm.setOnClickListener(v -> {
            List<Integer> selectedOptionList = new ArrayList<>();
            for (int i = 0; i < isSelectedList.size(); i++) {
                if(isSelectedList.get(i)){
                    selectedOptionList.add(i);
                }
            }
            if(selectedOptionList.size()==0){
                ToastUtil.customToastViewAndGravity(activity,nullTip,
                        Toast.LENGTH_LONG,R.layout.toast_view,R.id.toast_view_tip,Gravity.CENTER,0,0);

            }else {
                DialogManager.getInstance().hide(mSuccessView);
                dialogCallback.onConfirm(selectedOptionList);
            }

        });

        cancel.setOnClickListener(v -> {
            dialogCallback.onCancel();
            DialogManager.getInstance().hide(mSuccessView);
        });


        mSuccessView.setCancelable(false);
        DialogManager.getInstance().show(mSuccessView);
    }

    public interface DialogCallback{
        void onConfirm();
        void onCancel();
    }

    public interface DialogStringCallback{
        void onConfirm(String value);
        void onCancel();
    }

    public interface DialogMultiCallback{
        void onConfirm(List<Integer> selectedList);
        void onCancel();
    }

}

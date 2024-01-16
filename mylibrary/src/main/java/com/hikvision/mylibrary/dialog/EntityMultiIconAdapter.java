package com.hikvision.mylibrary.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hikvision.mylibrary.R;

import java.util.List;

public class EntityMultiIconAdapter extends BaseAdapter {
    private static final String TAG = "StringMultiIconAdapter";
    private Context context;
    private List<DialogManager.Entity> optionList;
    private List<Boolean> isSelectedList;
    private LayoutInflater inflater;
    private Drawable selectedIcon;
    private Drawable unselectedIcon;
    public EntityMultiIconAdapter(Context context, List<DialogManager.Entity> optionList, List<Boolean> isSelectedList, Drawable selectedIcon, Drawable unselectedIcon) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.optionList = optionList;
        this.isSelectedList = isSelectedList;
        this.selectedIcon = selectedIcon;
        this.unselectedIcon = unselectedIcon;

    }


    @Override
    public int getCount() {

        return optionList == null ? 0 : optionList.size();
    }

    @Override
    public Object getItem(int position) {
        return optionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_string_icon, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.dialog_connect_time_value);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.dialog_connect_time_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.name.setText(optionList.get(position).value);
        Log.d(TAG, "getView: "+isSelectedList.get(position));
        if(isSelectedList.get(position)){
            viewHolder.icon.setImageDrawable(selectedIcon);
        }else {
            viewHolder.icon.setImageDrawable(unselectedIcon);
        }
        return convertView;
    }

    class ViewHolder {
        TextView name;
        ImageView icon;
    }
}

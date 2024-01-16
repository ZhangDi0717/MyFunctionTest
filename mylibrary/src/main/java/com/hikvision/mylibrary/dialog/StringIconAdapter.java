package com.hikvision.mylibrary.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hikvision.mylibrary.R;

import java.util.List;

public class StringIconAdapter extends BaseAdapter {
    private Context context;
    private List<String> optionList;
    private LayoutInflater inflater;
    private String currentSelectOption;

    public StringIconAdapter(Context context, List<String> optionList, String currentSelectOption) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.optionList = optionList;
        this.currentSelectOption = currentSelectOption;
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
        viewHolder.name.setText(optionList.get(position));
        if(currentSelectOption.equals(optionList.get(position))){
            viewHolder.icon.setVisibility(View.VISIBLE);
        }else {
            viewHolder.icon.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView name;
        ImageView icon;
    }



}
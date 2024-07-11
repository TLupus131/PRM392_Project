package com.wolf.bookingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.wolf.bookingapp.R;
import com.wolf.bookingapp.entity.Item;
import com.wolf.bookingapp.entity.UserManager;

import java.util.List;

public class ButtonIconAdapter extends BaseAdapter {

    private Context context;
    private List<Item> itemList;

    public ButtonIconAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.itemIcon = convertView.findViewById(R.id.itemIcon);
            holder.itemText = convertView.findViewById(R.id.itemText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = itemList.get(position);
        holder.itemIcon.setImageResource(item.getIcon());
        holder.itemText.setText(item.getText());

        if ("Đăng xuất".equals(item.getText())) {
            holder.itemText.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.itemText.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getToActivity() != null) {
                    Intent intent = new Intent(context, item.getToActivity());
                    if ("Đăng xuất".equals(item.getText())) {
                        UserManager.getInstance().setAuthUser(null);
                        clearPreferences();
                    }
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private void clearPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("H_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    static class ViewHolder {
        ImageView itemIcon;
        TextView itemText;
    }
}

package com.wolf.bookingapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.wolf.bookingapp.R;
import com.wolf.bookingapp.entity.Comment;
import com.wolf.bookingapp.entity.Region;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private final Context context;
    private final List<Comment> list;

    public CommentAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_comment_item, parent, false);
            holder = new ViewHolder();
            holder.sivAvatar = convertView.findViewById(R.id.sivUserAvatar);
            holder.tvName = convertView.findViewById(R.id.tvUserName);
            holder.tvNationality = convertView.findViewById(R.id.tvUserNationality);
            holder.tvContent = convertView.findViewById(R.id.tvCommentContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Comment comment = list.get(position);
        if (comment != null) {
            Picasso.get().load(comment.getUserAvatar()).into(holder.sivAvatar);
            if (!comment.getUserName().equals("null")) {
                holder.tvName.setText(comment.getUserName());
            } else {
                holder.tvName.setText("Anonymous");
            }
            if (!comment.getUserNationality().equals("null")) {
                holder.tvNationality.setText(comment.getUserNationality());
            } else {
                holder.tvNationality.setText("Anonymous");
            }
            holder.tvContent.setText(comment.getContent());
        }
        return convertView;
    }

    static class ViewHolder {
        ShapeableImageView sivAvatar;
        TextView tvName, tvNationality, tvContent;
    }
}

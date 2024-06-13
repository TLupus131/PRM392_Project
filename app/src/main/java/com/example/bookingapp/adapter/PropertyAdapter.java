package com.example.bookingapp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.entity.Property;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PropertyAdapter extends BaseAdapter {

    private Context context;
    private List<Property> list;

    public PropertyAdapter(Context context, List<Property> list) {
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_property_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.property_item_imageView);
            holder.tvName = convertView.findViewById(R.id.property_item_tvName);
            holder.tvType = convertView.findViewById(R.id.property_item_tvType);
            holder.tvQuantity = convertView.findViewById(R.id.property_item_tvQuantity);
            holder.tvPrice = convertView.findViewById(R.id.property_item_tvPrice);
            holder.tvDiscountPrice = convertView.findViewById(R.id.property_item_tvDiscountPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Property property = list.get(position);
        if (property != null) {
            if (property.getListImage().size() > 0) {
                Picasso.get().load(property.getListImage().get(0)).into(holder.imageView);
            }
            holder.tvName.setText(property.getName());
//            holder.tvType.setText("1 phòng " + property.getTyp());
            holder.tvQuantity.setText("Giá cho 1 đêm, 2 người lớn");
            NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            holder.tvPrice.setText(vndFormat.format(property.getPrice()));
            double discountPrice = property.getPrice() - ((property.getPrice() * property.getDiscount()) / 100);
            holder.tvDiscountPrice.setText(vndFormat.format(discountPrice));
        }
        holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView tvName, tvType, tvQuantity, tvPrice, tvDiscountPrice;
    }
}

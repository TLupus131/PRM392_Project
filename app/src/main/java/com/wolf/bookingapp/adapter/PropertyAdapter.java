package com.wolf.bookingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wolf.bookingapp.R;
import com.wolf.bookingapp.activity.PropertyDetailActivity;
import com.wolf.bookingapp.entity.Property;
import com.wolf.bookingapp.entity.User;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PropertyAdapter extends BaseAdapter {

    private final Context context;
    private List<Property> list;
    private int room, day, adult, children;
    private Date checkInDate, checkOutDate;

    public PropertyAdapter(Context context, List<Property> list, int room, int day, int adult, int children, Date checkInDate, Date checkOutDate) {
        this.context = context;
        this.list = list;
        this.room = room;
        this.day = day;
        this.adult = adult;
        this.children = children;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public List<Property> getList() {
        return list;
    }

    public void setList(List<Property> list) {
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
            holder.recyclerViewIcons = convertView.findViewById(R.id.recycler_view_icons);
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
            holder.tvType.setText(room + " phòng/căn " + property.getType());
            holder.tvQuantity.setText("Giá cho " + day + " đêm, " + adult + " người lớn");
            calculateFinalPrice(holder, property.getPrice(), room, day, adult, property.getDiscount());

            holder.recyclerViewIcons.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerViewIcons.setAdapter(new IconAdapter(context, property.getStar()));
        }

        holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PropertyDetailActivity.class);
                intent.putExtra("property", property);
                intent.putExtra("room", room);
                intent.putExtra("day", day);
                intent.putExtra("adult", adult);
                intent.putExtra("children", children);
                intent.putExtra("checkInDate", checkInDate);
                intent.putExtra("checkOutDate", checkOutDate);
                intent.putExtra("totalPrice", holder.tvPrice.getText());
                intent.putExtra("finalPrice", holder.tvDiscountPrice.getText());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void calculateFinalPrice(ViewHolder holder, double basePrice, int room, int numDays, int numPeople, double discount) {
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        double totalPrice = 0;

        if (numPeople == 1 && numDays == 1) {
            totalPrice = basePrice;
        } else {
            for (int person = 2; person <= numPeople; person++) {
                double priceMultiplier = 1 + (person - 1) * 0.35;
                double pricePerPerson = basePrice * priceMultiplier;
                totalPrice += pricePerPerson;
            }
            for (int night = 2; night <= numDays; night++) {
                double priceMultiplier = 1 + (night - 1) * 0.35;
                double pricePerNight = basePrice * priceMultiplier;
                totalPrice += pricePerNight;
            }
        }

        totalPrice *= room;
        double finalPrice = totalPrice * (1 - discount / 100);

        holder.tvPrice.setText(vndFormat.format(totalPrice));
        holder.tvDiscountPrice.setText(vndFormat.format(finalPrice));
    }

    static class ViewHolder {
        ImageView imageView;
        TextView tvName, tvType, tvQuantity, tvPrice, tvDiscountPrice;
        RecyclerView recyclerViewIcons;
    }
}

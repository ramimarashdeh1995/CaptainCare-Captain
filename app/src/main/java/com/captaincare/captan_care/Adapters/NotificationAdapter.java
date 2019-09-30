package com.captaincare.captan_care.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.captaincare.captan_care.Models.NotificationClass;
import com.captaincare.captan_care.Fragments.Notification.NotificationDetailsFragment;
import com.captaincare.captan_care.R;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.holder> {

    private final Context context;
    private final ArrayList<NotificationClass> notificationList;

    public NotificationAdapter(Context context, ArrayList<NotificationClass> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_notification_layout, null);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.txtUser.setText(notificationList.get(position).getName());
        holder.txtOfferTitle.setText(notificationList.get(position).getTitle());
        holder.offer_time.setText(notificationList.get(position).getDateNotification());

        Glide.with(Objects.requireNonNull(context))
                .load(notificationList.get(position).getImag()).placeholder(R.drawable.ic_profile_default)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.circleImageView);
        String[] hourMin = notificationList.get(position).getTime().split(":");
        int offerHour = Integer.parseInt(hourMin[0]);

        if (offerHour < 0) {
            holder.offer_end.setVisibility(View.VISIBLE);
        } else {
            if (notificationList.get(position).getIsEnd().equals("1")){
                holder.offer_end.setVisibility(View.VISIBLE);
            }else{
                holder.offer_end.setVisibility(View.GONE);
            }

        }

        holder.itemView.setOnClickListener(v -> {
            if (offerHour > 0 && notificationList.get(position).getIsEnd().equals("0")) {
                holder.offer_end.setVisibility(View.GONE);

                AppCompatActivity activity = (AppCompatActivity) context;
                SharedPreferences sharedPreferences = context.getSharedPreferences("ShareNotificationData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("type", notificationList.get(position).getType());
                editor.putString("OfferID", notificationList.get(position).getOfferid());
                editor.putString("userID", notificationList.get(position).getVen_id());
                editor.putString("userName", notificationList.get(position).getName());
                editor.putString("userImg", notificationList.get(position).getImag());
                editor.putString("Address", notificationList.get(position).getAddress());
                editor.putString("lon", notificationList.get(position).getLon());
                editor.putString("lat", notificationList.get(position).getLat());
                editor.putString("cost", notificationList.get(position).getCost());
                editor.putString("hours", notificationList.get(position).getTime());
                editor.apply();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new NotificationDetailsFragment()).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class holder extends RecyclerView.ViewHolder {
        TextView txtUser, txtOfferTitle, offer_end, offer_time;
        CircleImageView circleImageView;
        LinearLayout card_item;

        public holder(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtOfferTitle = itemView.findViewById(R.id.txtOfferTitle);
            circleImageView = itemView.findViewById(R.id.imgUser);
            offer_end = itemView.findViewById(R.id.offer_end);
            offer_time = itemView.findViewById(R.id.offer_time);

            card_item = itemView.findViewById(R.id.card_item);
        }
    }
}

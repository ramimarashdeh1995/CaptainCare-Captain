package com.captaincare.captan_care.Adapters;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Models.MyRequestClass;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.myHolder> {

    private final Context context;
    private final ArrayList<MyRequestClass> myAdsArrayList;

    private AlertDialog.Builder dialogImage, dialogProgressBar, dialogFinishOffer;
    private View viewImage, viewProgressBar;
    private AlertDialog alertProgress;
    private PopupMenu popupMenu;

    public MyRequestAdapter(Context context, ArrayList<MyRequestClass> myAdsArrayList) {
        this.context = context;
        this.myAdsArrayList = myAdsArrayList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_my_request_layout, null);
        return new myHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        MyRequestClass myRequestClass = myAdsArrayList.get(position);


        //TODO : If Offer is End or Not
        if (myRequestClass.getIsEnd().equals("1")) {
            holder.tatFinished.setVisibility(View.VISIBLE);
            holder.btnFinishOffer.setVisibility(View.GONE);

        } else {
            holder.tatFinished.setVisibility(View.GONE);
            holder.btnFinishOffer.setVisibility(View.VISIBLE);
        }



        if (Locale.getDefault().getLanguage().equals("ar")) {
            holder.txtServiceType.setText(myRequestClass.getSubcategoryName_Ar());
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            holder.txtServiceType.setText(myRequestClass.getSubcategoryName_En());
        }

        holder.txtOfferNumber.setText(context.getString(R.string.offer) + " " + (position + 1));
        holder.txtOfferTitle.setText(myRequestClass.getOfferTitle());
        holder.txtOfferDes.setText(myRequestClass.getOfferDescription());
        holder.txtOfferCity.setText(myRequestClass.getCity());
        holder.txtOfferEndTime.setText(myRequestClass.getOfferEndDate());
        holder.costtxt.setText(myRequestClass.getCost());
        holder.txtOfferAddress.setText(myRequestClass.getAddress());

        if (!myRequestClass.getImgUrl1().equals("")) {
            Picasso.with(context).load(myRequestClass.getImgUrl1()).into(holder.img1);
        } else {
            holder.img1.setVisibility(View.GONE);
        }
        if (!myRequestClass.getImgUrl2().equals("")) {
            Picasso.with(context).load(myRequestClass.getImgUrl2()).into(holder.img2);
        } else {
            holder.img2.setVisibility(View.GONE);
        }
        if (!myRequestClass.getImgUrl3().equals("")) {
            Picasso.with(context).load(myRequestClass.getImgUrl3()).into(holder.img3);
        } else {
            holder.img3.setVisibility(View.GONE);
        }
        if (!myRequestClass.getImgUrl4().equals("")) {
            Picasso.with(context).load(myRequestClass.getImgUrl4()).into(holder.img4);
        } else {
            holder.img4.setVisibility(View.GONE);
        }

        holder.img1.setOnClickListener(v -> {
            showFullImage(myRequestClass.getImgUrl1());
        });
        holder.img2.setOnClickListener(v -> {
            showFullImage(myRequestClass.getImgUrl2());
        });
        holder.img3.setOnClickListener(v -> {
            showFullImage(myRequestClass.getImgUrl3());
        });
        holder.img4.setOnClickListener(v -> {
            showFullImage(myRequestClass.getImgUrl4());
        });

        holder.btnFinishOffer.setOnClickListener(v -> {
            dialogFinishOffer = new AlertDialog.Builder(context);
            dialogFinishOffer.setTitle("Are you sure you want to end this Request ?");
            dialogFinishOffer.setIcon(R.drawable.ic_info_gray);
            dialogFinishOffer.setNegativeButton(R.string.cancel, null);
            dialogFinishOffer.setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                dialog.dismiss();
                finishCaptainOffer(myRequestClass.getOfferId(), position , holder);
            });
            dialogFinishOffer.show();
        });
    }

    private void showProgressBar() {
        viewProgressBar = LayoutInflater.from(context).inflate(R.layout.custom_progress_bar, null);
        dialogProgressBar = new AlertDialog.Builder(context);
        dialogProgressBar.setCancelable(false);
        dialogProgressBar.setView(viewProgressBar);
        alertProgress = dialogProgressBar.create();
        Objects.requireNonNull(alertProgress.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertProgress.show();
    }

    private void showFullImage(String imgUrl) {
        viewImage = LayoutInflater.from(context).inflate(R.layout.dialog_show_image, null);
        dialogImage = new AlertDialog.Builder(context);
        dialogImage.setView(viewImage);
        ImageView imageView = viewImage.findViewById(R.id.showImage);
        Picasso.with(context).load(imgUrl).into(imageView);
        dialogImage.show();

    }

    private void finishCaptainOffer(String offerId, int position, myHolder holder) {
        showProgressBar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.finishoffer,
                response -> {
                    try {
                        alertProgress.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            dialogFinishOffer = new AlertDialog.Builder(context);
                            dialogFinishOffer.setTitle("Successful Finished");
                            dialogFinishOffer.setIcon(R.drawable.ic_successfull);
                            dialogFinishOffer.setPositiveButton(R.string.ok, (dialog1, which) -> {
                                dialog1.dismiss();
                                holder.tatFinished.setVisibility(View.VISIBLE);
                                holder.btnFinishOffer.setVisibility(View.GONE);
                            });
                            dialogFinishOffer.show();
                        } else if (jsonObject.getString("error").equals("true")) {
                            dialogFinishOffer = new AlertDialog.Builder(context);
                            dialogFinishOffer.setTitle("Error , Retry Again");
                            dialogFinishOffer.setIcon(R.drawable.ic_error2);
                            dialogFinishOffer.setPositiveButton(R.string.ok, null);
                            dialogFinishOffer.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    alertProgress.dismiss();
                    AppCompatActivity activity = (AppCompatActivity) context;
                    Snackbar.make(activity.findViewById(R.id.frame_layout), R.string.check_internet_connection, Snackbar.LENGTH_INDEFINITE).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("offer_id", offerId);
                return params;
            }
        };

        RequestHand.getInstance(context).addToRequestQueue(stringRequest);

    }

    @Override
    public int getItemCount() {
        return myAdsArrayList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView tatFinished, txtOfferNumber, txtOfferTitle, txtOfferDes,
                txtOfferCity, txtOfferAddress, txtServiceType, txtOfferEndTime,costtxt;
        Button /*btnEdit,*/ btnFinishOffer;
        ImageButton img1, img2, img3, img4;
        LinearLayout linearImage1, linearImage2, linearAddress, linearEmptyOffer;

        CardView cardView;

        public myHolder(@NonNull View itemView) {
            super(itemView);

//            frameLayout = itemView.findViewById(R.id.frame_end_offer);

            linearImage1 = itemView.findViewById(R.id.linearImage1);
            linearImage2 = itemView.findViewById(R.id.linearImage2);
            linearAddress = itemView.findViewById(R.id.linearAddress);
            linearEmptyOffer = itemView.findViewById(R.id.linearRequestEmpty);

            cardView = itemView.findViewById(R.id.cardMyAds);

            txtOfferNumber = itemView.findViewById(R.id.txtOfferNumber);
            txtOfferTitle = itemView.findViewById(R.id.txtOfferTitle);
            txtOfferDes = itemView.findViewById(R.id.txtOfferDes);
            txtOfferCity = itemView.findViewById(R.id.txtOfferCity);
            txtServiceType = itemView.findViewById(R.id.txtServiceType);
            txtOfferEndTime = itemView.findViewById(R.id.txtOfferEndTime);
            txtOfferAddress = itemView.findViewById(R.id.txtOfferAddress);
            costtxt=itemView.findViewById(R.id.costtxt);
            tatFinished =itemView.findViewById(R.id.txtFinshed);

            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            img4 = itemView.findViewById(R.id.img4);

         //   btnEdit = itemView.findViewById(R.id.button_edit);
            btnFinishOffer = itemView.findViewById(R.id.button_finishOffer);

        }
    }
}

     /*  PopupMenu popupMenu = new PopupMenu(context, holder.img2);
            popupMenu.inflate(R.menu.menu_pop_up);
            //registering popup with OnMenuItemClickListener
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.show) {
                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_image, null);
                    dialogImage = new AlertDialog.Builder(context);
                    dialogImage.setView(view);
                    dialogImage.setTitle("gfggfgfgf");
                    ImageView imageView = view.findViewById(R.id.showImage);
                    imageView.setImageResource(R.drawable.ic_phone_profile);
//                    Glide.with(context).load(myAdsClass.getImgUrl2()).into(imageView);
                    dialogImage.show();
                }
                return true;
            });
            popupMenu.show();*/
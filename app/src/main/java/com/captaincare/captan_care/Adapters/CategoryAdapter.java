package com.captaincare.captan_care.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Fragments.OfferFragment;
import com.captaincare.captan_care.Models.CategoryClass;
import com.captaincare.captan_care.Models.SubCategoryClass;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

import static android.content.Context.MODE_PRIVATE;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    private Context context;
    private ArrayList<CategoryClass> arrayListCategoryData;
    private ArrayList<SubCategoryClass> subCategoryClassArrayList;
    private AlertDialog.Builder subCategoryBuilder;
    private SharedPreferences subCategoryPreferences;
    String Language;

    public CategoryAdapter(Context context, ArrayList<CategoryClass> arrayListCategoryData) {
        this.context = context;
        this.arrayListCategoryData = arrayListCategoryData;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.custom_sections_layout, null);
        return new MyHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        Paper.init(context);
        Language = Paper.book().read("language");
        if (Language.equals("ar")) {
            holder.txtSectionName.setText(arrayListCategoryData.get(position).getCategoryName_Ar());
        } else {
            holder.txtSectionName.setText(arrayListCategoryData.get(position).getCategoryName_En());
        }

        Picasso.with(context).load(arrayListCategoryData.get(position).getCategoryimgUrl()).into(holder.imgSection);

        holder.cardView.setOnClickListener(v -> {
            RetrieveSubCategory(arrayListCategoryData.get(position).getCategoryId());
        });
    }

    private void RetrieveSubCategory(String categoryId) {
        subCategoryClassArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConnectionSrever.getService,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            subCategoryClassArrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                SubCategoryClass subCategoryClass = new SubCategoryClass(
                                        object.getString("s_id"),
                                        object.getString("c_id"),
                                        object.getString("sub_name_ar"),
                                        object.getString("sub_name")
                                );
                                subCategoryClassArrayList.add(subCategoryClass);
                            }
                            String[] subCat_AR = new String[subCategoryClassArrayList.size()];
                            String[] subCat_EN = new String[subCategoryClassArrayList.size()];
                            for (int i = 0; i < subCategoryClassArrayList.size(); i++) {
                                subCat_AR[i] = subCategoryClassArrayList.get(i).getSubcategoryName_Ar();
                                subCat_EN[i] = subCategoryClassArrayList.get(i).getSubcategoryName_En();
                            }
                            showSubCategory(subCat_AR, subCat_EN);

                        } else if (jsonObject.getString("error").equals("true")) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("c_id", categoryId);
                return params;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void showSubCategory(String[] subCat_AR, String[] subCat_EN) {
        subCategoryPreferences = context.getSharedPreferences("shareSubCat_ID", MODE_PRIVATE);
        SharedPreferences.Editor editor = subCategoryPreferences.edit();

        AppCompatActivity activity = (AppCompatActivity) context;

        subCategoryBuilder = new AlertDialog.Builder(context, R.style.DialogTheme);
        subCategoryBuilder.setTitle(R.string.choose_service);

        if (Language.equals("ar")) {
            subCategoryBuilder.setItems(subCat_AR, (dialog, which) -> {
                //Go to Offer Page
                editor.putString("key_subCat", subCategoryClassArrayList.get(which).getSubCategoryID());
                editor.apply();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new OfferFragment()).commit();
                dialog.dismiss();
            });
        } else {
            subCategoryBuilder.setItems(subCat_EN, (dialog, which) -> {
                //Go to Offer Page
                editor.putString("key_subCat", subCategoryClassArrayList.get(which).getSubCategoryID());
                editor.apply();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new OfferFragment()).commit();
                dialog.dismiss();
            });
        }
        subCategoryBuilder.show();

    }

    @Override
    public int getItemCount() {
        return arrayListCategoryData.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtSectionName;
        ImageView imgSection;
        CardView cardView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            txtSectionName = itemView.findViewById(R.id.txtSectionsName);
            imgSection = itemView.findViewById(R.id.imgSection);
            cardView = itemView.findViewById(R.id.cardMyAds);

        }
    }
}

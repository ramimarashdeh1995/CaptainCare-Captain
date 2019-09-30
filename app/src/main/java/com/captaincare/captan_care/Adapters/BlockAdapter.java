package com.captaincare.captan_care.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.captaincare.captan_care.Models.FollowAndBlockClass;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.myHolder> {

    private Context context;
    private final ArrayList<FollowAndBlockClass> blockArrayList;

    private AlertDialog.Builder dialogBlock;

    public BlockAdapter(Context context, ArrayList<FollowAndBlockClass> blockArrayList) {
        this.context = context;
        this.blockArrayList = blockArrayList;
    }


    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_block_layout,null);

        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, @SuppressLint("RecyclerView") int position) {
        FollowAndBlockClass blockClass = blockArrayList.get(position);
        holder.txtName.setText(blockClass.getUserName());
        Glide.with(context).load(blockClass.getUserPhoto()).placeholder(R.drawable.ic_profile_default).into(holder.imgPersonal);

        holder.btnUnBlock.setOnClickListener(v -> {
            dialogBlock = new AlertDialog.Builder(context);
            dialogBlock.setTitle("Are you sure want to Un-Block " + blockClass.getUserName());
            dialogBlock.setPositiveButton(R.string.yes, (dialog, which) -> {
                unBlockVendor(blockClass.getBlockId(), position, dialog);
            });
            dialogBlock.setNegativeButton(R.string.cancel, null);
            dialogBlock.show();
        });

    }

    private void unBlockVendor(String blockId, int position, DialogInterface dialog) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.BlockToMarket,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")){
                            blockArrayList.remove(position);
                            notifyItemRemoved(position);
                            dialog.dismiss();
                        }else if (jsonObject.getString("error").equals("true")){

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("b_id", blockId);
                return params;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return blockArrayList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{

        private TextView txtName;
        private Button btnUnBlock;
        private CircleImageView imgPersonal;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            txtName=itemView.findViewById(R.id.txtName);
            btnUnBlock =itemView.findViewById(R.id.button_UnBlock);
            imgPersonal=itemView.findViewById(R.id.imgPersonal);
        }
    }
}

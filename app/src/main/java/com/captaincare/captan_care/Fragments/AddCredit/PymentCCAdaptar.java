package com.captaincare.captan_care.Fragments.AddCredit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.captaincare.captan_care.R;

import java.util.ArrayList;


public class PymentCCAdaptar extends RecyclerView.Adapter<PymentCCAdaptar.myHolder> {


    private Context context;
    private ArrayList<PymentCC>pymentCCS;

    public PymentCCAdaptar(Context context,ArrayList<PymentCC>pymentCCArrayList){
        this.context=context;
        this.pymentCCS=pymentCCArrayList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.custom_cc, null);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {

        PymentCC pymentCC=pymentCCS.get(i);

        myHolder.txtCostCC.setText(pymentCC.getP_Price()+" JD");
        myHolder.txtFreeCC.setText(pymentCC.getP_CC()+" CC Coins");

        myHolder.cardView.setOnClickListener(v->{
            Intent I=new Intent(context, ShopingCC.class);
            I.putExtra("p_id",pymentCC.getP_ID());
            I.putExtra("p_cc",pymentCC.getP_CC());
            I.putExtra("p_price",pymentCC.getP_Price());
            context.startActivity(I);

        });
    }

    @Override
    public int getItemCount() {
        return pymentCCS.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{

        TextView txtFreeCC,txtCostCC;
        CardView cardView;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            txtFreeCC=itemView.findViewById(R.id.txtFreeCC);
            txtCostCC=itemView.findViewById(R.id.txtCostCC);
            cardView=itemView.findViewById(R.id.cardMyCC);
        }
    }
}

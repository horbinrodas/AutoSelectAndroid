package com.umg.vehiculomodel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.umg.autoselect.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Vehiculo> items;
    SelectListener listener;
    public MyAdapter(Context context, List<Vehiculo> items, SelectListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        holder.marcaView.setText(items.get(position).getMarca());
        holder.lineaView.setText(items.get(position).getLinea());
        holder.modeloView.setText(String.valueOf(items.get(position).getModelo()));
        holder.idView.setText(String.valueOf(items.get(position).getId()));
        Glide.with(this.context).load(items.get(position).getImg_link()).into(holder.listImageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(items.get(position));
            }
        });
        holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
        holder.cardView.setCardElevation(0);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
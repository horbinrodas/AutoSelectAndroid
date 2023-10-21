package com.umg.vehiculomodel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.umg.autoselect.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView listImageView;
    TextView idView, marcaView, lineaView, modeloView;
    CardView cardView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        listImageView = itemView.findViewById(R.id.listImageView);
        idView = itemView.findViewById(R.id.idView);
        marcaView = itemView.findViewById(R.id.marcaView);
        lineaView = itemView.findViewById(R.id.lineaView);
        modeloView = itemView.findViewById(R.id.modeloView);
        cardView = itemView.findViewById(R.id.vehiculoView);
    }
}

package com.dev.jackmeraz.androideatit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.jackmeraz.androideatit.Interface.ItemClickListener;
import com.dev.jackmeraz.androideatit.R;

/**
 * Created by jacobo.meraz on 24/11/2017.
 */

public class ComidaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView comida_Name;
    public ImageView comida_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ComidaViewHolder(View itemView) {
        super(itemView);

        comida_Name = (TextView) itemView.findViewById(R.id.comida_name);
        comida_image = (ImageView) itemView.findViewById(R.id.comida_image);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}

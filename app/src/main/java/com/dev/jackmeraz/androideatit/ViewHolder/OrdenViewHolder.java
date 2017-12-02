package com.dev.jackmeraz.androideatit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dev.jackmeraz.androideatit.Interface.ItemClickListener;
import com.dev.jackmeraz.androideatit.R;

/**
 * Created by jacobo.meraz on 01/12/2017.
 */

public class OrdenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrdenId, txtOrdenStatus, txtOrdenTelefono, txtOrdenDireccion;

    private ItemClickListener itemClickListener;

    public OrdenViewHolder(View itemView) {
        super(itemView);

        txtOrdenDireccion = (TextView) itemView.findViewById(R.id.orden_direccion);
        txtOrdenTelefono = (TextView) itemView.findViewById(R.id.orden_telefono);
        txtOrdenStatus = (TextView) itemView.findViewById(R.id.orden_status);
        txtOrdenId = (TextView) itemView.findViewById(R.id.orden_id);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}

package com.dev.jackmeraz.androideatit.ViewHolder;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dev.jackmeraz.androideatit.Interface.ItemClickListener;
import com.dev.jackmeraz.androideatit.Model.Orden;
import com.dev.jackmeraz.androideatit.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jacobo.meraz on 25/11/2017.
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_cart_name, txtPrecio;
    public ImageView img_cart_contador;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
        txtPrecio = (TextView) itemView.findViewById(R.id.cart_item_precio);
        img_cart_contador = (ImageView) itemView.findViewById(R.id.cart_item_contador);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Orden> listDatos = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Orden> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + listDatos.get(position).getCantidad(), Color.RED);
        holder.img_cart_contador.setImageDrawable(drawable);

        Locale locale = new Locale("es", "MX");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int precio = (Integer.parseInt(listDatos.get(position).getPrecio()))*(Integer.parseInt(listDatos.get(position).getCantidad()));
        holder.txtPrecio.setText(fmt.format(precio));

        holder.txt_cart_name.setText(listDatos.get(position).getNom_Producto());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }
}

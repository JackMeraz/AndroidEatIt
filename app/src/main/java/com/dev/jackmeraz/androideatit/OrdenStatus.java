package com.dev.jackmeraz.androideatit;

import android.app.VoiceInteractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dev.jackmeraz.androideatit.Common.Common;
import com.dev.jackmeraz.androideatit.Model.Pedido;
import com.dev.jackmeraz.androideatit.ViewHolder.OrdenViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdenStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Pedido, OrdenViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        pedidos = database.getReference("Pedidos");

        recyclerView = (RecyclerView) findViewById(R.id.listaOrden);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() == null)
            cargarOrdenes(Common.CurrentUser.getTelefono());
        else
            cargarOrdenes(getIntent().getStringExtra("userPhone"));
        cargarOrdenes(Common.CurrentUser.getTelefono());

    }

    private void cargarOrdenes(String telefono) {

        adapter = new FirebaseRecyclerAdapter<Pedido, OrdenViewHolder>(
                Pedido.class,
                R.layout.orden_layout,
                OrdenViewHolder.class,
                pedidos.orderByChild("telefono")
                            .equalTo(telefono)
        ) {
            @Override
            protected void populateViewHolder(OrdenViewHolder ordenViewHolder, Pedido pedido, int i) {

                ordenViewHolder.txtOrdenId.setText(adapter.getRef(i).getKey());
                ordenViewHolder.txtOrdenStatus.setText(Common.convertCodigoAStatus(pedido.getStatus()));
                ordenViewHolder.txtOrdenDireccion.setText(pedido.getDireccion());
                ordenViewHolder.txtOrdenTelefono.setText(pedido.getTelefono());
            }
        };

        recyclerView.setAdapter(adapter);
    }


}

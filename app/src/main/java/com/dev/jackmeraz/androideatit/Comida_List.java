package com.dev.jackmeraz.androideatit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.jackmeraz.androideatit.Interface.ItemClickListener;
import com.dev.jackmeraz.androideatit.Model.Comida;
import com.dev.jackmeraz.androideatit.ViewHolder.ComidaViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Comida_List extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference comidaList;

    String CategoriaId = "";

    FirebaseRecyclerAdapter<Comida, ComidaViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida__list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        comidaList = database.getReference("Comida");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_comida);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Obtiene Intent aqui
        if (getIntent() != null)
            CategoriaId = getIntent().getStringExtra("CategoriaId");
        if (!CategoriaId.isEmpty() && CategoriaId != null)
        {
            loadComidaList (CategoriaId);
        }

    }

    private void loadComidaList (String CategoriaId) {
        adapter = new FirebaseRecyclerAdapter<Comida, ComidaViewHolder>(Comida.class,
                R.layout.comida_item,
                ComidaViewHolder.class,
                comidaList.orderByChild("MenuId").equalTo(CategoriaId) //Select * from Comida where MenuId =
                ) {
            @Override
            protected void populateViewHolder(ComidaViewHolder comidaViewHolder, Comida comida, int i) {

                comidaViewHolder.comida_Name.setText(comida.getName());
                Picasso.with(getBaseContext()).load(comida.getImage())
                        .into(comidaViewHolder.comida_image);

                final Comida local = comida;
                comidaViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int posicion, boolean isLongClick) {
                        Toast.makeText(Comida_List.this, "" + local.getName(), Toast.LENGTH_SHORT).show();
                        //Inicia nuevo activity de Detalles

                        Intent ComidaDetail = new Intent(Comida_List.this, ComidaDetail.class);
                        ComidaDetail.putExtra("ComidaId", adapter.getRef(posicion).getKey()); //Envia ComidaId al nuevo activity
                        startActivity(ComidaDetail);

                    }
                });

            }
        };

        //Configura el Adapter

        recyclerView.setAdapter(adapter);
    }
}

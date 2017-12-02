package com.dev.jackmeraz.androideatit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.jackmeraz.androideatit.Interface.ItemClickListener;
import com.dev.jackmeraz.androideatit.Model.Comida;
import com.dev.jackmeraz.androideatit.ViewHolder.ComidaViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Comida_List extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference comidaList;

    String CategoriaId = "";

    FirebaseRecyclerAdapter<Comida, ComidaViewHolder> adapter;

    //Funcion de Buscar
    FirebaseRecyclerAdapter<Comida, ComidaViewHolder> searchAdapter;
    List<String> ListaSugerencia = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

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

        //Busqueda
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.BarraBusqueda);
        materialSearchBar.setHint("Ingrese su Busqueda");
        //materialSearchBar.setSpeechMode(false); No se necesita, ya esta definido en xml
        loadSugerencia(); //Escribe funcion para cargar sugerencias desde Firebase
        materialSearchBar.setLastSuggestions(ListaSugerencia);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Cuando el usuario escriba su texto, cambiaremos la lista de sugerencias

                List<String> sugerencia = new ArrayList<String>();
                for (String busqueda:ListaSugerencia)
                {
                    if (busqueda.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        sugerencia.add(busqueda);
                }
                materialSearchBar.setLastSuggestions(sugerencia);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean b) {
                //Cuando la barra de busqueda se Cierra
                //Restaura el adaptador original
                if (!b)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence charSequence) {
                //Cuando finaliza la busqueda
                //muestra resultado en el adaptador de sugerencias
                startSearch(charSequence);

            }

            @Override
            public void onButtonClicked(int i) {

            }
        });

    }

    private void startSearch(CharSequence charSequence) {
        searchAdapter = new FirebaseRecyclerAdapter<Comida, ComidaViewHolder>(
                Comida.class,
                R.layout.comida_item,
                ComidaViewHolder.class,
                comidaList.orderByChild("Name").equalTo(charSequence.toString())//Compara Nombre
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
                        ComidaDetail.putExtra("ComidaId", searchAdapter.getRef(posicion).getKey()); //Envia ComidaId al nuevo activity
                        startActivity(ComidaDetail);

                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter); //Establece adapter para  Recycler View en Resultados de Busqueda
    }

    private void loadSugerencia() {
        comidaList.orderByChild("MenuId").equalTo(CategoriaId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            Comida item = postSnapshot.getValue(Comida.class);
                            ListaSugerencia.add(item.getName()); //Agrega nombre de la comida a la lista de sugerencias
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

package com.dev.jackmeraz.androideatit;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dev.jackmeraz.androideatit.Model.Comida;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.dev.jackmeraz.androideatit.R.style.ExpandedAppbar;

public class ComidaDetail extends AppCompatActivity {

    TextView Nombre_comida, precio_comida, Descrip_comida;
    ImageView Comida_Image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String ComidaId = "";

    FirebaseDatabase database;
    DatabaseReference comida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        comida = database.getReference("Comida");

        //Iniciar View
        numberButton = (ElegantNumberButton) findViewById(R.id.BtnNumero);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        Descrip_comida = (TextView) findViewById(R.id.Descrip_comida);
        Nombre_comida = (TextView) findViewById(R.id.nombre_comida);
        precio_comida = (TextView) findViewById(R.id.precio_comida);
        Comida_Image = (ImageView) findViewById(R.id.img_comida);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.desplazo);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Obtiene ComidaId del Intent
        if (getIntent() !=null)
            ComidaId = getIntent().getStringExtra("ComidaId");
        if (!ComidaId.isEmpty())
        {
            getDetailComida (ComidaId);
        }

    }

    private void getDetailComida(String comidaId) {

        comida.child(ComidaId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Comida comida = dataSnapshot.getValue(Comida.class);

                //Establece Imagen
                Picasso.with(getBaseContext()).load(comida.getImage())
                        .into(Comida_Image);

                collapsingToolbarLayout.setTitle(comida.getName());

                precio_comida.setText(comida.getPrecio());

                Nombre_comida.setText(comida.getName());

                Descrip_comida.setText(comida.getDescripcion());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

package com.dev.jackmeraz.androideatit;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.jackmeraz.androideatit.Common.Common;
import com.dev.jackmeraz.androideatit.Database.Database;
import com.dev.jackmeraz.androideatit.Model.Orden;
import com.dev.jackmeraz.androideatit.Model.Pedido;
import com.dev.jackmeraz.androideatit.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Carrito extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference pedidos;

    TextView txtPrecioTotal;
    FButton btnPedido;

    List<Orden> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        //Firebase
        database = FirebaseDatabase.getInstance();
        pedidos = database.getReference("Pedidos");

        //Iniciar
        recyclerView = (RecyclerView) findViewById(R.id.ListCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtPrecioTotal = (TextView) findViewById(R.id.Total);
        btnPedido = (FButton) findViewById(R.id.btnPedido);

        btnPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();

            }
        });

        loadListaComida();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Carrito.this);
        alertDialog.setTitle("Un paso más!!!");
        alertDialog.setMessage("Ingrese su Dirección: ");

        final EditText editDireccion = new EditText(Carrito.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editDireccion.setLayoutParams(lp);
        alertDialog.setView(editDireccion); //Agrega EditText a alerta
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Se crea nuevo Pedido
                Pedido pedido = new Pedido(
                        Common.CurrentUser.getTelefono(),
                        Common.CurrentUser.getName(),
                        editDireccion.getText().toString(),
                        txtPrecioTotal.getText().toString(),
                        cart
                );

                //Envia a Firebase
                //Se hace usando System.CurrentMilli para la Key
                pedidos.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(pedido);

                //Borrar Carrito
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Carrito.this, "Gracias, Su Pedido se ha Realizado Exitosamente!!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadListaComida() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //Calcula precio total
        int total = 0;
        for (Orden order:cart){
           total = total + ((Integer.parseInt(order.getPrecio())) * (Integer.parseInt(order.getCantidad())));
        }
        Locale local = new Locale("es", "MX");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(local);

        txtPrecioTotal.setText(fmt.format(total));

    }
}

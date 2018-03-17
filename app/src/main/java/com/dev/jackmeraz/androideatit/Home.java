package com.dev.jackmeraz.androideatit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.jackmeraz.androideatit.Common.Common;
import com.dev.jackmeraz.androideatit.Interface.ItemClickListener;
import com.dev.jackmeraz.androideatit.Model.Categoria;
import com.dev.jackmeraz.androideatit.Service.ListenOrdenes;
import com.dev.jackmeraz.androideatit.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference categoria;

    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter <Categoria, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Iniciar Firebase
        database = FirebaseDatabase.getInstance();
        categoria = database.getReference("Categorias");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Agrega elementos al carrito
                Intent cartIntent = new Intent(Home.this, Carrito.class);
                startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Establece nombre para usuario
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView) headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.CurrentUser.getName());

        //Cargar Menu
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();


        //Registrar Servicio
        Intent servicio = new Intent(Home.this, ListenOrdenes.class);
        startService(servicio);

    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Categoria, MenuViewHolder>(Categoria.class, R.layout.menu_item,MenuViewHolder.class,categoria) {
            @Override
            protected void populateViewHolder(MenuViewHolder ViewHolder, Categoria categoria, int i) {
                ViewHolder.txtMenuName.setText(categoria.getName());
                Picasso.with(getBaseContext()).load(categoria.getImage())
                        .into(ViewHolder.imageView);
                final Categoria clickItem = categoria;
                ViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int posicion, boolean isLongClick) {
                        //Obtiene CategoriaId y lo envia al nuevo activity Comida
                        Intent ComidaList = new Intent(Home.this, Comida_List.class);
                        //Porque CategoryId es la clave, así que solo obtenemos la clave de este elemento
                        ComidaList.putExtra("CategoriaId", adapter.getRef(posicion).getKey());
                        startActivity(ComidaList);

                    }
                });


            }
        };

        recycler_menu.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(Home.this,Carrito.class);
            startActivity(cartIntent);
        } else if (id == R.id.nav_orden) {
            Intent ordenStatusIntent = new Intent(Home.this,OrdenStatus.class);
            startActivity(ordenStatusIntent);
        } else if (id == R.id.nav_LogOut) {
            //LogOut
            Intent SingIn = new Intent(Home.this, Singin.class);
            SingIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(SingIn);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

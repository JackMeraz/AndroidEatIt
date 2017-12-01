package com.dev.jackmeraz.androideatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.jackmeraz.androideatit.Common.Common;
import com.dev.jackmeraz.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Singin extends AppCompatActivity {
    EditText txtphone, txtpass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        txtpass = (MaterialEditText) findViewById(R.id.txtpass);
        txtphone = (MaterialEditText) findViewById(R.id.txtphone);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //Iniciar Firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(Singin.this);
                mDialog.setMessage("Por Favor Espere...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Revisa si Usuario no existe en la BD

                        if (dataSnapshot.child(txtphone.getText().toString()).exists()) {

                            //Obtener Informacion del Usuario
                            mDialog.dismiss();
                            User user = dataSnapshot.child(txtphone.getText().toString()).getValue(User.class);
                            user.setTelefono(txtphone.getText().toString()); //Establece Telefono
                            if (user.getPassword().equals(txtpass.getText().toString())) {
                                Toast.makeText(Singin.this, "Inicio de Sesion Exitoso !", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(Singin.this, Home.class);
                                Common.CurrentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(Singin.this, "Error de Contraseña!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(Singin.this, "Usuano no Existe en la BD", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}

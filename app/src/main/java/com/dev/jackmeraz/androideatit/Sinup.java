package com.dev.jackmeraz.androideatit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dev.jackmeraz.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Sinup extends AppCompatActivity {

    MaterialEditText txtphone, txtname, txtpass;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinup);

        txtname =(MaterialEditText) findViewById(R.id.txtname);
        txtpass =(MaterialEditText) findViewById(R.id.txtpass);
        txtphone =(MaterialEditText) findViewById(R.id.txtphone);

        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        //Iniciar Firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(Sinup.this);
                mDialog.setMessage("Por Favor Espere...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Revisa si existe Telefono del Usuario

                        if (dataSnapshot.child(txtphone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(Sinup.this, "Numero de Telefono ya registrado", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            mDialog.dismiss();
                            User user = new User(txtname.getText().toString(), txtpass.getText().toString());
                            table_user.child(txtphone.getText().toString()).setValue(user);
                            Toast.makeText(Sinup.this, "Registrado Exitosamente !!!", Toast.LENGTH_SHORT).show();
                            finish();
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

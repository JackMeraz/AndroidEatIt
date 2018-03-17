package com.dev.jackmeraz.androideatit.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.dev.jackmeraz.androideatit.Common.Common;
import com.dev.jackmeraz.androideatit.Model.Orden;
import com.dev.jackmeraz.androideatit.Model.Pedido;
import com.dev.jackmeraz.androideatit.OrdenStatus;
import com.dev.jackmeraz.androideatit.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOrdenes extends Service implements ChildEventListener {

    FirebaseDatabase db;
    DatabaseReference pedidos;

    public ListenOrdenes() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        pedidos = db.getReference("Pedidos");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pedidos.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //Disparador aqui
        Pedido pedido = dataSnapshot.getValue(Pedido.class);
        showNotificacion(dataSnapshot.getKey(),pedido);

    }

    private void showNotificacion(String key, Pedido pedido) {
        Intent intent = new Intent(getBaseContext(), OrdenStatus.class);
        intent.putExtra("userPhone", pedido.getTelefono()); //
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("TICS Despresurizado")
                .setContentInfo("Su orden ha sido actualizada!!!")
                .setContentText("Orden #" + key + " a cambiado su estatus a " + Common.convertCodigoAStatus(pedido.getStatus()))
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

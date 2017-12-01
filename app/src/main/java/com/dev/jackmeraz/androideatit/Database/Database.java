package com.dev.jackmeraz.androideatit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.dev.jackmeraz.androideatit.Model.Orden;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacobo.meraz on 25/11/2017.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_Name = "LocalBD.db";
    private static final int DB_Ver = 1;
    public Database(Context context) {
        super(context, DB_Name, null, DB_Ver);
    }

    public List<Orden> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"Nom_Producto", "Id_Producto", "Cantidad", "Precio", "Descuento"};
        String sqlTable = "OrdenDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Orden> result = new ArrayList<>();
        if (c.moveToFirst())
        {
            do {
                result.add(new Orden(c.getString(c.getColumnIndex("Id_Producto")),
                c.getString(c.getColumnIndex("Nom_Producto")),
                c.getString(c.getColumnIndex("Cantidad")),
                c.getString(c.getColumnIndex("Precio")),
                c.getString(c.getColumnIndex("Descuento"))
                ));
            } while (c.moveToNext());
        }

        return result;
    }

    public void addCart (Orden orden)
    {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format("INSERT INTO OrdenDetail (Id_producto, Nom_Producto, Cantidad, Precio, Descuento) VALUES ('%s', '%s', '%s', '%s', '%s');",
                orden.getId_Producto(),
                orden.getNom_Producto(),
                orden.getCantidad(),
                orden.getPrecio(),
                orden.getDescuento());
        db.execSQL(Query);
    }

    public void cleanCart ()
    {
        SQLiteDatabase db = getReadableDatabase();
        String Query = String.format("DELETE FROM OrdenDetail");
        db.execSQL(Query);
    }
}

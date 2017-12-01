package com.dev.jackmeraz.androideatit.Model;

import java.util.List;

/**
 * Created by jacobo.meraz on 25/11/2017.
 */

public class Pedido {
    private String Telefono, Nombre, Direccion, Total;
    private List<Orden> comida; //lista de comida en la orden

    public Pedido() {
    }

    public Pedido(String telefono, String nombre, String direccion, String total, List<Orden> comida) {
        Telefono = telefono;
        Nombre = nombre;
        Direccion = direccion;
        Total = total;
        this.comida = comida;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public List<Orden> getComida() {
        return comida;
    }

    public void setComida(List<Orden> comida) {
        this.comida = comida;
    }
}

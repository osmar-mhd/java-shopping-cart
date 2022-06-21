package pkg3cv18_p1_vioratolozadaosmar;

import java.io.Serializable;

public class Producto implements Serializable {
    
    //nombre, precio, descripción, existencia
    private int idProducto;
    private String nombre;
    private float precio;
    private String info;
    private int disponibles;

    public Producto() {
    }
    //Vectores
    public Producto(int idProducto, String nombre, int disponibles, float precio, String info) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.info = info;
        this.disponibles = disponibles;
    }

    //Metodos Get
    public int getidProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }
    
    public String getInfo() {
        return info;
    }

    public float getPrecio() {
        return precio;
    }
    
    public int getDisponibles() {
        return disponibles;
    }
    
    //Metodos Set
    public void setidProductos(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
    
    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    //String Builder necesario para las consultas Select y readAll
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(idProducto);
        return sb.toString();
    }
}
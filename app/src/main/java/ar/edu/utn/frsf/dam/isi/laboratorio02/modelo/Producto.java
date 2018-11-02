package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import java.util.Objects;

public class Producto {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Double precio;
    //private Categoria categoria;
    private int categoriaId;

    public Producto() {}

    public Producto(String nombre, String descripcion, Double precio, int categoriaId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        //this.categoria = categoria;
        this.categoriaId = categoriaId;
    }

    public Producto(String nombre, Double precio, int categoriaId) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoriaId = categoriaId;
    }

    public Producto(Integer id, String nombre, String descripcion, Double precio, int categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaId = categoriaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getCategoriaId() { return categoriaId; }

    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }

    /*public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }*/


    @Override
    public String toString() {
        return nombre+ "( $" + precio +")";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}

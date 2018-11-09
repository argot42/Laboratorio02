package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class PedidoDetalle {

    //private static int ID_DETALLE =1;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private Integer cantidad;
    @Embedded(prefix = "prod_")
    private Producto producto;
    //@Embedded(prefix = "ped_")
    //private Pedido pedido;

    private long idPedidoAsignado;

    public PedidoDetalle() {

    }

    @Ignore
    public PedidoDetalle(Integer cantidad, Producto producto) {
        //id=ID_DETALLE++;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setIdPedidoAsignado(long idPedidoAsignado) {
        this.idPedidoAsignado = idPedidoAsignado;
    }

    public long getIdPedidoAsignado() {
        return idPedidoAsignado;
    }

    /*public Pedido getPedido() {
        return pedido;
    }*/

    /*public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        pedido.agregarDetalle(this);
    }*/

    @Override
    public String toString() {
        return producto.getNombre() + "( $"+producto.getPrecio()+")"+ cantidad;
    }
}

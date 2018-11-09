package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class PedidoConDetalles {

    @Embedded
    public Pedido pedido;

    @Relation(parentColumn = "id", entityColumn = "idPedidoAsignado", entity = PedidoDetalle.class)
    public List<PedidoDetalle> detalle;

    @Override
    public String toString() {
        return "PedidoConDetalles -> [" + pedido.toString() + "] -> " + detalle.toString();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public List<PedidoDetalle> getDetalle() {
        return detalle;
    }
}

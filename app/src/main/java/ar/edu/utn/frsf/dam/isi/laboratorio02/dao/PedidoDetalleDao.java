package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

@Dao
public interface PedidoDetalleDao {

    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Insert
    int insert(PedidoDetalle pd);

    @Delete
    void delete(PedidoDetalle pd);

    @Query("SELECT * FROM PedidoDetalle WHERE id = :idPedidoDetalle")
    PedidoDetalle buscarPedidoDetallePorId(int idPedidoDetalle);
}

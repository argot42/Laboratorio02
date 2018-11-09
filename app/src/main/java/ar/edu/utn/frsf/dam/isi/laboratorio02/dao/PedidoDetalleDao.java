package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

@Dao
public interface PedidoDetalleDao {

    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(PedidoDetalle pd);

    @Update
    void update(PedidoDetalle pd);

    @Delete
    void delete(PedidoDetalle pd);

    @Query("SELECT * FROM PedidoDetalle WHERE id = :idPedidoDetalle")
    PedidoDetalle buscarPedidoDetallePorId(long idPedidoDetalle);
}

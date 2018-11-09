package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;

@Dao
public interface PedidoDao {

    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Update
    void update(Pedido p);

    @Insert
    long insert(Pedido p);

    @Delete
    void delete(Pedido p);

    @Query("SELECT * FROM Pedido WHERE id = :idPedido")
    Pedido buscarPedidoPorId(long idPedido);

    @Transaction
    @Query("SELECT * FROM Pedido")
    List<PedidoConDetalles> getAllConDetalles();

    @Transaction
    @Query("SELECT * FROM Pedido WHERE id = :idPedido")
    PedidoConDetalles buscarPedidoPorIdConDetalles(long idPedido);
}

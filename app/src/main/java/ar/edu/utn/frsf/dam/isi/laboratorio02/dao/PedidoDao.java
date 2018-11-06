package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

@Dao
public interface PedidoDao {

    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Insert
    long insert(Pedido p);

    @Delete
    void delete(Pedido p);

    @Query("SELECT * FROM Pedido WHERE id = :idPedido")
    Pedido buscarPedidoPorId(int idPedido);
}

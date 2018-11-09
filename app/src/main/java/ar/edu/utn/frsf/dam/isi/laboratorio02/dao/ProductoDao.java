package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

@Dao
public interface ProductoDao {

    @Query("SELECT * FROM Producto")
    List<Producto> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Producto p);

    @Delete
    void delete(Producto p);

    @Query("SELECT * FROM Producto WHERE id = :idProducto")
    Producto buscarProductoPorId(long idProducto);

    @Query("SELECT * FROM Producto WHERE cat_id = :idCategoria")
    List<Producto> buscarProductosPorCategoria(long idCategoria);

    @Query("DELETE FROM Producto WHERE id = :productoId")
    void deletePorId(long productoId);
}

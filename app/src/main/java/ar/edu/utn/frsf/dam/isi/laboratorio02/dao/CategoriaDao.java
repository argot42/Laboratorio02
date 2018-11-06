package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

@Dao
public interface CategoriaDao {

    @Query("SELECT * FROM Categoria")
    List<Categoria> getAll();

    @Insert
    int insert(Categoria c);

    @Delete
    void delete(Categoria c);

    @Query("SELECT * FROM Categoria WHERE id = :idCategoria")
    Categoria buscarCategoriaPorId(int idCategoria);
}

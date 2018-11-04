package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

@Database(entities = {Categoria.class}, version = 1)
public abstract class LabDatabase extends RoomDatabase {
    public abstract CategoriaDao categoriaDao();

    private static volatile LabDatabase INSTANCE;

    public static LabDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LabDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LabDatabase.class,
                            "lab_database"
                    ).build();
                }
            }
        }

        return INSTANCE;
    }
}
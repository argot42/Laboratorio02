package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.app.Application;

public class LabRepository {

    private CategoriaDao mCategoriaDao;

    LabRepository(Application application) {
        LabDatabase db = LabDatabase.getDatabase(application);
        mCategoriaDao = db.categoriaDao();
    }
}

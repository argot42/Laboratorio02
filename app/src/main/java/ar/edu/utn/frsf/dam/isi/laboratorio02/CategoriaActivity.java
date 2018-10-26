package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.CategoriaRest;

public class CategoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        final EditText edtNombreCategoria = (EditText) findViewById(R.id.edtNombreCategoria);
        Button btnCrearCategoria = (Button) findViewById(R.id.btnCrearCategoria);
        Button btnCategoriaVolver = (Button) findViewById(R.id.btnCategoriaVolver);

        btnCrearCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombreCategoria = edtNombreCategoria.getText().toString();
                if (nombreCategoria.equals("")) return;
                edtNombreCategoria.setText("");

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        CategoriaRest.crearCategoria(new Categoria(nombreCategoria));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        CategoriaActivity.this,
                                        String.format("Categoría [%s] creada!", nombreCategoria),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
                    }
                };

                Thread t = new Thread(r);
                t.setPriority(Thread.MIN_PRIORITY);
                t.start();
            }
        });

        btnCategoriaVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

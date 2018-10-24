package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CategoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        EditText edtNombreCategoria = (EditText) findViewById(R.id.edtNombreCategoria);
        Button btnCrearCategoria = (Button) findViewById(R.id.btnCrearCategoria);
        Button btnCategoriaVolver = (Button) findViewById(R.id.btnCategoriaVolver);

        btnCrearCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
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

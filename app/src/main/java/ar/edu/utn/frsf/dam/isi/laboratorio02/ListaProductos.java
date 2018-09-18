package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ListaProductos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        final ProductoRepository productoRespository = new ProductoRepository();

        // widgets
        // spinner con categorías de productos
        final Spinner cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        cmbProductosCategoria.setAdapter(new ArrayAdapter<Categoria>(ListaProductos.this, android.R.layout.simple_spinner_dropdown_item, productoRespository.getCategorias()));

        // actualizar lista de productos al cambiar opción en spinner
        final RecyclerView lstProductos = (RecyclerView) findViewById(R.id.lstProductos);
        /*cmbProductosCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria categoria = (Categoria) cmbProductosCategoria.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }
}

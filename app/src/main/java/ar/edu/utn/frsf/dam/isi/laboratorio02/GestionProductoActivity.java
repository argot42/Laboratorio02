package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.LabDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class GestionProductoActivity extends AppCompatActivity {
    private ToggleButton tgGestionProdCrearProdNuevo;
    private EditText edtGestionProdBuscarPorId;
    private Button btnGestionProdBuscarPorId;
    private EditText edtGestionProdNombre;
    private EditText edtGestionProdDescripcion;
    private EditText edtGestionProdPrecio;
    private Spinner spGestionProdSpinner;
    private Button btnGestionProdGuardar;
    private Button btnGestionProdBorrar;
    private Button btnGestionProdVolver;

    private ArrayAdapter<Categoria> categoriaAdapter;

    private Boolean actualizacion = false;
    private long prod_id = -1;
    private Categoria categoria_seleccionada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);

        // setup spinner de productos
        spGestionProdSpinner = (Spinner) findViewById(R.id.spGestionProdSpinner);

        // database
        final LabDatabase lb = LabDatabase.getDatabase(GestionProductoActivity.this);

        // cargar categoria spinner
        Runnable r = new Runnable() {
            @Override
            public void run() {
                final Categoria[] cats = lb.categoriaDao().getAll().toArray(new Categoria[0]);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        categoriaAdapter = new ArrayAdapter<Categoria>(
                                GestionProductoActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                cats
                        );
                        spGestionProdSpinner.setAdapter(categoriaAdapter);
                        spGestionProdSpinner.setSelection(0);
                    }
                });
            }
        };

        spGestionProdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria_seleccionada = (Categoria) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Thread t = new Thread(r);
        t.start();

        // setup busqueda de producto por ID
        edtGestionProdBuscarPorId = (EditText) findViewById(R.id.edtGestionProdBuscarPorId);
        btnGestionProdBuscarPorId = (Button) findViewById(R.id.btnGestionProdBuscarPorId);
        btnGestionProdBorrar = (Button) findViewById(R.id.btnGestionProdBorrar);
        edtGestionProdNombre = (EditText) findViewById(R.id.edtGestionProdNombre);
        edtGestionProdDescripcion = (EditText) findViewById(R.id.edtGestionProdDescripcion);
        edtGestionProdPrecio = (EditText) findViewById(R.id.edtGestionProdPrecio);
        btnGestionProdVolver = (Button) findViewById(R.id.btnGestionProdVolver);
        tgGestionProdCrearProdNuevo = (ToggleButton) findViewById(R.id.tgGestionProdCrearProdNuevo);
        btnGestionProdGuardar = (Button) findViewById(R.id.btnGestionProdGuardar);

        tgGestionProdCrearProdNuevo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                actualizacion = isChecked;
                edtGestionProdBuscarPorId.setEnabled(isChecked);
                btnGestionProdBuscarPorId.setEnabled(isChecked);
                btnGestionProdBorrar.setEnabled(isChecked);
            }
        });

        btnGestionProdBuscarPorId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        prod_id = Long.valueOf(edtGestionProdBuscarPorId.getText().toString());

                        final Producto producto = lb.productoDao().buscarProductoPorId(prod_id);
                        if (producto == null) {
                            Log.e("LAB_04", "No existe producto con este id");
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edtGestionProdNombre.setText(producto.getNombre());
                                edtGestionProdDescripcion.setText(producto.getDescripcion());
                                edtGestionProdPrecio.setText(String.valueOf(producto.getPrecio()));
                                spGestionProdSpinner.setSelection(categoriaAdapter.getPosition(producto.getCategoria()));
                            }
                        });
                    }
                };

                Thread hiloBuscarPorId = new Thread(r);
                hiloBuscarPorId.start();
            }
        });

        // guardar producto
        btnGestionProdGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String prod_nombre = edtGestionProdNombre.getText().toString();
                Log.d("LAB_04", prod_nombre);
                if (prod_nombre.equals("")) { return; }

                String prod_descripcion = edtGestionProdDescripcion.getText().toString();
                Log.d("LAB_04", prod_descripcion);
                if (prod_descripcion.equals("")) { return; }

                Double prod_precio = Double.valueOf(edtGestionProdPrecio.getText().toString());
                Log.d("LAB_04", String.valueOf(prod_precio));
                if (prod_precio < 0.0) { return; }

                final Producto p = new Producto(prod_nombre, prod_descripcion, prod_precio, categoria_seleccionada);

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        //if (actualizacion) {
                        //    Log.d("LAB_04", "actualizacion");
                        //    return;
                        //}

                        if (actualizacion) {
                            p.setId(prod_id);
                        }

                        long id = lb.productoDao().insert(p);

                        if (id < 0) { Log.e("LAB_04", String.format("Error al crear el producto [%s]", p.getNombre())); }
                        else { Log.d("LAB_04", String.format("Producto [%s] creado con id [%d]", p.getNombre(), id)); }
                    }
                };

                Thread hiloCrearUsuario = new Thread(r);
                hiloCrearUsuario.start();
            }
        });

        // borrar producto
        btnGestionProdBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        lb.productoDao().deletePorId(prod_id);
                    }
                };

                Thread hiloBorrarUsuario = new Thread(r);
                hiloBorrarUsuario.start();
            }
        });

        // volver
        btnGestionProdVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

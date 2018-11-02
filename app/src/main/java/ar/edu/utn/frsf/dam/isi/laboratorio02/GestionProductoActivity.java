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

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.CategoriaRest;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    //private Categoria prod_categoria = null;
    private int categoria_position = -1;
    private Boolean actualizacion = false;
    private int prod_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);

        // setup spinner de productos
        spGestionProdSpinner = (Spinner) findViewById(R.id.spGestionProdSpinner);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                CategoriaRest catRest = new CategoriaRest();
                final Categoria[] cats = catRest.listarTodas().toArray(new Categoria[0]);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        categoriaAdapter = new ArrayAdapter<Categoria>(GestionProductoActivity.this, android.R.layout.simple_spinner_dropdown_item, cats);
                        spGestionProdSpinner.setAdapter(categoriaAdapter);
                        spGestionProdSpinner.setSelection(0);
                    }
                });
            }
        };

        spGestionProdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //prod_categoria = (Categoria) parent.getItemAtPosition(position);
                categoria_position = position;
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
                prod_id = Integer.valueOf(edtGestionProdBuscarPorId.getText().toString());

                ProductoRetrofit clienteRest = RestClient.getInstance()
                        .getRetrofit()
                        .create(ProductoRetrofit.class);

                Call<Producto> busquedaCall = clienteRest.buscarProductoId(prod_id);

                busquedaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        Producto producto = response.body();
                        if (producto == null) {
                            Log.e("LAB_04", "No existe producto con este id");
                            return;
                        }

                        edtGestionProdNombre.setText(producto.getNombre());
                        edtGestionProdDescripcion.setText(producto.getDescripcion());
                        edtGestionProdPrecio.setText(String.valueOf(producto.getPrecio()));

                        categoria_position = categoriaAdapter.getPosition(producto.getCategoria());
                        spGestionProdSpinner.setSelection(categoria_position);
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Log.e("LAB_04", call.toString());
                        Log.e("LAB_04", t.toString());
                    }
                });
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

                Categoria prod_categoria = (Categoria) spGestionProdSpinner.getItemAtPosition(categoria_position);

                Producto p = new Producto(prod_nombre, prod_descripcion, prod_precio, prod_categoria);

                ProductoRetrofit clienteRest = RestClient.getInstance()
                        .getRetrofit()
                        .create(ProductoRetrofit.class);

                if (actualizacion) {

                    Call<Producto> actualizacionCall = clienteRest.actualizarProducto(prod_id, p);

                    actualizacionCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> response) {
                            Log.d("LAB_04", call.toString());
                            Log.d("LAB_04", response.toString());
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Log.e("LAB_04", call.toString());
                            Log.e("LAB_04", t.toString());
                        }
                    });

                } else {

                    Call<Producto> altaCall = clienteRest.crearProducto(p);

                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> response) {
                            Log.d("LAB_04", call.toString());
                            Log.d("LAB_04", response.toString());
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Log.e("LAB_04", call.toString());
                            Log.e("LAB_04", t.toString());
                        }
                    });
                }
            }
        });

        // borrar producto
        btnGestionProdBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductoRetrofit clienteRest = RestClient.getInstance()
                        .getRetrofit()
                        .create(ProductoRetrofit.class);

                Call<Producto> borrarCall = clienteRest.borrar(prod_id);

                borrarCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        Log.d("LAB_04", call.toString());
                        Log.d("LAB_04", response.toString());
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Log.e("LAB_04", call.toString());
                        Log.e("LAB_04", t.toString());
                    }
                });
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

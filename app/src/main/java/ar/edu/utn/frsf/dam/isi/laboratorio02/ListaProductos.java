package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ListaProductos extends AppCompatActivity {

    private List<Producto> productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        // esto será ejecutado en segundo plano
       /* Runnable r = new Runnable() {
            @Override
            public void run() {
                CategoriaRest catRest = new CategoriaRest();
                final Categoria[] cats = catRest.listarTodas().toArray(new Categoria[0]);

                ProductoRetrofit clienteRest = RestClient.getInstance()
                        .getRetrofit()
                        .create(ProductoRetrofit.class);

                Call<List<Producto>> listarCall = clienteRest.listarProductos();

                try {
                    productos = listarCall.execute().body();
                } catch (IOException e) {
                    Log.e("LAB_04", String.format("%s: problemas comunicandose con el servidor", e.toString()));
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // setup spinner
                        final Spinner cmbProductoCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
                        cmbProductoCategoria.setAdapter(new ArrayAdapter<Categoria>(ListaProductos.this, android.R.layout.simple_spinner_dropdown_item, cats));
                        cmbProductoCategoria.setSelection(0);

                        final ProductoAdapter productoAdapter = new ProductoAdapter(buscarPorCategoria(productos, cats[0]));

                        cmbProductoCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                productoAdapter.setProductoList(buscarPorCategoria(productos, cats[position]));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        // setup recyclerview
                        final RecyclerView lstProductos = (RecyclerView) findViewById(R.id.lstProductos);
                        lstProductos.setHasFixedSize(true);
                        lstProductos.setLayoutManager(new LinearLayoutManager(ListaProductos.this));
                        // decoración
                        lstProductos.addItemDecoration(new VerticalSpaceItemDecoration(48));
                        lstProductos.addItemDecoration(new DividerItemDecoration(ListaProductos.this));
                        // adapter
                        lstProductos.setAdapter(productoAdapter);

                        // chequear si la actividad padre genero un intent con un extra NUEVO_PEDIDO = 1
                        // si es true habilitar edittext, button y setear clicklistener
                        if (getIntent().getIntExtra("NUEVO_PEDIDO", 0) == 1) {
                            final EditText edtProductoCantidad = (EditText) findViewById(R.id.edtProdCantidad);
                            final Button btnProdAddPedido = (Button) findViewById(R.id.btnProdAddPedido);

                            edtProductoCantidad.setEnabled(true);
                            btnProdAddPedido.setEnabled(true);

                            btnProdAddPedido.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Producto prod = productoAdapter.getItem();
                                    Intent output = new Intent();

                                    if (prod == null) {
                                        setResult(Activity.RESULT_CANCELED, output);

                                    } else {
                                        output.putExtra("idProducto", prod.getId());
                                        output.putExtra("cantidad", Integer.valueOf(edtProductoCantidad.getText().toString()));
                                        setResult(Activity.RESULT_OK, output);
                                    }

                                    finish();
                                }
                            });
                        }
                    }
                });
            }
        };

        Thread hiloCargarCombo = new Thread(r);
        hiloCargarCombo.start();*/
    }

    private List<Producto> buscarPorCategoria (List<Producto> prod, Categoria c) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p:prod) {
            if (p.getCategoriaId() == c.getId()) resultado.add(p);
        }
        return resultado;
    }
}

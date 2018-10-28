package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ar.edu.utn.frsf.dam.isi.laboratorio02.adapter.ProductoAdapter;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.decoration.DividerItemDecoration;
import ar.edu.utn.frsf.dam.isi.laboratorio02.decoration.VerticalSpaceItemDecoration;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.CategoriaRest;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ListaProductos extends AppCompatActivity {

    static final int LISTAPRODUCTO_REQUEST = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        final ProductoRepository productoRepo = new ProductoRepository();
        int idProductoSel = 0;

        // esto será ejecutado en segundo plano
        Runnable r = new Runnable() {
            @Override
            public void run() {
                CategoriaRest catRest = new CategoriaRest();
                final Categoria[] cats = catRest.listarTodas().toArray(new Categoria[0]);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // setup spinner
                        final Spinner cmbProductoCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
                        cmbProductoCategoria.setAdapter(new ArrayAdapter<Categoria>(ListaProductos.this, android.R.layout.simple_spinner_dropdown_item, cats));
                        cmbProductoCategoria.setSelection(0);

                        final ProductoAdapter productoAdapter = new ProductoAdapter(productoRepo.buscarPorCategoria(productoRepo.getCategorias().get(0)));

                        cmbProductoCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                productoAdapter.setProductoList(productoRepo.buscarPorCategoria((Categoria) parent.getItemAtPosition(position)));
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
        hiloCargarCombo.start();

        /*final ProductoRepository productoRespository = new ProductoRepository();

        // widgets
        // spinner con categorías de productos
        final Spinner cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        cmbProductosCategoria.setAdapter(new ArrayAdapter<Categoria>(ListaProductos.this, android.R.layout.simple_spinner_dropdown_item, productoRespository.getCategorias()));

        // inicializar lista
        final RecyclerView lstProductos = (RecyclerView) findViewById(R.id.lstProductos);
        lstProductos.setHasFixedSize(true);
        lstProductos.setLayoutManager(new LinearLayoutManager(this));
        // decoración de la lista
        lstProductos.addItemDecoration(new VerticalSpaceItemDecoration(48));
        lstProductos.addItemDecoration(new DividerItemDecoration(this));
        // setar adapter
        final ProductoAdapter productoAdapter = new ProductoAdapter(productoRespository.buscarPorCategoria(productoRespository.getCategorias().get(0)));
        lstProductos.setAdapter(productoAdapter);

        // actualizar lista de productos al cambiar opción en spinner
        cmbProductosCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productoAdapter.setProductoList(productoRespository.buscarPorCategoria((Categoria) parent.getItemAtPosition(position)));
                productoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final EditText edtProductoCantidad = (EditText) findViewById(R.id.edtProdCantidad);
        final Button btnProdAddPedido = (Button) findViewById(R.id.btnProdAddPedido);

        // chequear si la actividad padre genero un intent con un extra NUEVO_PEDIDO = 1
        // si es true habilitar edittext, button y setear clicklistener
        if (getIntent().getIntExtra("NUEVO_PEDIDO", 0) == 1) {
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
        }*/
    }
}

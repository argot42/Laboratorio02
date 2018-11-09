package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.adapter.ProductoSeleccionadoAdapter;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.LabDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.decoration.DividerItemDecoration;
import ar.edu.utn.frsf.dam.isi.laboratorio02.decoration.VerticalSpaceItemDecoration;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;


public class NuevoPedido extends AppCompatActivity {
    private Pedido unPedido;
    private List<PedidoDetalle> listaDetalles;
    private List<PedidoDetalle> detallesToDelete = new ArrayList<>();

    private ProductoSeleccionadoAdapter productoSeleccionadoAdapter;

    private static int NUEVOPROD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pedido);

        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final RadioGroup optPedidoModoEntrega = (RadioGroup) findViewById(R.id.optPedidoModoEntrega);
        final EditText edtPedidoDireccion = (EditText) findViewById(R.id.edtPedidoDireccion);
        final RecyclerView lstProductosSeleccionados = (RecyclerView) findViewById(R.id.lstProductosSeleccionados);
        final Button btnPedidoAddProducto = (Button) findViewById(R.id.btnPedidoAddProducto);
        final Button btnPedidoQuitarProducto = (Button) findViewById(R.id.btnPedidoQuitarProducto);
        final EditText edtPedidoHoraEntrega = (EditText) findViewById(R.id.edtPedidoHoraEntrega);
        final Button btnPedidoHacerPedido = (Button) findViewById(R.id.btnPedidoHacerPedido);
        final Button btnPedidoVolver = (Button) findViewById(R.id.btnPedidoVolver);

        // si hay extras llenar los campos con esa informacion
        final long idPedidoSeleccionado = getIntent().getLongExtra("idPedidoSeleccionado", -1);

        // database
        final LabDatabase lb = LabDatabase.getDatabase(NuevoPedido.this);

        if (idPedidoSeleccionado >= 0) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    PedidoConDetalles pcd = lb.pedidoDao().buscarPedidoPorIdConDetalles(idPedidoSeleccionado);

                    unPedido = pcd.getPedido();
                    listaDetalles = pcd.getDetalle();

                    Log.d("LAB_04", listaDetalles.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edtEmail.setText(unPedido.getMailContacto()); // set email

                            // set correct radiobutton
                            if (unPedido.getRetirar()) {
                                optPedidoModoEntrega.check(R.id.optPedidoRetira);
                            } else {
                                optPedidoModoEntrega.check(R.id.optPedidoEnviar);
                            }

                            edtPedidoDireccion.setText(unPedido.getDireccionEnvio()); // set direccion envio

                            // get time
                            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                            edtPedidoHoraEntrega.setText(dateFormat.format(unPedido.getFecha()));

                            productoSeleccionadoAdapter = new ProductoSeleccionadoAdapter(listaDetalles); // set detalles

                            // lista de productos
                            lstProductosSeleccionados.setHasFixedSize(true);
                            lstProductosSeleccionados.setLayoutManager(new LinearLayoutManager(NuevoPedido.this));
                            //decoracion
                            lstProductosSeleccionados.addItemDecoration(new VerticalSpaceItemDecoration(48));
                            lstProductosSeleccionados.addItemDecoration(new DividerItemDecoration(NuevoPedido.this));
                            // agregar adaptador
                            lstProductosSeleccionados.setAdapter(productoSeleccionadoAdapter);
                        }
                    });
                }
            };

            Thread hiloPopularPedido = new Thread(r);
            hiloPopularPedido.start();

        } else {
            unPedido = new Pedido();
            listaDetalles = new ArrayList<>();
            productoSeleccionadoAdapter = new ProductoSeleccionadoAdapter(listaDetalles);

            // completar email y hacer pedido con preferencias
            SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
            // email
            String emailDefault = preferencias.getString("prefCorreoDefault", null);
            if (emailDefault != null) {
                edtEmail.setText(emailDefault);
                unPedido.setMailContacto(emailDefault);
            }
            // retirar
            if (preferencias.getBoolean("prefRetirarDefault", false)) {
                optPedidoModoEntrega.check(R.id.optPedidoRetira);
                unPedido.setRetirar(true);
            } else {
                optPedidoModoEntrega.check(R.id.optPedidoEnviar);
                unPedido.setRetirar(false);
            }

            // lista de productos
            lstProductosSeleccionados.setHasFixedSize(true);
            lstProductosSeleccionados.setLayoutManager(new LinearLayoutManager(this));
            //decoracion
            lstProductosSeleccionados.addItemDecoration(new VerticalSpaceItemDecoration(48));
            lstProductosSeleccionados.addItemDecoration(new DividerItemDecoration(this));
            // agregar adaptador
            lstProductosSeleccionados.setAdapter(productoSeleccionadoAdapter);
        }

        // get email
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                unPedido.setMailContacto(edtEmail.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // get modo entrega
        optPedidoModoEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.optPedidoRetira) {
                    unPedido.setRetirar(true);
                } else if (checkedId == R.id.optPedidoEnviar) {
                    unPedido.setRetirar(false);
                }
            }
        });

        // get direccion de envío
        edtPedidoDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                unPedido.setDireccionEnvio(edtPedidoDireccion.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // agregar producto
        btnPedidoAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NuevoPedido.this, ListaProductos.class);
                i.putExtra("NUEVO_PEDIDO", 1);
                startActivityForResult(i, NUEVOPROD);
                }
        });

        // quitar producto
        btnPedidoQuitarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detallesToDelete.add(productoSeleccionadoAdapter.getSelectedDetalle());
                productoSeleccionadoAdapter.deleteSelectedItem();
            }
        });

        // hacer pedido
        btnPedidoHacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check fecha
                String[] horaIngresada = edtPedidoHoraEntrega.getText().toString().split(":");

                int valorHora = Integer.valueOf(horaIngresada[0]);
                int valorMinuto = Integer.valueOf(horaIngresada[1]);

                if (valorHora < 0 || valorHora > 23) {
                    Toast.makeText(NuevoPedido.this, String.format("La hora ingresada (%d) es incorrecta", valorHora), Toast.LENGTH_LONG).show();
                    return;
                }
                if (valorMinuto < 0 || valorMinuto > 59) {
                    Toast.makeText(NuevoPedido.this, String.format("Los minutos ingresados (%d) son incorrectos", valorMinuto), Toast.LENGTH_LONG).show();
                    return;
                }

                GregorianCalendar hora = new GregorianCalendar();
                hora.set(Calendar.HOUR_OF_DAY, valorHora);
                hora.set(Calendar.MINUTE, valorMinuto);
                hora.set(Calendar.SECOND, 0);
                unPedido.setFecha(hora.getTime());

                if (unPedido.getMailContacto() == null || unPedido.getMailContacto().equals("")) {
                    Toast.makeText(NuevoPedido.this, "El campo Correo Electrónico no puede estar vacío", Toast.LENGTH_LONG).show();
                    return;
                }
                if (unPedido.getRetirar() == null) {
                    Toast.makeText(NuevoPedido.this, "El campo Modo Entrega no puede estar vacío", Toast.LENGTH_LONG).show();
                    return;
                }
                if (unPedido.getDireccionEnvio() == null || unPedido.getDireccionEnvio().equals("")) {
                    Toast.makeText(NuevoPedido.this, "El campo Dirección de Envío no puede estar vacío", Toast.LENGTH_LONG).show();
                    return;
                }
                if (productoSeleccionadoAdapter == null || productoSeleccionadoAdapter.getItemCount() == 0) {
                    Toast.makeText(NuevoPedido.this, "Se deben agregar productos al pedido", Toast.LENGTH_LONG).show();
                    return;
                }

                unPedido.setEstado(Pedido.Estado.REALIZADO);

                Runnable r = new Runnable() {
                    @Override public void run() {
                        // guardar pedido en local db
                        if (idPedidoSeleccionado < 0) {
                            long pedidoId = lb.pedidoDao().insert(unPedido);

                            // guardar detalles
                            for (PedidoDetalle pd : listaDetalles) {
                                pd.setIdPedidoAsignado(pedidoId);
                                lb.pedidoDetalleDao().insert(pd);
                            }

                        } else {
                            lb.pedidoDao().update(unPedido);

                            for (PedidoDetalle pd : detallesToDelete) {
                                lb.pedidoDetalleDao().delete(pd);
                            }

                            for (PedidoDetalle pd : listaDetalles) {
                                pd.setIdPedidoAsignado(idPedidoSeleccionado);
                                lb.pedidoDetalleDao().insert(pd);
                            }
                        }

                        // esperar
                        try {
                            Thread.currentThread().sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // buscar pedidos no aceptados y aceptarlos automáticamente
                        List<Pedido> lista = lb.pedidoDao().getAll();
                        for(Pedido p:lista){
                            if(p.getEstado().equals(Pedido.Estado.REALIZADO)) {
                                p.setEstado(Pedido.Estado.ACEPTADO);

                                // update db
                                lb.pedidoDao().update(p);

                                Intent i = new Intent();
                                i.setAction(EstadoPedidoReceiver.ESTADO_ACEPTADO);
                                i.putExtra("idPedido", p.getId());
                                sendBroadcast(i);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NuevoPedido.this,
                                        "Informacion de pedidos actualizada!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };
                Thread unHilo = new Thread(r);
                unHilo.start();

                Intent i = new Intent(NuevoPedido.this, HistorialPedidos.class);
                startActivity(i);
                finish();
            }
        });

        // volver
        btnPedidoVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NUEVOPROD) {
            if (resultCode == RESULT_OK) {

                final long idProducto = data.getExtras().getLong("idProducto");
                final int cantidad = data.getExtras().getInt("cantidad");

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        LabDatabase lb = LabDatabase.getDatabase(NuevoPedido.this);

                        Producto p = lb.productoDao().buscarProductoPorId(idProducto);
                        if (p == null) return;

                        final PedidoDetalle pedidoDetalle = new PedidoDetalle(cantidad, p);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                productoSeleccionadoAdapter.addItem(pedidoDetalle); // agregamos pedido a adapter


                                Double total = 0.0;
                                for (PedidoDetalle pd: listaDetalles) {
                                    total += pd.getCantidad() * pd.getProducto().getPrecio();
                                }

                                final TextView tvTotalPedido = (TextView) findViewById(R.id.tvTotalPedido);
                                tvTotalPedido.setText(String.format("Total pedido: $%.2f", total));
                            }
                        });
                    }
                };

                Thread hiloAgregarDetalle = new Thread(r);
                hiloAgregarDetalle.start();
            }
        }
    }
}

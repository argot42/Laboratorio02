package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.decoration.DividerItemDecoration;
import ar.edu.utn.frsf.dam.isi.laboratorio02.decoration.VerticalSpaceItemDecoration;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;


public class NuevoPedido extends AppCompatActivity {
    private Pedido unPedido;
    private PedidoRepository repositorioPedido = new PedidoRepository();
    private ProductoRepository repositorioProducto;

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
        int idPedidoSeleccionado = getIntent().getIntExtra("idPedidoSeleccionado", -1);

        if (idPedidoSeleccionado >= 0) {
            unPedido = repositorioPedido.buscarPorId(idPedidoSeleccionado);

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

            productoSeleccionadoAdapter = new ProductoSeleccionadoAdapter(unPedido.getDetalle()); // set detalles

        } else {
            unPedido = new Pedido();
            productoSeleccionadoAdapter = new ProductoSeleccionadoAdapter(unPedido.getDetalle());
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

        // lista de productos
        lstProductosSeleccionados.setHasFixedSize(true);
        lstProductosSeleccionados.setLayoutManager(new LinearLayoutManager(this));
        //decoracion
        lstProductosSeleccionados.addItemDecoration(new VerticalSpaceItemDecoration(48));
        lstProductosSeleccionados.addItemDecoration(new DividerItemDecoration(this));
        // agregar adaptador
        lstProductosSeleccionados.setAdapter(productoSeleccionadoAdapter);

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

                repositorioPedido.guardarPedido(unPedido);

                Intent i = new Intent(NuevoPedido.this, HistorialPedidos.class);
                startActivity(i);
                finish();

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.currentThread().sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // buscar pedidos no aceptados y aceptarlos utomáticamente
                        List<Pedido> lista = repositorioPedido.getLista();
                        for(Pedido p:lista){
                            if(p.getEstado().equals(Pedido.Estado.REALIZADO))
                                p.setEstado(Pedido.Estado.ACEPTADO);
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
                int idProducto = data.getExtras().getInt("idProducto");
                int cantidad = data.getExtras().getInt("cantidad");

                repositorioProducto = new ProductoRepository();
                Producto nuevoProducto = repositorioProducto.buscarPorId(idProducto);
                PedidoDetalle pedidoDetalle = new PedidoDetalle(cantidad, nuevoProducto);

                productoSeleccionadoAdapter.addItem(pedidoDetalle);

                final TextView tvTotalPedido = (TextView) findViewById(R.id.tvTotalPedido);
                tvTotalPedido.setText(String.format("Total pedido: $%.2f", unPedido.total()));
            }
        }
    }
}

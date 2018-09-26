package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class MainActivity extends AppCompatActivity {

    private Button btnNuevoPedido;
    private Button btnHistorial;
    private Button btnListaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNuevoPedido = (Button) findViewById(R.id.btnMainNuevoPedido);
        btnNuevoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, NuevoPedido.class);
         startActivity(i);
            }
        });

        btnHistorial = (Button) findViewById(R.id.btnHistorialPedidos);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, HistorialPedidos.class);
                startActivity(i);
            }
        });

        btnListaProductos = (Button) findViewById(R.id.btnListaProductos);
        btnListaProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaProductos.class);
                startActivity(intent);

                /*Intent intent = new Intent(MainActivity.this, ListaProductos.class);
                intent.putExtra("NUEVO_PEDIDO", 1);
                startActivityForResult(intent, 1);*/
            }
        });

        /* *****TEST***** */
        ProductoRepository testProductoRepository = new ProductoRepository();
        Producto testProducto = testProductoRepository.buscarPorId(0);
        List<PedidoDetalle> testListPedidoDetalle = new ArrayList<>();
        testListPedidoDetalle.add(new PedidoDetalle(10, testProducto));
        testListPedidoDetalle.add(new PedidoDetalle(20, testProducto));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date ddate;
        try {
            ddate = format.parse("2018-10-03 09:10:10");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("jejej");
        }

        Pedido testPedido = new Pedido(ddate, testListPedidoDetalle, Pedido.Estado.ACEPTADO, "San Martin 2400", "foo@bar.org", true);
        PedidoRepository testRepo = new PedidoRepository();
        testRepo.guardarPedido(testPedido);

        List<PedidoDetalle> testListPedidoDetalle2 = new ArrayList<>();
        testListPedidoDetalle2.add(new PedidoDetalle(30, testProducto));
        Pedido testPedido2 = new Pedido(ddate, testListPedidoDetalle2, Pedido.Estado.RECHAZADO, "FOOBAR 200", "foobar@test.org", false);
        testRepo.guardarPedido(testPedido2);

        List<PedidoDetalle> detalles3 = new ArrayList<>();
        detalles3.add(new PedidoDetalle(40, testProductoRepository.buscarPorId(10)));
        testRepo.guardarPedido(
                new Pedido(ddate, detalles3, Pedido.Estado.CANCELADO, "TEST 0", "TEST@TEST.org", false)
        );

        List<PedidoDetalle> detalles4 = new ArrayList<>();
        detalles4.add(new PedidoDetalle(100, testProductoRepository.buscarPorId(15)));
        testRepo.guardarPedido(
                new Pedido(ddate, detalles4, Pedido.Estado.LISTO, "TEST 0", "TEST@TEST.org", true)
        );
        /* ********************** */
    }
}

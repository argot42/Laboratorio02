package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import ar.edu.utn.frsf.dam.isi.laboratorio02.adapter.HistorialAdapter;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.decoration.DividerItemDecoration;
import ar.edu.utn.frsf.dam.isi.laboratorio02.decoration.VerticalSpaceItemDecoration;

public class HistorialPedidos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        final Button btnHistorialNuevo = (Button) findViewById(R.id.btnHistorialNuevo);
        btnHistorialNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HistorialPedidos.this, NuevoPedido.class);
                startActivity(i);
                finish();
            }
        });

        final Button btnHistorialMenu = (Button) findViewById(R.id.btnHistorialMenu);
        btnHistorialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // lista de pedidos
        final RecyclerView lstHistorial = (RecyclerView) findViewById(R.id.lstHistorialPedidos);
        lstHistorial.setHasFixedSize(true);
        lstHistorial.setLayoutManager(new LinearLayoutManager(this));
        lstHistorial.addItemDecoration(new VerticalSpaceItemDecoration(48));
        lstHistorial.addItemDecoration(new DividerItemDecoration(this));
        lstHistorial.setAdapter(new HistorialAdapter(new PedidoRepository().getLista()));
    }
}

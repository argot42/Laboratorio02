package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HistorialPedidos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        Button btnHistorialNuevo = (Button) findViewById(R.id.btnHistorialNuevo);
        btnHistorialNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HistorialPedidos.this, NuevoPedido.class);
                startActivity(i);
            }
        });

        Button btnHistorialMenu = (Button) findViewById(R.id.btnHistorialMenu);
        btnHistorialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

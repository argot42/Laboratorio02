package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;


import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;


import static android.R.layout.simple_list_item_single_choice;
import static ar.edu.utn.frsf.dam.isi.laboratorio02.ListaProductos.LISTAPRODUCTO_REQUEST;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class NuevoPedido extends AppCompatActivity {

    private Pedido unPedido;

    // widgets
    private EditText edtEmail;
    private RadioGroup rgPedidoModoEntrega;
    private EditText edtPedidoDireccion;
    private EditText edtPedidoHoraEntrega;
    private Button btnPedidoAddProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pedido);

        unPedido = new Pedido();

        // obtener widgets de la vista
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        rgPedidoModoEntrega = (RadioGroup) findViewById(R.id.optPedidoModoEntrega);
        edtPedidoDireccion = (EditText) findViewById(R.id.edtPedidoDireccion);
        edtPedidoHoraEntrega=(EditText) findViewById(R.id.edtPedidoHoraEntregaDefault);
        btnPedidoAddProducto=(Button) findViewById(R.id.btnPedidoAddProducto);

        // setear event listeners
        // email input
        // setear event listeners
        // email input
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                unPedido.setMailContacto(edtEmail.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Modo de Entrega input
        rgPedidoModoEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){

                if (checkedId == R.id.optPedidoRetira) {
                    //some code
                    unPedido.setRetirar(TRUE);

                } else if (checkedId == R.id.optPedidoEnviar) {
                    //some code
                    unPedido.setRetirar(FALSE);
                }
            }
        });

        // pedidoDireccion input
        // setear event listeners
        // pedidoDireccion input
        edtPedidoDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                unPedido.setDireccionEnvio(edtPedidoDireccion.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final String[] datos= new String[]{"Elem1","Elem2","Elem3"};
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, simple_list_item_single_choice, datos);
        ListView listView = findViewById(R.id.lstPedidoItems);
        listView.setAdapter(itemsAdapter);



        // botÃ³n realizar plazo fijo
        btnPedidoAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(NuevoPedido.this,ListaProductos.class);
                i.putExtra("NUEVO_PEDIDO",1);
                startActivityForResult(i,LISTAPRODUCTO_REQUEST);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == LISTAPRODUCTO_REQUEST) {
                Integer id= data.getExtras().getInt("idProducto");
                Integer cantidad= data.getExtras().getInt("cantidad");

            }

        }


    }

}

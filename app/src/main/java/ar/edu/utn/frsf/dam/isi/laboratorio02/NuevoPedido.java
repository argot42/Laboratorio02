package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;


import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;


import static android.R.layout.simple_list_item_single_choice;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class NuevoPedido extends AppCompatActivity {

    private Pedido unPedido;

    // widgets
    private EditText edtEmail;
    private RadioGroup rgPedidoModoEntrega;
    private EditText edtPedidoDireccion;
    private EditText edtPedidoHoraEntrega;

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

    }

}

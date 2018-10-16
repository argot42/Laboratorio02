package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {
    public static String ESTADO_ACEPTADO = "ar.edu.utn.frsf.dam.isi.laboratorio2.ESTADO_ACEPTADO";
    public static String ESTADO_CANCELADO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_CANCELADO";
    public static String ESTADO_EN_PREPARACION = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION";
    public static String ESTADO_LISTO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ESTADO_ACEPTADO)) {
            int idPedido = intent.getIntExtra("idPedido", -1);

            if (idPedido >= 0) {
                PedidoRepository pr = new PedidoRepository();
                Pedido p = pr.buscarPorId(idPedido);
                Toast.makeText(context,
                        String.format("Pedido para %s ha cambiado de estado a %s", p.getMailContacto(), p.getEstado()),
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}

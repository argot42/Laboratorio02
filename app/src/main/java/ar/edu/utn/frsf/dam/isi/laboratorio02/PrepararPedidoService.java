package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.LabDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {

    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            // esperamos 20 seg
            try {
               Thread.sleep(20000);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }

            // obtenemos lista de pedidos
            LabDatabase lb = LabDatabase.getDatabase(this);
            List<Pedido> pedidos = lb.pedidoDao().getAll();
            // iteramos sobre pedidos cambiando los estados "ACEPTADO" a "EN_PREPARACION"
            // y enviamos un broadcast notificando de dicho cambio
            for (Pedido p:pedidos) {
                if (!p.getEstado().equals(Pedido.Estado.ACEPTADO)) { continue; }

                p.setEstado(Pedido.Estado.EN_PREPARACION);

                lb.pedidoDao().update(p);

                Intent i = new Intent();
                i.setAction(EstadoPedidoReceiver.ESTADO_EN_PREPARACION);
                i.putExtra("idPedido", p.getId());
                sendBroadcast(i);
            }
        }
    }
}

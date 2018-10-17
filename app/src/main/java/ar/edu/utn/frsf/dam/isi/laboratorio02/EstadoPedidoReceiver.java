package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
//import android.widget.Toast;

import java.text.SimpleDateFormat;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
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
                /*Toast.makeText(context,
                        String.format("Pedido para %s ha cambiado de estado a %s", p.getMailContacto(), p.getEstado()),
                        Toast.LENGTH_LONG
                ).show();*/

               // Hacer notificacion clickeable
                Intent i = new Intent(context, NuevoPedido.class);
                i.putExtra("idPedidoSeleccionado", p.getId());

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntentWithParentStack(i);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                String hour_min = new SimpleDateFormat("HH:mm").format(p.getFecha());

                Notification notification = new NotificationCompat.Builder(context, "CANAL01")
                        .setSmallIcon(R.drawable.new_post)
                        .setContentTitle("Tu pedido fue aceptado")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(String.format("El costo será de %.2f\nPrevisto el envío para %shs", p.total(), hour_min)))
                        .setContentIntent(resultPendingIntent)
                        .build();

                NotificationManagerCompat nManager = NotificationManagerCompat.from(context);
                nManager.notify(0, notification);
            }
        }
    }
}

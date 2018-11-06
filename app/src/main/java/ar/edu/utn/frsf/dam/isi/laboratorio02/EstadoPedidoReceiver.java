package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.LabDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {
    public static final String ESTADO_ACEPTADO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO";
    public static final String ESTADO_CANCELADO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_CANCELADO";
    public static final String ESTADO_EN_PREPARACION = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION";
    public static final String ESTADO_LISTO = "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) { return; }
        String action = intent.getAction();
        if (action == null) { return; }

        // get database
        final LabDatabase lb = LabDatabase.getDatabase(context);
        final Context c = context;

        switch (action) {
            case ESTADO_ACEPTADO: {
                final int idPedido = intent.getIntExtra("idPedido", -1);

                if (idPedido >= 0) {

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Pedido p = lb.pedidoDao().buscarPedidoPorId(idPedido);

                            // Hacer notification clickeable
                            Intent i = new Intent(c, NuevoPedido.class);
                            i.putExtra("idPedidoSeleccionado", p.getId());

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
                            stackBuilder.addNextIntentWithParentStack(i);
                            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                            String hour_min = new SimpleDateFormat("HH:mm").format(p.getFecha());

                            Notification notification = new NotificationCompat.Builder(c, "CANAL01")
                                    .setSmallIcon(R.drawable.new_post)
                                    .setContentTitle("Tu pedido fue aceptado")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(String.format("El costo será de %.2f\nPrevisto el envío para %shs", p.total(), hour_min)))
                                    .setContentIntent(resultPendingIntent)
                                    .build();

                            NotificationManagerCompat nManager = NotificationManagerCompat.from(c);
                            nManager.notify(0, notification);
                        }
                    };

                    Thread hiloAceptado = new Thread(r);
                    hiloAceptado.start();
                }

                break;
            }

            case ESTADO_EN_PREPARACION: {
                final int idPedido = intent.getIntExtra("idPedido", -1);
                if (idPedido < 0) { return; }

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Pedido p = lb.pedidoDao().buscarPedidoPorId(idPedido);

                        // notificacion clickeable
                        Intent i = new Intent(c, HistorialPedidos.class);

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
                        stackBuilder.addNextIntentWithParentStack(i);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                        String hour_min = new SimpleDateFormat("HH:mm").format(p.getFecha());

                        Notification notification = new NotificationCompat.Builder(c, "CANAL01")
                                .setSmallIcon(R.drawable.new_post)
                                .setContentTitle("Tu pedido esta siendo preparado")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(String.format("El costo será de %.2f\nPrevisto el envío para %shs", p.total(), hour_min)))
                                .setContentIntent(resultPendingIntent)
                                .build();

                        NotificationManagerCompat nManager = NotificationManagerCompat.from(c);
                        nManager.notify(1, notification);
                    }
                };

                Thread hiloEnPreparacion = new Thread(r);
                hiloEnPreparacion.start();

                break;
            }

            case ESTADO_LISTO: {
                final int idPedido = intent.getIntExtra("idPedido", -1);
                if (idPedido < 0 ) { return; }

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Pedido p = lb.pedidoDao().buscarPedidoPorId(idPedido);

                        Intent i = new Intent(c, HistorialPedidos.class);

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
                        stackBuilder.addNextIntentWithParentStack(i);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                        Notification notification = new NotificationCompat.Builder(c, "CANAL02")
                                .setSmallIcon(R.drawable.new_post)
                                .setContentTitle("Tu pedido esta listo")
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(String.format("El costo será de %.2f", p.total())))
                                .setContentIntent(resultPendingIntent)
                                .build();

                        NotificationManagerCompat nManager = NotificationManagerCompat.from(c);
                        nManager.notify(2, notification);
                    }
                };


                break;
            }
        }
    }
}

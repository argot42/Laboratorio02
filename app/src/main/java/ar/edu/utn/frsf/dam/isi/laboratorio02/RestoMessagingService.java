package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.LabDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class RestoMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        guardarToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Map<String, String> data = message.getData();
        if (data.isEmpty()) { return; }

        LabDatabase lb = LabDatabase.getDatabase(this);
        Pedido pedido = lb.pedidoDao().buscarPedidoPorId(Integer.parseInt(data.get("ID_PEDIDO")));
        if (pedido == null) { return; }

        pedido.setEstado(Pedido.Estado.LISTO);

        Intent i = new Intent();
        i.setAction(EstadoPedidoReceiver.ESTADO_LISTO);
        i.putExtra("idPedido", pedido.getId());
        sendBroadcast(i);
    }

    private void guardarToken(String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id", token);
        editor.apply();
    }

    private String leerToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("registration_id", null);
    }
}

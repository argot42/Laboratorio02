package ar.edu.utn.frsf.dam.isi.laboratorio02.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {
    private List<Pedido> listaPedidos;

    public class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmailPedido;
        TextView tvHoraEntrega;
        TextView tvCantidadItems;
        TextView tvPrecio;
        TextView tvEstado;
        ImageView ivTipoEntrega;
        Button btnCancelarPedido;

        public HistorialViewHolder(View base) {
            super(base);
            this.tvEmailPedido = (TextView) base.findViewById(R.id.tvEmailPedido);
            this.tvHoraEntrega = (TextView) base.findViewById(R.id.tvHoraEntrega);
            this.tvCantidadItems = (TextView) base.findViewById(R.id.tvCantidadItems);
            this.tvPrecio = (TextView) base.findViewById(R.id.tvPrecio);
            this.tvEstado = (TextView) base.findViewById(R.id.tvEstado);
            this.ivTipoEntrega = (ImageView) base.findViewById(R.id.ivTipoEntrega);
            this.btnCancelarPedido = (Button) base.findViewById(R.id.btnCancelarPedido);
        }
    }

    public HistorialAdapter(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @Override
    public HistorialAdapter.HistorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistorialViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_row, parent, false));
    }

    @Override
    public void onBindViewHolder(HistorialViewHolder holder, int position) {
        Pedido pedido = this.listaPedidos.get(position);

        holder.tvEmailPedido.setText(String.format("Contacto: %s", pedido.getMailContacto()));
        holder.tvHoraEntrega.setText(String.format("Hora de Entrega: %s", pedido.getFecha()));

        // estado con color
        Spannable estado = new SpannableString(String.format("Estado: %s", pedido.getEstado().name()));
        ForegroundColorSpan color = new ForegroundColorSpan(Color.BLACK);
        switch(pedido.getEstado()) {
            case LISTO:
                color = new ForegroundColorSpan(Color.DKGRAY);
                break;
            case ENTREGADO:
                color = new ForegroundColorSpan(Color.BLUE);
                break;
            case CANCELADO:
            case RECHAZADO:
                color = new ForegroundColorSpan(Color.RED);
                break;
            case ACEPTADO:
                color = new ForegroundColorSpan(Color.GREEN);
                break;
            case EN_PREPARACION:
                color = new ForegroundColorSpan(Color.MAGENTA);
                break;
            case REALIZADO:
                color = new ForegroundColorSpan(Color.BLUE);
                break;
        }
        estado.setSpan(color, 8, 8 + pedido.getEstado().name().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvEstado.setText(estado);

        // calcular cantidad de items y deuda total
        Integer cantidad = 0;
        Double deuda = 0.0;
        for (PedidoDetalle d : pedido.getDetalle()) {
            cantidad += d.getCantidad();
            deuda += d.getProducto().getPrecio() * d.getCantidad();
        }
        holder.tvCantidadItems.setText(String.format("Items: %d", cantidad));
        holder.tvPrecio.setText(String.format("A pagar: $%.2f", deuda));

        if (pedido.getRetirar()) {
            holder.ivTipoEntrega.setImageResource(R.drawable.ic_retirar);
        } else {
            holder.ivTipoEntrega.setImageResource(R.drawable.ic_delivery);
        }
    }

    @Override
    public int getItemCount() { return this.listaPedidos.size(); }
}

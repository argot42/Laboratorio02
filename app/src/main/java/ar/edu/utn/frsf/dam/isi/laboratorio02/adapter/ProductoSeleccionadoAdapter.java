package ar.edu.utn.frsf.dam.isi.laboratorio02.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class ProductoSeleccionadoAdapter extends RecyclerView.Adapter<ProductoSeleccionadoAdapter.ProductoSeleccionadoViewHolder> {
    private List<PedidoDetalle> detalleList;
    private int lastSelectedPosition = -1;

    public class ProductoSeleccionadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        RadioButton selectionState;

        public ProductoSeleccionadoViewHolder(View base) {
            super(base);
            this.tvNombre = (TextView) base.findViewById(R.id.tvNombre);
            this.selectionState = (RadioButton) base.findViewById(R.id.rbSelectionState);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public ProductoSeleccionadoAdapter(List<PedidoDetalle> detalleList) {
        this.detalleList = detalleList;
    }

    @Override
    public ProductoSeleccionadoAdapter.ProductoSeleccionadoViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        return new ProductoSeleccionadoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductoSeleccionadoViewHolder holder, int position) {
        PedidoDetalle detalle = detalleList.get(position);
        holder.tvNombre.setText(String.format("%s ($%.2f) %d", detalle.getProducto().getNombre(), detalle.getProducto().getPrecio(), detalle.getCantidad()));
        holder.selectionState.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() { return detalleList.size(); }

    public void deleteSelectedItem() {
        if (lastSelectedPosition < 0) { return; }

        detalleList.remove(lastSelectedPosition);
        notifyItemChanged(lastSelectedPosition);

        lastSelectedPosition = -1;
    }

    public void addItem(PedidoDetalle detalle) {
        detalleList.add(detalle);
        notifyItemInserted(this.getItemCount() - 1);
    }

    public List<PedidoDetalle> getDetalles() {
        return detalleList;
    }
}
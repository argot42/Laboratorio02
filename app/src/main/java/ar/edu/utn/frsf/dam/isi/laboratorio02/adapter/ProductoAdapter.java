package ar.edu.utn.frsf.dam.isi.laboratorio02.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productoList;
    private int lastSelectedPosition = -1;

    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        RadioButton selectionState;

        public ProductoViewHolder(View base) {
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

    public ProductoAdapter(List<Producto> productoList) {
        this.productoList = productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public ProductoAdapter.ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);
        holder.tvNombre.setText(String.format("%s ($%.2f)", producto.getNombre(), producto.getPrecio()));
        holder.selectionState.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public Producto getItem() {
        if (this.lastSelectedPosition < 0) {
            return null;
        }

        return this.productoList.get(this.lastSelectedPosition);
    }
}
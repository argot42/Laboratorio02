package ar.edu.utn.frsf.dam.isi.laboratorio02.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productoList;

    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;

        public ProductoViewHolder(View base) {
            super(base);
            this.tvNombre = (TextView) base.findViewById(R.id.tvNombre);
        }
    }

    public ProductoAdapter(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public ProductoAdapter.ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_row, parent, false);
        ProductoViewHolder vh = new ProductoViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);
        holder.tvNombre.setText(String.format("%s ($%f)", producto.getNombre(), producto.getPrecio()));
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }
}

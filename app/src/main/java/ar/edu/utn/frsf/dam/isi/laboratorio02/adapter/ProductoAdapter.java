package ar.edu.utn.frsf.dam.isi.laboratorio02.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productoList;

    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        public ProductoViewHolder(View view) {
            super(view);
        }
    }

    public ProductoAdapter(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }
}

package com.example.expensetrackerv01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private Context mContext;
    private List<Gasto> mGastos;
    private DataBaseHelper_Java dbHelper;

    public GastoAdapter(Context context, List<Gasto> gastos, DataBaseHelper_Java dbHelper) {
        mContext = context;
        mGastos = gastos;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto currentGasto = mGastos.get(position);
        holder.tvImporte.setText(currentGasto.getImporte());
        holder.tvCategoria.setText(currentGasto.getCategoria());
        holder.tvMetodoPago.setText(currentGasto.getMetodoPago());
        holder.tvSubcategoria.setText(currentGasto.getSubcategoria());
        holder.tvFecha.setText(currentGasto.getFecha());  // Mostrar la fecha

        holder.btnEliminar.setOnClickListener(v -> {
            dbHelper.eliminarGasto(currentGasto.getId());
            mGastos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mGastos.size());
        });
    }

    @Override
    public int getItemCount() {
        return mGastos.size();
    }

    public class GastoViewHolder extends RecyclerView.ViewHolder {
        TextView tvImporte, tvCategoria, tvMetodoPago, tvSubcategoria, tvFecha;
        Button btnEliminar;

        public GastoViewHolder(View itemView) {
            super(itemView);
            tvImporte = itemView.findViewById(R.id.tv_importe);
            tvCategoria = itemView.findViewById(R.id.tv_categoria);
            tvMetodoPago = itemView.findViewById(R.id.tv_metodo_pago);
            tvSubcategoria = itemView.findViewById(R.id.tv_subcategoria);
            tvFecha = itemView.findViewById(R.id.tv_fecha);  // Referencia al TextView de la fecha
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

}

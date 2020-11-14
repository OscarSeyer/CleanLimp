package com.dvalic.appautofin.Activities.Compra.Casa;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dvalic.appautofin.API.Model.MCotizacion;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.ItemCotizacionBinding;
import java.util.ArrayList;
import java.util.Objects;


public class CotizacionAdapter  extends RecyclerView.Adapter<CotizacionAdapter.CotizacionViewHolder> {

    private ArrayList<MCotizacion> cotizacionList;
    CotizarMonto cotizarMonto;


    public CotizacionAdapter(CotizarMonto cotizarMonto){
        cotizacionList = new ArrayList<>();
        this.cotizarMonto = cotizarMonto;
    }

    @NonNull
    @Override
    public CotizacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CotizacionViewHolder(ItemCotizacionBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull CotizacionViewHolder holder, int position) {

        MCotizacion cotizacion = cotizacionList.get(position);
        setValueCorrect(cotizacion);
        holder.binding.tvCantidadTotal.setText( UTHelpers.formatMoney(Double.parseDouble(Objects.requireNonNull(cotizacion.getMonto()))));
        holder.binding.tvCuotaTotal.setText(UTHelpers.formatMoney(Double.parseDouble(Objects.requireNonNull(cotizacion.getCuota()))));
        holder.binding.tvInscripcion.setText(UTHelpers.formatMoney(Double.parseDouble(Objects.requireNonNull(cotizacion.getInscripcion()))));
        Double cuta_inscripcion = Double.parseDouble(cotizacion.getCuota()) + Double.parseDouble(cotizacion.getInscripcion());
        holder.binding.tvCuotaInscripcion.setText(UTHelpers.formatMoney(cuta_inscripcion));
        holder.binding.tvMensualidadAdjudica.setText( UTHelpers.formatMoney(Double.parseDouble(Objects.requireNonNull(cotizacion.getMensualidadAdjudicatario()))));
        holder.binding.tvMensualidadAdjudicaTotal.setText(UTHelpers.formatMoney(Double.parseDouble(Objects.requireNonNull(cotizacion.getMensualidadAdjudicado()))));
        boolean isExpanded = cotizacion.getExpanded();
        holder.binding.clyExpandable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        cotizarMonto.moveToTopScroll(position);
        holder.binding.clyHead.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, view.getId(),0, "Eliminar");
                contextMenu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(cotizacionList.size() == 1){
                            clearItems();
                            cotizarMonto.sumarMontos();
                            return  false;
                        }else{
                            cotizacionList.remove(position);
                            //notifyItemRemoved(position);
                            notifyDataSetChanged();
                            cotizarMonto.sumarMontos();
                            return true;
                        }

                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return cotizacionList.size();
    }




    class CotizacionViewHolder extends RecyclerView.ViewHolder {

        ItemCotizacionBinding binding;

        public CotizacionViewHolder(@NonNull final ItemCotizacionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.clyHead.setOnClickListener((v)->{
                MCotizacion cotizacion = cotizacionList.get(getAdapterPosition());
                cotizacion.setExpanded(!cotizacion.getExpanded());
                notifyItemChanged(getAdapterPosition());

            });

        }
    }

    public void addItem(MCotizacion cotizacion){
        cotizacionList.add(cotizacion);
        //this.notifyItemRangeChanged(0, cotizacionList.size());
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<MCotizacion> items){
        cotizacionList.addAll(items);
        notifyDataSetChanged();
    }

    public void clearItems(){
        cotizacionList.clear();
        notifyDataSetChanged();
    }

    public ArrayList<MCotizacion> getItems(){
        return cotizacionList;
    }


    public void setValueCorrect(MCotizacion cotizacion){
        cotizacion.setMonto(TextUtils.isEmpty(cotizacion.getMonto()) ? "0" : cotizacion.getMonto());
        cotizacion.setCuota(TextUtils.isEmpty(cotizacion.getCuota()) ? "0" : cotizacion.getCuota());
        cotizacion.setCuotaInscripcion(TextUtils.isEmpty(cotizacion.getCuotaInscripcion()) ? "0" : cotizacion.getInscripcion());
        cotizacion.setMensualidadAdjudicado(TextUtils.isEmpty(cotizacion.getMensualidadAdjudicado()) ? "0" : cotizacion.getMensualidadAdjudicado());
        cotizacion.setMensualidadAdjudicatario(TextUtils.isEmpty(cotizacion.getMensualidadAdjudicatario()) ? "0" : cotizacion.getMensualidadAdjudicatario());
    }

}
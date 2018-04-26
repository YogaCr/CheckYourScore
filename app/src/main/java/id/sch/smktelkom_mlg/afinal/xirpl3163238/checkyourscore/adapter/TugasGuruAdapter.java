package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.TugasGuruClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

/**
 * Created by Sakata Yoga on 26/04/2018.
 */

public class TugasGuruAdapter extends RecyclerView.Adapter<TugasGuruAdapter.ViewHolder> {
    List<TugasGuruClass> guruClassList;
    Context context;

    public TugasGuruAdapter(List<TugasGuruClass> guruClassList, Context context) {
        this.guruClassList = guruClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.daftar_tugas_guru, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNama.setText(guruClassList.get(position).getNama());
        holder.cvTugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return guruClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama;
        CardView cvTugas;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvnama_tugas);
            cvTugas = itemView.findViewById(R.id.cvTugasGuru);
        }
    }
}

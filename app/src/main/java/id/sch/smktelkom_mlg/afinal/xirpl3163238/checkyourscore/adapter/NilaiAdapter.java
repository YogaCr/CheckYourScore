package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.NilaiClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

/**
 * Created by Ferlina Firdausi on 18/04/2018.
 */

public class NilaiAdapter extends RecyclerView.Adapter<NilaiAdapter.ViewHolder> {
    List<NilaiClass> nilaiList;
    Context context;


    public NilaiAdapter(List<NilaiClass> nilaiList, Context context) {
        this.nilaiList = nilaiList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nilai_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.deskripsi.setText(nilaiList.get(position).getDeskripsi());
        holder.nilai.setText(String.valueOf(nilaiList.get(position).getNilai()));
        holder.status.setText(nilaiList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return nilaiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deskripsi, nilai, status;
        public LinearLayout LinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            deskripsi = itemView.findViewById(R.id.desc);
            nilai = itemView.findViewById(R.id.nilai);
            status = itemView.findViewById(R.id.status);
            LinearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }
}
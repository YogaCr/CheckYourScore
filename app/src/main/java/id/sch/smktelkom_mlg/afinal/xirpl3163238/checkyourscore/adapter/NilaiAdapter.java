package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.NilaiClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity.PesanRemidiActivity;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.deskripsi.setText(nilaiList.get(position).getDeskripsi());
        DecimalFormat df = new DecimalFormat("0");
        if (nilaiList.get(position).getNilai() % 1 == 0) {
            holder.nilai.setText(df.format(nilaiList.get(position).getNilai()));
        } else {
            holder.nilai.setText(String.valueOf(nilaiList.get(position).getNilai()));
        }
        if (nilaiList.get(position).getStatus().equals("Remidi")) {
            holder.nilai.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.cvTugas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PesanRemidiActivity.class);
                    i.putExtra("UniqueCode", nilaiList.get(position).getMapId());
                    i.putExtra("BabId", nilaiList.get(position).getId());
                    context.startActivity(i);
                }
            });
        }
        holder.status.setText(nilaiList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return nilaiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deskripsi, nilai, status;
        public CardView cvTugas;

        public ViewHolder(View itemView) {
            super(itemView);
            deskripsi = itemView.findViewById(R.id.desc);
            nilai = itemView.findViewById(R.id.nilai);
            status = itemView.findViewById(R.id.status);
            cvTugas = itemView.findViewById(R.id.cvTugasSiswa);

        }
    }
}
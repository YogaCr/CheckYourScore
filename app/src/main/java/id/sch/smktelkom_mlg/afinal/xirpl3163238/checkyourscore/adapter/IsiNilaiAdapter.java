package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.IsiNilaiClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

/**
 * Created by sadaa on 4/26/2018.
 */

public class IsiNilaiAdapter extends RecyclerView.Adapter<IsiNilaiAdapter.ViewHolder> {
    Context context;
    List<IsiNilaiClass> isiNilaiClassList;

    public IsiNilaiAdapter(List<IsiNilaiClass> isiNilaiClassList, Context context) {
        this.isiNilaiClassList = isiNilaiClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public IsiNilaiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.isi_nilai_guru, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IsiNilaiAdapter.ViewHolder holder, int position) {
        holder.NamaSiswa.setText(isiNilaiClassList.get(position).getNamaSiswa());
        DecimalFormat df = new DecimalFormat("0");
        if (isiNilaiClassList.get(position).getNilaiSiswa() % 1 == 0) {
            holder.NilaiSiswa.setText(df.format(isiNilaiClassList.get(position).getNilaiSiswa()));
        } else {
            holder.NilaiSiswa.setText(String.valueOf(isiNilaiClassList.get(position).getNilaiSiswa()));
        }
    }

    @Override
    public int getItemCount() {
        return isiNilaiClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView NamaSiswa;
        EditText NilaiSiswa;

        public ViewHolder(View itemView) {
            super(itemView);

            NamaSiswa = itemView.findViewById(R.id.tvNamaSiswa);
            NilaiSiswa = itemView.findViewById(R.id.edNilaiSiswa);
        }
    }
}

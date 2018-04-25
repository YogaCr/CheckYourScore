package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.SiswaClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

/**
 * Created by Sakata Yoga on 18/04/2018.
 */

public class InputNilaiAdapter extends RecyclerView.Adapter<InputNilaiAdapter.ViewHolder> {
    Context context;
    List<SiswaClass> siswa;

    public InputNilaiAdapter() {
    }

    public InputNilaiAdapter(Context context) {
        this.context = context;
    }

    public void setSiswa(List<SiswaClass> siswa) {
        this.siswa = siswa;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.input_nilai, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNama.setText(siswa.get(position).getNama());

    }

    @Override
    public int getItemCount() {
        return siswa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama;
        EditText etNilai;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvTambahNilaiNamaSiswa);
            etNilai = itemView.findViewById(R.id.etTambahNilai);
        }
    }
}

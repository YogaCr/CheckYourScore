package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.MapelClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity.MapelActivity;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity.MapelGuruActivity;

/**
 * Created by Sakata Yoga on 12/04/2018.
 */

public class MapelAdapter extends RecyclerView.Adapter<MapelAdapter.ViewHolder> {
    Context context;
    List<MapelClass> mapelList;
    boolean isGuru;

    public MapelAdapter(Context context, List<MapelClass> mapelList, boolean isGuru) {
        this.context = context;
        this.mapelList = mapelList;
        this.isGuru = isGuru;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mapel_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvNamaMapel.setText(mapelList.get(position).getNama());
        holder.tvKelasMapel.setText(mapelList.get(position).getKelas());
        holder.ivIcon.setImageResource(mapelList.get(position).getIconResource());
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        if (mapelList.get(position).getUrlSampul() != "") {
            Glide.with(context).load(mapelList.get(position).getUrlSampul()).apply(requestOptions).into(holder.ivSampul);
        }
        holder.cvMapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGuru) {
                    Intent i = new Intent(context, MapelGuruActivity.class);
                    i.putExtra("UniqueCode", mapelList.get(position).getUniqueCode());
                    context.startActivity(i);
                    ((Activity) context).finish();
                } else {
                    Intent i = new Intent(context, MapelActivity.class);
                    i.putExtra("UniqueCode", mapelList.get(position).getUniqueCode());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon, ivSampul;
        public TextView tvNamaMapel, tvKelasMapel;
        public CardView cvMapel;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivRvIconMapel);
            tvNamaMapel = itemView.findViewById(R.id.tvRvNamaMapel);
            tvKelasMapel = itemView.findViewById(R.id.tvRvKelasMapel);
            cvMapel = itemView.findViewById(R.id.cv_mapel);
            ivSampul = itemView.findViewById(R.id.ivRvSampulMapel);
        }
    }
}

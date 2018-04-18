package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UlanganFragment extends Fragment {

    RecyclerView rvNilaiUlangan;
    NilaiAdapter nilaiAdapter;
    List<NilaiClass> nilaiUlanganList = new ArrayList<>();  //menglist dari nilai class

    public UlanganFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ulangan, container, false);
        rvNilaiUlangan = v.findViewById(R.id.recyclerView);
        rvNilaiUlangan.setFocusable(false);
        nilaiUlanganList.add(new NilaiClass("Himpunan", 90, true));
        nilaiUlanganList.add(new NilaiClass("Fungsi", 65, false));
        nilaiUlanganList.add(new NilaiClass("SPLTV", 95, true));
        nilaiUlanganList.add(new NilaiClass("Transformasi", 50, false));
        nilaiUlanganList.add(new NilaiClass("Himpunan", 100, true));
        nilaiUlanganList.add(new NilaiClass("Fungsi", 80, true));
        nilaiUlanganList.add(new NilaiClass("SPLTV", 95, true));
        nilaiUlanganList.add(new NilaiClass("Transformasi", 70, false));

        nilaiAdapter = new NilaiAdapter(nilaiUlanganList, v.getContext());
        rvNilaiUlangan.setLayoutManager(new LinearLayoutManager(v.getContext()));
        rvNilaiUlangan.setAdapter(nilaiAdapter);
        return v;
    }

}

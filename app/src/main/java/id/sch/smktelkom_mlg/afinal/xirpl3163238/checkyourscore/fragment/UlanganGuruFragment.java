package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.TugasGuruClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter.TugasGuruAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class UlanganGuruFragment extends Fragment {

    RecyclerView rvUlangan;
    List<TugasGuruClass> list = new ArrayList<>();
    FirebaseFirestore firestore;
    TugasGuruAdapter adapter;
    String uniqueCode;
    TextView tvNone;
    ProgressBar progressBar;

    public UlanganGuruFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_ulangan_guru, container, false);
        setHasOptionsMenu(true);
        rvUlangan = v.findViewById(R.id.rvUlanganGuru);
        progressBar = v.findViewById(R.id.pbFragUlanganGuru);
        uniqueCode = getActivity().getIntent().getStringExtra("UniqueCode");
        firestore = FirebaseFirestore.getInstance();
        tvNone = v.findViewById(R.id.tvUlanganGuruNone);
        getData();
        return v;
    }

    void getData() {
        tvNone.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        firestore.collection("Mapel").document(uniqueCode).collection("Bab").whereEqualTo("Tugas", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            TugasGuruClass guruClass = new TugasGuruClass();
                            guruClass.setNama(ds.getString("Nama"));
                            guruClass.setID(ds.getId());
                            list.add(guruClass);
                        }
                        adapter = new TugasGuruAdapter(list, getContext());
                        adapter.notifyDataSetChanged();
                        rvUlangan.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvUlangan.setAdapter(adapter);
                    } else {
                        tvNone.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mapelRefresh:
                getData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

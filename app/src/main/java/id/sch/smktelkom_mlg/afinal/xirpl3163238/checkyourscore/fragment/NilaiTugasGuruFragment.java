package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
public class NilaiTugasGuruFragment extends Fragment {

    RecyclerView rvTugas;
    TugasGuruAdapter adapter;
    List<TugasGuruClass> listTugas = new ArrayList<>();
    FirebaseFirestore firestore;
    String uniqueCode;
    TextView tvNone;
    ProgressBar pbFrag;

    public NilaiTugasGuruFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nilai_tugas_guru, container, false);
        setHasOptionsMenu(true);
        rvTugas = v.findViewById(R.id.rvTugasGuru);
        tvNone = v.findViewById(R.id.tvTugasGuruNone);
        pbFrag = v.findViewById(R.id.pbFragTugasGuru);
        uniqueCode = getActivity().getIntent().getStringExtra("UniqueCode");
        getData();
        return v;
    }

    void getData() {

        tvNone.setVisibility(View.INVISIBLE);
        pbFrag.setVisibility(View.VISIBLE);
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Mapel").document(uniqueCode).collection("Bab").whereEqualTo("Tugas", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                listTugas.clear();
                if (task.getResult().size() > 0) {

                    for (DocumentSnapshot ds : task.getResult()) {
                        TugasGuruClass guruClass = new TugasGuruClass();
                        guruClass.setNama(ds.getString("Nama"));
                        guruClass.setID(ds.getId());
                        listTugas.add(guruClass);
                    }
                    adapter = new TugasGuruAdapter(listTugas, getContext());
                    adapter.notifyDataSetChanged();
                    rvTugas.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvTugas.setAdapter(adapter);
                } else {
                    tvNone.setVisibility(View.VISIBLE);
                }
                pbFrag.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mapelRefresh) {
            getData();
        }
        return super.onOptionsItemSelected(item);
    }
}

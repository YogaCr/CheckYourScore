package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.NilaiClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter.NilaiAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TugasFragment extends Fragment {
    public static final int NOTIFICATION_ID = 10;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    RecyclerView rvNilaiUlangan;
    NilaiAdapter nilaiAdapter;
    List<NilaiClass> nilaiTugasList = new ArrayList<>();
    String uniqueCode;
    Double KKM;
    ProgressBar progressBar;
    TextView tvNone;

    public TugasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tugas, container, false);
        progressBar = v.findViewById(R.id.pbFragTugasSiswa);
        rvNilaiUlangan = v.findViewById(R.id.recyclerView);
        tvNone = v.findViewById(R.id.tvTugasSiswaNone);
        uniqueCode = getActivity().getIntent().getStringExtra("UniqueCode");
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Mapel").document(uniqueCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                KKM = task.getResult().getDouble("KKM");
            }
        });
        getData();
        return v;
    }

    void getData() {

        progressBar.setVisibility(View.VISIBLE);
        tvNone.setVisibility(View.INVISIBLE);
        firestore.collection("Mapel").document(uniqueCode).collection("Bab").whereEqualTo("Tugas", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                nilaiTugasList.clear();
                for (DocumentSnapshot ds : documentSnapshots) {
                    final String nama = ds.getString("Nama");
                    final String id = ds.getId();
                    firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(ds.getId()).collection("Nilai").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Boolean lulus;
                            lulus = task.getResult().getDouble("Nilai") >= KKM;
                            nilaiTugasList.add(new NilaiClass(nama, task.getResult().getDouble("Nilai"), lulus, id, uniqueCode));
                            nilaiAdapter = new NilaiAdapter(nilaiTugasList, getContext());
                            nilaiAdapter.notifyDataSetChanged();
                            rvNilaiUlangan.setLayoutManager(new LinearLayoutManager(getContext()));
                            rvNilaiUlangan.setAdapter(nilaiAdapter);
                            tvNone.setVisibility(View.INVISIBLE);
                        }

                    });

                }

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        if (nilaiTugasList.size() == 0) {
            tvNone.setVisibility(View.VISIBLE);
        }
    }

}

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.SiswaClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter.InputNilaiAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class IsiNilaiGuruFragment extends Fragment {

    RecyclerView recyclerView;
    List<SiswaClass> classes = new ArrayList<>();
    InputNilaiAdapter adapter;
    FirebaseFirestore firestore;
    String uniqueCode, idBab;

    public IsiNilaiGuruFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        uniqueCode = getActivity().getIntent().getStringExtra("uniqueCode");
        idBab = getActivity().getIntent().getStringExtra("idBab");
        View v = inflater.inflate(R.layout.fragment_isi_nilai_guru, container, false);
        recyclerView = v.findViewById(R.id.rvIsiNilaiSiswa);
        firestore = FirebaseFirestore.getInstance();
        getData();
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.inputnilaiSave:
                saveData();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void saveData() {
        int jumlah = recyclerView.getChildCount();
        for (int x = 0; x < jumlah; x++) {
            if (recyclerView.findViewHolderForLayoutPosition(x) instanceof InputNilaiAdapter.ViewHolder) {
                InputNilaiAdapter.ViewHolder viewHolder = (InputNilaiAdapter.ViewHolder) recyclerView.findViewHolderForLayoutPosition(x);
                Map<String, Object> data = new HashMap<>();
                if (viewHolder.etNilai.getText().toString().isEmpty()) {
                    data.put("Nilai", classes.get(x).getNilai());
                } else {
                    data.put("Nilai", Double.parseDouble(viewHolder.etNilai.getText().toString()));
                }
                firestore.collection("Mapel").document(getActivity().getIntent().getStringExtra("uniqueCode")).collection("Bab").document(getActivity().getIntent().getStringExtra("idBab")).collection("Nilai").document(classes.get(x).getUID()).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Update nilai sukses", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Update nilai gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                firestore.collection("JoinSiswa").whereEqualTo("Mapel", uniqueCode).whereEqualTo("UID", classes.get(x).getUID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            Map<String, Object> up = new HashMap<>();
                            up.put("Notif", true);
                            firestore.collection("JoinSiswa").document(ds.getId()).update(up);
                        }
                    }
                });

            }
        }
    }

    public void getData() {
        classes.clear();
        firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(idBab).collection("Nilai").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    final Double nilai = documentSnapshot.getDouble("Nilai");
                    firestore.collection("User").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            SiswaClass nilaiClass = new SiswaClass();
                            nilaiClass.setNama(task.getResult().getString("Nama"));
                            nilaiClass.setNilai(nilai);
                            nilaiClass.setUID(task.getResult().getId());
                            classes.add(nilaiClass);
                            adapter = new InputNilaiAdapter(getContext());
                            adapter.setSiswa(classes);
                            adapter.notifyDataSetChanged();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }
}

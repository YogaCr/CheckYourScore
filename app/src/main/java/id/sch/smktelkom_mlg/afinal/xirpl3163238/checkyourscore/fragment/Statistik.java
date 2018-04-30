package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.NilaiGrafikClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Statistik extends Fragment {
    FirebaseAuth mAuth;
    TextView tvError;
    LineGraphSeries<DataPoint> data;
    DataPoint[] values;
    List<NilaiGrafikClass> list = new ArrayList<>();
    GraphView graphView;
    Spinner spnJenis;
    FirebaseFirestore firestore;
    String uniqueCode;


    public Statistik() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_statistik, container, false);
        mAuth = FirebaseAuth.getInstance();
        graphView = v.findViewById(R.id.graphSiswa);
        tvError = v.findViewById(R.id.tvErrorStatistikSiswa);
        spnJenis = v.findViewById(R.id.spnStatSiswaJenis);
        firestore = FirebaseFirestore.getInstance();
        uniqueCode = getActivity().getIntent().getStringExtra("UniqueCode");
        spnJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnJenis.getSelectedItemPosition() == 0) {
                    getData(true);
                } else {
                    getData(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    void getData(final boolean tugas) {
        list.clear();
        graphView.removeAllSeries();
        firestore.collection("Mapel").document(uniqueCode).collection("Bab").orderBy("WaktuBuat", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    getTugas(tugas);
                } else {
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getTugas(boolean tugas) {
        firestore.collection("Mapel")
                .document(uniqueCode)
                .collection("Bab")
                .whereEqualTo("Tugas", tugas)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult()) {
                        final String nama = ds.getString("Nama");
                        final int size = task.getResult().size();
                        firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(ds.getId()).collection("Nilai").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    NilaiGrafikClass nilaiGrafikClass = new NilaiGrafikClass();
                                    nilaiGrafikClass.setNama(nama);
                                    nilaiGrafikClass.setNilai(task.getResult().getDouble("Nilai"));
                                    list.add(nilaiGrafikClass);
                                    if (list.size() == size) {
                                        if (list.size() == 1) {
                                            graphView.setVisibility(View.INVISIBLE);
                                            tvError.setText("Membutuhkan lebih dari 1 data");
                                            tvError.setVisibility(View.VISIBLE);
                                        } else {
                                            tvError.setVisibility(View.INVISIBLE);
                                            graphView.setVisibility(View.VISIBLE);
                                            values = new DataPoint[list.size()];
                                            for (int i = 0; i < values.length; i++) {
                                                values[i] = new DataPoint(i + 1, list.get(i).getNilai());
                                            }
                                            data = new LineGraphSeries<>(values);
                                            graphView.addSeries(data);
                                            final String[] label = new String[list.size()];
                                            for (int i = 0; i < label.length; i++) {
                                                label[i] = list.get(i).getNama();
                                            }

                                            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                                                @Override
                                                public String formatLabel(double value, boolean isValueX) {

                                                    if (isValueX) {
                                                        return label[((int) value) - 1];
                                                    }

                                                    return super.formatLabel(value, isValueX);
                                                }
                                            });
                                            graphView.getViewport().setMaxX(list.size());
                                            graphView.getViewport().setMinY(0);
                                            graphView.getViewport().setMaxY(120);
                                            graphView.getViewport().setXAxisBoundsManual(true);
                                            graphView.getViewport().setYAxisBoundsManual(true);
                                        }
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            }
        });

        if (list.size() == 0)

        {
            graphView.setVisibility(View.INVISIBLE);
            tvError.setText("Tidak ada data");
            tvError.setVisibility(View.VISIBLE);
        }
    }

}

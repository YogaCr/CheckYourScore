package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.NilaiGrafikClass;

public class StatistikActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView tvError;
    BarGraphSeries<DataPoint> data;
    DataPoint[] values;
    List<NilaiGrafikClass> list = new ArrayList<>();
    GraphView graphView;
    Spinner spnJenis;
    FirebaseFirestore firestore;
    String uniqueCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik);
        mAuth = FirebaseAuth.getInstance();
        graphView = findViewById(R.id.graphSiswa);

        graphView.getViewport().setScrollable(true);
        tvError = findViewById(R.id.tvErrorStatistikSiswa);
        spnJenis = findViewById(R.id.spnStatSiswaJenis);
        firestore = FirebaseFirestore.getInstance();
        uniqueCode = getIntent().getStringExtra("UniqueCode");
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
                    Toast.makeText(StatistikActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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
                                            final String[] label = new String[list.size()];
                                            for (int i = 0; i < label.length; i++) {
                                                label[i] = list.get(i).getNama().substring(0, 4);
                                            }
                                            String[] labelY = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
                                            StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(graphView, label, labelY);
                                            graphView.getGridLabelRenderer().setLabelFormatter(labelsFormatter);
                                            graphView.getGridLabelRenderer().setNumHorizontalLabels(label.length);
                                            graphView.getViewport().setMinY(0);
                                            graphView.getViewport().setMaxY(100);
                                            graphView.getViewport().setYAxisBoundsManual(true);
                                            data = new BarGraphSeries<>(values);
                                            data.setSpacing(50);
                                            graphView.addSeries(data);

                                        }
                                    }
                                } else {
                                    Toast.makeText(StatistikActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

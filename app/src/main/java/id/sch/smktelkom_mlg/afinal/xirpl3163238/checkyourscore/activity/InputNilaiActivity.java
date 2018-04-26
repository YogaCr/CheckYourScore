package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

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

public class InputNilaiActivity extends AppCompatActivity {
    List<SiswaClass> siswaClasses = new ArrayList<>();
    RecyclerView rvSiswa;
    InputNilaiAdapter adapter;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    String uniqueCode, materi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_nilai);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu");
        progressDialog.show();
        Intent i = getIntent();
        rvSiswa = findViewById(R.id.rvTambahNilai);
        adapter = new InputNilaiAdapter(this);
        rvSiswa.setLayoutManager(new LinearLayoutManager(InputNilaiActivity.this));
        firestore = FirebaseFirestore.getInstance();
        uniqueCode = i.getStringExtra("UniqueCode");
        materi = i.getStringExtra("IDMateri");
        firestore.collection("Mapel").document(uniqueCode).collection("JoinSiswa").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult()) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("Nilai", 0);
                        firestore.collection("Mapel").document(uniqueCode).collection("JoinSiswa").document(ds.getString("UID")).collection("Nilai").document(materi).set(data);
                        firestore.collection("User").document("pdFyA0m4RqWadX15WmdP").collection("Siswa").document(ds.getString("UID")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    SiswaClass siswaClass = new SiswaClass();
                                    siswaClass.setUID(task.getResult().getId());
                                    siswaClass.setNama(task.getResult().getString("Nama"));
                                    siswaClasses.add(siswaClass);
                                    adapter.setSiswa(siswaClasses);
                                    adapter.notifyDataSetChanged();
                                    rvSiswa.setLayoutManager(new LinearLayoutManager(InputNilaiActivity.this));
                                    rvSiswa.setAdapter(adapter);
                                }
                            }
                        });
                    }
                    progressDialog.hide();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input_nilai, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.inputnilaiSave:
                simpanNilai();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void simpanNilai() {

        int jumlah = rvSiswa.getChildCount();
        for (int x = 0; x < jumlah; x++) {
            if (rvSiswa.findViewHolderForLayoutPosition(x) instanceof InputNilaiAdapter.ViewHolder) {
                InputNilaiAdapter.ViewHolder viewHolder = (InputNilaiAdapter.ViewHolder) rvSiswa.findViewHolderForLayoutPosition(x);
                Map<String, Object> data = new HashMap<>();
                if (viewHolder.etNilai.getText().toString().isEmpty()) {
                    data.put("Nilai", siswaClasses.get(x).getNilai());
                } else {
                    data.put("Nilai", Double.parseDouble(viewHolder.etNilai.getText().toString()));
                }
                firestore.collection("Mapel").document(uniqueCode).collection("JoinSiswa").document(siswaClasses.get(x).getUID()).collection("Nilai").document(materi).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(InputNilaiActivity.this, MapelGuruActivity.class);
                        i.putExtra("UniqueCode", uniqueCode);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }
    }
}

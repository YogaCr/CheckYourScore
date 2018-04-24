package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InputNilaiActivity extends AppCompatActivity {
    List<SiswaClass> siswaClasses = new ArrayList<>();
    RecyclerView rvSiswa;
    InputNilaiAdapter adapter;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

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
        String uniqueCode = i.getStringExtra("UniqueCode");
        firestore.collection("JoinMapel").whereEqualTo("KodeKelas", uniqueCode).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult()) {

                        firestore.collection("Siswa").document(ds.getString("Siswa")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()) {
                                    SiswaClass siswaClass = new SiswaClass();
                                    siswaClass.setUID(task.getResult().getId());
                                    siswaClass.setNama(task.getResult().getString("Nama"));
                                    siswaClasses.add(siswaClass);
                                    Toast.makeText(InputNilaiActivity.this, siswaClasses.get(0).getNama(), Toast.LENGTH_SHORT).show();
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

            }
        }
    }
}

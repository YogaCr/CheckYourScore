package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

public class TambahBabActivity extends AppCompatActivity {
    Spinner spnLinkRemidi, spnJenis;
    Intent i;
    Button btnSelesai;
    View itRemidi;
    EditText etLinkRemidi, etNamaBab, etPesanRemidi;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_bab);
        this.setTitle("Tambah Nilai");
        firestore = FirebaseFirestore.getInstance();
        etNamaBab = findViewById(R.id.etTambahNilaiNama);
        etPesanRemidi = findViewById(R.id.etTambahNilaiRemidi);
        btnSelesai = findViewById(R.id.btnTambahNilaiSubmit);
        spnLinkRemidi = findViewById(R.id.spnTambahButuhLink);
        itRemidi = findViewById(R.id.tilRemidiLink);
        spnJenis = findViewById(R.id.spnTambahBabJenis);
        etLinkRemidi = findViewById(R.id.etTambahLinkRemidi);
        spnLinkRemidi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnLinkRemidi.getSelectedItemPosition() == 0) {
                    itRemidi.setVisibility(View.INVISIBLE);
                } else {
                    itRemidi.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        i = getIntent();
        final String uniqueCode = i.getStringExtra("UniqueCode");
        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNamaBab.getText().toString() == "") {
                    etNamaBab.setError("Tolong masukkan nama materi");
                } else if ((spnLinkRemidi.getSelectedItemPosition() == 1 && etLinkRemidi.getText().toString() == "") || (spnLinkRemidi.getSelectedItemPosition() == 1 && !Patterns.WEB_URL.matcher(etLinkRemidi.getText().toString()).matches())) {
                    etPesanRemidi.setError("Tolong masukkan link remidi dengan benar");
                } else {

                    Map<String, Object> data = new HashMap<>();
                    data.put("Nama", etNamaBab.getText().toString());
                    if (spnJenis.getSelectedItemPosition() == 0) {
                        data.put("Tugas", true);
                    } else {
                        data.put("Tugas", false);
                    }
                    if (etPesanRemidi.getText().toString() != "") {
                        data.put("PesanRemidi", etPesanRemidi.getText().toString());
                    } else {
                        data.put("PesanRemidi", "-");
                    }
                    if (spnLinkRemidi.getSelectedItemPosition() == 1) {
                        data.put("URLRemidi", etLinkRemidi.getText().toString());
                    }
                    firestore.collection("Mapel").document(uniqueCode).collection("Bab").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Intent in = new Intent(TambahBabActivity.this, InputNilaiActivity.class);
                                in.putExtra("IDMateri", task.getResult().getId());
                                in.putExtra("UniqueCode", uniqueCode);
                                startActivity(in);
                                finish();
                            }
                        }
                    });


                }
            }
        });
    }
}

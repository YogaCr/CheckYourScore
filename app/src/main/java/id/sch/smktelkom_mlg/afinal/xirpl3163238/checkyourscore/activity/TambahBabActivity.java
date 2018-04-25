package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

public class TambahBabActivity extends AppCompatActivity {
    Spinner spnLinkRemidi, spnJenis;
    Intent i;
    Button btnSelesai;
    View itRemidi;
    EditText etLinkRemidi, etNamaBab, etPesanRemidi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_bab);
        this.setTitle("Tambah Nilai");
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
                    Intent in = new Intent(TambahBabActivity.this, InputNilaiActivity.class);
                    in.putExtra("UniqueCode", uniqueCode);
                    if (spnJenis.getSelectedItemPosition() == 0) {
                        in.putExtra("Tugas", true);
                    } else {
                        in.putExtra("Tugas", false);
                    }
                    if (etPesanRemidi.getText().toString() != "") {
                        in.putExtra("PesanRemidi", etPesanRemidi.getText().toString());
                    }
                    if (spnLinkRemidi.getSelectedItemPosition() == 1 && etLinkRemidi.getText().toString() != "") {
                        in.putExtra("LinkRemidi", etLinkRemidi.getText().toString());
                    }
                    startActivity(in);
                }
            }
        });
    }
}

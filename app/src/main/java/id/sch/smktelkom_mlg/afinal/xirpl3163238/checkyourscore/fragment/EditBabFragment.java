package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity.MapelGuruActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditBabFragment extends Fragment {
    String uniqueCode, idBab;
    FirebaseFirestore firestore;
    Spinner spnLinkRemidi, spnJenis;
    Button btnSelesai;
    View itRemidi;
    EditText etLinkRemidi, etNamaBab, etPesanRemidi;

    public EditBabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_bab, container, false);
        setHasOptionsMenu(true);
        firestore = FirebaseFirestore.getInstance();
        etNamaBab = v.findViewById(R.id.etTambahNilaiNama);
        etPesanRemidi = v.findViewById(R.id.etTambahNilaiRemidi);
        btnSelesai = v.findViewById(R.id.btnTambahNilaiSubmit);
        spnLinkRemidi = v.findViewById(R.id.spnTambahButuhLink);
        itRemidi = v.findViewById(R.id.tilRemidiLink);
        spnJenis = v.findViewById(R.id.spnTambahBabJenis);
        etLinkRemidi = v.findViewById(R.id.etTambahLinkRemidi);
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
        uniqueCode = getActivity().getIntent().getStringExtra("uniqueCode");
        idBab = getActivity().getIntent().getStringExtra("idBab");
        getData();
        return v;
    }

    void getData() {
        firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(idBab).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                etNamaBab.setText(task.getResult().getString("Nama"));
                etPesanRemidi.setText(task.getResult().getString("PesanRemidi"));
                if (task.getResult().getBoolean("Tugas")) {
                    spnJenis.setSelection(0);
                } else {
                    spnJenis.setSelection(1);
                }
                if (task.getResult().contains("LinkRemidi")) {
                    spnLinkRemidi.setSelection(1);
                    etLinkRemidi.setText(task.getResult().getString("LinkRemidi"));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.inputnilaiSave:
                saveData();
                return true;
            case R.id.inputnilaiDelete:
                hapusBab();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void hapusBab() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Yakin mau menghapus?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(idBab).delete();
                Intent i = new Intent(getContext(), MapelGuruActivity.class);
                i.putExtra("UniqueCode", uniqueCode);
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void saveData() {
        if (etNamaBab.getText().toString().isEmpty()) {
            etNamaBab.setError("Tolong masukkan nama materi");
        } else if ((spnLinkRemidi.getSelectedItemPosition() == 1 && etLinkRemidi.getText().toString().isEmpty()) || (spnLinkRemidi.getSelectedItemPosition() == 1 && !Patterns.WEB_URL.matcher(etLinkRemidi.getText().toString()).matches())) {
            etLinkRemidi.setError("Tolong masukkan link remidi dengan benar");
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("Nama", etNamaBab.getText().toString());
            data.put("PesanRemidi", etPesanRemidi.getText().toString());
            if (spnJenis.getSelectedItemPosition() == 0) {
                data.put("Tugas", true);
            } else {
                data.put("Tugas", false);
            }
            if (spnLinkRemidi.getSelectedItemPosition() == 1 && !etLinkRemidi.getText().toString().isEmpty()) {
                data.put("LinkRemidi", etLinkRemidi.getText().toString());
            } else if (spnLinkRemidi.getSelectedItemPosition() == 0) {
                data.remove("LinkRemidi");
            }
            firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(idBab).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Update Bab Sukses", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Update Bab Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
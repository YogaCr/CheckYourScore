package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter.CustomAdapter;

public class EditMapelActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
    String uniqueCode;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    LinearLayout layoutBuatMapel;
    EditText etNama, etKelas, etKKM;
    StorageReference storageReference;
    ImageView ivSampul;
    ProgressDialog progressDialog;

    int icons[] = {R.drawable.bahasa, R.drawable.biologi, R.drawable.kimia, R.drawable.mat, R.drawable.music, R.drawable.sejarah, R.drawable.seni, R.drawable.sosial, R.drawable.tik};
    Spinner spin;
    List<String> nama = new ArrayList<>();
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mapel);
        for (int x = 0; x < icons.length; x++) {
            nama.add(getResources().getResourceName(icons[x]));
        }
        progressDialog = new ProgressDialog(EditMapelActivity.this);
        progressDialog.setMessage("Sedang Mengupload");
        uniqueCode = getIntent().getStringExtra("UniqueCode");
        layoutBuatMapel = findViewById(R.id.layoutBuatMapel);
        spin = findViewById(R.id.spinnerIcon);
        ivSampul = findViewById(R.id.ivSampul);
        etNama = findViewById(R.id.etBuatMapelNama);
        etKelas = findViewById(R.id.etBuatMapelKelas);
        etKKM = findViewById(R.id.etBuatMapelKKM);
        CustomAdapter customAdapter = new CustomAdapter(this, icons);
        spin.setAdapter(customAdapter);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getData();
        findViewById(R.id.btnPilihGambar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        findViewById(R.id.btnBuatMapel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNama.getText().toString().isEmpty()) {
                    etNama.setError("Tolong masukkan nama mapel");
                } else if (etKelas.getText().toString().isEmpty()) {
                    etKelas.setError("Tolong masukkan kelas");
                } else if (etKKM.getText().toString().isEmpty()) {
                    etKKM.setError("Tolong masukkan KKM");
                } else {
                    progressDialog.show();
                    final Map<String, Object> data = new HashMap<>();
                    String icon = getResources().getResourceName(icons[spin.getSelectedItemPosition()]);
                    String nama = etNama.getText().toString();
                    String kelas = etKelas.getText().toString();

                    data.put("Nama", nama);
                    data.put("Kelas", kelas);
                    data.put("Icon", icon);
                    data.put("UID Guru", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.put("KKM", Double.parseDouble(etKKM.getText().toString()));
                    if (filePath != null) {
                        StorageReference ref = storageReference.child("images/Sampul/" + UUID.randomUUID().toString());
                        ref.putFile(filePath)
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        data.put("Sampul", task.getResult().getDownloadUrl().toString());
                                        firestore.collection("Mapel").document(uniqueCode).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent i = new Intent(EditMapelActivity.this, MapelGuruActivity.class);
                                                    i.putExtra("UniqueCode", uniqueCode);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                });
                    } else {

                        firestore.collection("Mapel").document(uniqueCode).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent i = new Intent(EditMapelActivity.this, MapelGuruActivity.class);
                                    i.putExtra("UniqueCode", uniqueCode);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    void getData() {
        progressDialog.show();
        firestore.collection("Mapel").document(uniqueCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                etNama.setText(task.getResult().getString("Nama"));
                etKelas.setText(task.getResult().getString("Kelas"));
                etKKM.setText(String.valueOf(task.getResult().getDouble("KKM")));
                int location = nama.indexOf(task.getResult().getString("Icon"));
                spin.setSelection(location);
                if (task.getResult().contains("Sampul")) {
                    Glide.with(EditMapelActivity.this).load(task.getResult().getString("Sampul")).into(ivSampul);
                }
                progressDialog.hide();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivSampul.setImageBitmap(bitmap);
            } catch (IOException e) {

            }
        }
    }
}

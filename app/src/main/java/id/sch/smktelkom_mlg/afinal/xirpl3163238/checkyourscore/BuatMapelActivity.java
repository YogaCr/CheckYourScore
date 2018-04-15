package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

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
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class BuatMapelActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ProgressBar pbBuatMapel;
    LinearLayout layoutBuatMapel;
    EditText etNama, etKelas;
    StorageReference storageReference;
    ImageView ivSampul;
    int icons[] = {R.drawable.student_100px};
    Spinner spin;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_mapel);
        pbBuatMapel = findViewById(R.id.pBBuatMapel);
        layoutBuatMapel = findViewById(R.id.layoutBuatMapel);
        spin = findViewById(R.id.spinnerIcon);
        ivSampul = findViewById(R.id.ivSampul);
        etNama = findViewById(R.id.etBuatMapelNama);
        etKelas = findViewById(R.id.etBuatMapelKelas);
        CustomAdapter customAdapter = new CustomAdapter(this, icons);
        spin.setAdapter(customAdapter);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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
                if (filePath != null) {
                    ProgressDialog progressDialog = new ProgressDialog(BuatMapelActivity.this);
                    progressDialog.setMessage("Sedang Mengupload");
                    progressDialog.show();
                    StorageReference ref = storageReference.child("images/Sampul/" + UUID.randomUUID().toString());
                    ref.putFile(filePath).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String icon = getResources().getResourceName(icons[spin.getSelectedItemPosition()]);
                            String nama = etNama.getText().toString();
                            String kelas = etKelas.getText().toString();
                            Map<String, Object> data = new HashMap<>();
                            data.put("Nama", nama);
                            data.put("Kelas", kelas);
                            data.put("Icon", icon);
                            data.put("UID Guru", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            data.put("Sampul", taskSnapshot.getDownloadUrl().toString());
                            Random r = new Random();
                            String abjad = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                            String kode = "";
                            for (int x = 0; x < 6; x++) {
                                int y = r.nextInt(abjad.length());
                                kode += abjad.charAt(y);
                            }
                            firestore.collection("Mapel").document(kode).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(BuatMapelActivity.this, MenuGuruActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }
                    });
                }
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

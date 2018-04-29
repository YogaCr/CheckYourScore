package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    FirebaseAuth mAuth;
    CardView layoutPilihLogin, layoutLogin, layoutSignUp, layoutVerifikasi, layoutSetupPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String pref_key = "IS_GURU";
    int RC_Login = 1;
    Animation animRightLeftExit, animRightLeftEntrance;
    ProgressBar progressBarLogin;
    EditText etEmailLogIn, etPasswordLogIn, etNamaSignUp, etEmailSignUp, etPasswordSignUp, etSetupPassword;
    boolean guru, useEmailPassword;
    GoogleApiClient googleApiClient;
    GoogleSignInOptions gso;
    FirebaseFirestore firestore;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        layoutPilihLogin = findViewById(R.id.PilihLogin);
        layoutLogin = findViewById(R.id.LoginLayout);
        layoutSignUp = findViewById(R.id.SignUpLayout);
        layoutVerifikasi = findViewById(R.id.VerifikasiLayout);
        layoutSetupPassword = findViewById(R.id.LoginSetPassword);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        sharedPreferences = getSharedPreferences(pref_key, MODE_PRIVATE);
        etEmailLogIn = findViewById(R.id.SignInEmail);
        etPasswordLogIn = findViewById(R.id.SignInPassword);
        etNamaSignUp = findViewById(R.id.signUpNama);
        etEmailSignUp = findViewById(R.id.signUpEmail);
        etPasswordSignUp = findViewById(R.id.signUpPassword);
        etSetupPassword = findViewById(R.id.SignInAturPassword);

        animRightLeftEntrance = AnimationUtils.loadAnimation(this, R.anim.righttoleftentrance);
        animRightLeftExit = AnimationUtils.loadAnimation(this, R.anim.righttoleftexit);
        connectingGoogleApiClient();

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (googleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                }
                Intent googleLogin = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(googleLogin, RC_Login);
            }
        });
        findViewById(R.id.btnLoginAturPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSetupPassword.getText().toString().isEmpty()) {
                    etSetupPassword.setError("Masukkan password");
                } else {
                    mAuth.getCurrentUser().updatePassword(etSetupPassword.getText().toString());
                    Map<String, Object> data = new HashMap<>();
                    data.put("Nama", mAuth.getCurrentUser().getDisplayName());
                    firestore.collection("User").document(mAuth.getCurrentUser().getUid()).set(data);
                    if (guru) {
                        afterLoginGuru();
                    } else {
                        afterLoginSiswa();
                    }

                }
            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() != null) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        if (sharedPreferences.contains(pref_key)) {
                            if (sharedPreferences.getBoolean(pref_key, true)) {
                                afterLoginGuru();
                            } else {
                                afterLoginSiswa();
                            }
                        }
                    } else {
                        layoutVerifikasi.startAnimation(animRightLeftEntrance);
                        layoutVerifikasi.setVisibility(View.VISIBLE);
                    }
                } else {
                    layoutPilihLogin.startAnimation(animRightLeftEntrance);
                    layoutPilihLogin.setVisibility(View.VISIBLE);
                }
            }
        };
        findViewById(R.id.tvKembaliPilih).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLogin.startAnimation(animRightLeftExit);
                layoutPilihLogin.startAnimation(animRightLeftEntrance);
                layoutLogin.setVisibility(View.INVISIBLE);
                layoutPilihLogin.setVisibility(View.VISIBLE);
            }
        });
    }


    void connectingGoogleApiClient() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    void afterLoginSiswa() {
        Intent i = new Intent(LoginActivity.this, MenuSiswaActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    void afterLoginGuru() {
        Intent i = new Intent(LoginActivity.this, MenuGuruActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_Login) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                progressBarLogin.setVisibility(View.VISIBLE);
                layoutLogin.setVisibility(View.INVISIBLE);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        editor = sharedPreferences.edit();
                        if (guru) {
                            editor.putBoolean(pref_key, true);
                            editor.apply();
                            firestore.collection("User").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().exists()) {
                                            for (UserInfo userInfo : mAuth.getCurrentUser().getProviderData()) {
                                                if (userInfo.getProviderId().equals("password")) {
                                                    useEmailPassword = true;
                                                    break;
                                                }
                                            }
                                            if (useEmailPassword) {
                                                Map<String, Object> data = new HashMap<>();
                                                data.put("Nama", mAuth.getCurrentUser().getDisplayName());
                                                firestore.collection("User").document(mAuth.getCurrentUser().getUid()).set(data);
                                                afterLoginGuru();
                                            } else {
                                                layoutLogin.startAnimation(animRightLeftExit);
                                                layoutSetupPassword.startAnimation(animRightLeftEntrance);
                                                layoutLogin.setVisibility(View.INVISIBLE);
                                                layoutSetupPassword.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            if (mAuth.getCurrentUser().getDisplayName() != task.getResult().getString("Nama")) {
                                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(task.getResult().getString("Nama")).build();
                                                mAuth.getCurrentUser().updateProfile(profileChangeRequest);
                                                afterLoginGuru();
                                            }
                                        }
                                    }
                                }
                            });

                        } else {
                            editor.putBoolean(pref_key, false);
                            editor.apply();
                            firestore.collection("User").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().exists()) {
                                            for (UserInfo userInfo : mAuth.getCurrentUser().getProviderData()) {
                                                if (userInfo.getProviderId().equals("password")) {
                                                    useEmailPassword = true;
                                                    break;
                                                }
                                            }
                                            if (useEmailPassword) {
                                                Map<String, Object> data = new HashMap<>();
                                                data.put("Nama", mAuth.getCurrentUser().getDisplayName());
                                                firestore.collection("User").document(mAuth.getCurrentUser().getUid()).set(data);
                                                afterLoginSiswa();
                                            } else {
                                                layoutLogin.startAnimation(animRightLeftExit);
                                                layoutSetupPassword.startAnimation(animRightLeftEntrance);
                                                layoutLogin.setVisibility(View.INVISIBLE);
                                                layoutSetupPassword.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            if (mAuth.getCurrentUser().getDisplayName() != task.getResult().getString("Nama")) {
                                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(task.getResult().getString("Nama")).build();
                                                mAuth.getCurrentUser().updateProfile(profileChangeRequest);
                                                afterLoginSiswa();
                                            }
                                        }
                                    }
                                }
                            });

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.loginView), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        layoutLogin.setVisibility(View.VISIBLE);
                        layoutLogin.startAnimation(animRightLeftEntrance);
                    }
                });
            }
        }
    }

    public void pilihLogin(View v) {
        switch (v.getId()) {
            case R.id.btnPilihGuru:
                guru = true;
                break;
            case R.id.btnPilihSiswa:
                guru = false;
                break;
            default:
                break;
        }
        layoutPilihLogin.startAnimation(animRightLeftExit);
        layoutLogin.startAnimation(animRightLeftEntrance);
        layoutPilihLogin.setVisibility(View.INVISIBLE);
        layoutLogin.setVisibility(View.VISIBLE);
    }

    public void gotoMasukDaftar(View v) {
        switch (v.getId()) {
            case R.id.gotosignUpButton:
                layoutLogin.startAnimation(animRightLeftExit);
                layoutSignUp.startAnimation(animRightLeftEntrance);
                layoutSignUp.setVisibility(View.VISIBLE);
                layoutLogin.setVisibility(View.INVISIBLE);

                break;
            case R.id.gotologInButton:
                layoutSignUp.startAnimation(animRightLeftExit);
                layoutLogin.startAnimation(animRightLeftEntrance);
                layoutLogin.setVisibility(View.VISIBLE);
                layoutSignUp.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    public void prosesMasukDaftar(View v) {
        switch (v.getId()) {
            case R.id.signUpButton:
                if (etNamaSignUp.getText().toString().equals("")) {
                    etNamaSignUp.setError("Tolong masukkan nama anda");
                } else if (etEmailSignUp.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(etEmailSignUp.getText().toString()).matches()) {
                    etEmailSignUp.setError("Tolong masukkan email dengan benar");
                } else if (etPasswordSignUp.getText().length() < 6 || etPasswordSignUp.getText().length() > 12) {
                    etPasswordSignUp.setError("Password haruslah tidak kurang dari 6 digit atau tidak lebih dari 12 digit");
                } else {
                    progressBarLogin.setVisibility(View.VISIBLE);
                    layoutSignUp.setVisibility(View.INVISIBLE);
                    mAuth.createUserWithEmailAndPassword(etEmailSignUp.getText().toString(), etPasswordSignUp.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(etNamaSignUp.getText().toString()).build();
                            mAuth.getCurrentUser().updateProfile(userProfileChangeRequest);
                            Map<String, Object> data = new HashMap<>();
                            data.put("Nama", etNamaSignUp.getText().toString());
                            firestore.collection("User").document(mAuth.getCurrentUser().getUid()).set(data);
                            mAuth.getCurrentUser().sendEmailVerification();
                            editor = sharedPreferences.edit();
                            if (guru) {
                                editor.putBoolean(pref_key, true);
                            } else {
                                editor.putBoolean(pref_key, false);
                            }
                            editor.apply();
                            progressBarLogin.setVisibility(View.INVISIBLE);
                            layoutVerifikasi.startAnimation(animRightLeftEntrance);
                            layoutVerifikasi.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarLogin.setVisibility(View.INVISIBLE);
                            layoutSignUp.setVisibility(View.VISIBLE);
                            layoutSignUp.startAnimation(animRightLeftEntrance);
                            Snackbar.make(findViewById(R.id.loginView), e.getMessage().toString(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.signInButton:
                if (etEmailLogIn.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(etEmailLogIn.getText().toString()).matches()) {
                    etEmailLogIn.setError("Tolong masukkan email dengan benar");
                } else if (etPasswordLogIn.getText().length() < 6 || etPasswordLogIn.getText().length() > 12) {
                    etPasswordLogIn.setError("Password haruslah tidak kurang dari 6 digit atau tidak lebih dari 12 digit");
                } else {
                    progressBarLogin.setVisibility(View.VISIBLE);
                    layoutLogin.setVisibility(View.INVISIBLE);
                    mAuth.signInWithEmailAndPassword(etEmailLogIn.getText().toString(), etPasswordLogIn.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            editor = sharedPreferences.edit();
                            if (guru) {
                                editor.putBoolean(pref_key, true);
                            } else {
                                editor.putBoolean(pref_key, false);
                            }
                            editor.apply();
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                if (guru) {
                                    afterLoginGuru();
                                } else {
                                    afterLoginSiswa();
                                }
                            } else {
                                progressBarLogin.setVisibility(View.INVISIBLE);
                                layoutVerifikasi.startAnimation(animRightLeftEntrance);
                                layoutVerifikasi.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarLogin.setVisibility(View.INVISIBLE);
                            layoutLogin.setVisibility(View.VISIBLE);
                            layoutLogin.startAnimation(animRightLeftEntrance);
                            Snackbar.make(findViewById(R.id.loginView), "Email atau password salah", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }

    public void buttonVerifikasi(View v) {
        switch (v.getId()) {
            case R.id.btnGoToMain:
                mAuth.getCurrentUser().reload();
                if (mAuth.getCurrentUser().isEmailVerified()) {
                    if (sharedPreferences.getBoolean(pref_key, true)) {
                        afterLoginGuru();
                    } else {
                        afterLoginSiswa();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.loginView), "Email anda belum diverifikasi", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnResendVerification:
                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(R.id.loginView), "Email verifikasi sudah dikirim", Snackbar.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.loginView), "Gagal mengirim email verifikasi", Snackbar.LENGTH_SHORT).show();
                    }
                });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(findViewById(R.id.loginView), connectionResult.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }
}
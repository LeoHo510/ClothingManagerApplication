package com.example.appmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.appmanager.R;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SignInActivity extends AppCompatActivity {
    TextInputEditText txtEmail, txtPassword;
    AppCompatButton btnSignIn;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signIn), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initControl();
    }

    private void initControl() {
        String username = Paper.book().read("email");
        String pass = Paper.book().read("password");
        txtEmail.setText(username);
        txtPassword.setText(pass);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (txtEmail.getText() == null) {
                        throw new IllegalArgumentException("Do Not Left Blank Username");
                    } else if (txtPassword.getText() == null) {
                        throw new IllegalArgumentException("Do Not Left Blank Password");
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        String email = txtEmail.getText().toString();
                        String pass = txtPassword.getText().toString();
                        if (user != null) {
                            firebaseAuth.signOut();
                        } else {
                            compositeDisposable.add(apiClothing.checkRule(email, pass)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {
                                                if (messageModel.isSuccess()) {
                                                    firebaseAuth.signInWithEmailAndPassword(email, pass)
                                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        login(email, pass);
                                                                        updatePass(email, pass);
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    MotionToast.Companion.createToast(SignInActivity.this,
                                                            "Notice",
                                                            Objects.requireNonNull(messageModel.getMessage()),
                                                            MotionToastStyle.WARNING,
                                                            MotionToast.GRAVITY_BOTTOM,
                                                            MotionToast.SHORT_DURATION,
                                                            ResourcesCompat.getFont(SignInActivity.this, R.font.helvetica_regular));
                                                }
                                            },
                                            throwable -> {
                                                MotionToast.Companion.createToast(SignInActivity.this,
                                                        "Notice",
                                                        Objects.requireNonNull(throwable.getMessage()),
                                                        MotionToastStyle.ERROR,
                                                        MotionToast.GRAVITY_BOTTOM,
                                                        MotionToast.SHORT_DURATION,
                                                        ResourcesCompat.getFont(SignInActivity.this, R.font.helvetica_regular));
                                            }
                                    ));
                        }
                    }
                } catch (IllegalArgumentException e) {
                    MotionToast.Companion.createToast(SignInActivity.this,
                            "Notice",
                            Objects.requireNonNull(e.getMessage()),
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(SignInActivity.this, R.font.helvetica_regular));
                }
            }
        });
    }

    private void initView() {
        Paper.init(getApplicationContext());
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        txtEmail = findViewById(R.id.txtEmailSI);
        txtPassword = findViewById(R.id.txtPassword);
        btnSignIn = findViewById(R.id.btnSignInNike);
        progressBar = findViewById(R.id.progressbar);
        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    public void login(String email, String password) {
        compositeDisposable.add(apiClothing.signIn(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                Paper.book().write("user_current", userModel.getResult().get(0));
                                MotionToast.Companion.createToast(SignInActivity.this,
                                        "Notice",
                                        "Sign In Success",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(SignInActivity.this, R.font.helvetica_regular));
                                Utils.user = Paper.book().read("user_current");
                                Paper.book().delete("email");
                                Paper.book().write("email", Utils.user.getEmail());
                                Paper.book().delete("password");
                                Paper.book().write("password", Utils.user.getPassword());
                                Log.d("UserCurrent", String.valueOf(Utils.user.getIduser()));
                                Intent signIn = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(signIn);
                                finish();
                            }
                        },
                        throwable -> {
                            MotionToast.Companion.createToast(SignInActivity.this,
                                    "Notice",
                                    throwable.getMessage(),
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(SignInActivity.this, R.font.helvetica_regular));
                        }
                ));
    }
    public void updatePass(String email, String password) {
        compositeDisposable.add(apiClothing.updatePass(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }
}
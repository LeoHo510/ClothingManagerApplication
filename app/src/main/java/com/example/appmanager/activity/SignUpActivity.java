package com.example.appmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText txtFirstName, txtLastName, txtAddress, txtEmail, txtPhoneNumber, txtPassword, txtRePassword;
    AppCompatButton btnSignUp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initControl();
    }

    private void initControl() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String fisrtname = txtFirstName.getText().toString();
                    String lastname = txtLastName.getText().toString();
                    String address = txtAddress.getText().toString();
                    String email = txtEmail.getText().toString();
                    String phonenumber = txtPhoneNumber.getText().toString();
                    String password = txtPassword.getText().toString();
                    String repassword = txtRePassword.getText().toString();
                    if (fisrtname.isEmpty()) {
                        throw new IllegalArgumentException("Do Not Left Blank First Name");
                    } else if (lastname.isEmpty()) {
                        throw new IllegalArgumentException("Do Not Left Blank Last Name");
                    } else if (address.isEmpty()) {
                        throw new IllegalArgumentException("Do Not Left Blank Address");
                    } else if (email.isEmpty()) {
                        throw new IllegalArgumentException("Do Not Left Blank Email");
                    } else if (phonenumber.isEmpty()) {
                        throw new IllegalArgumentException("Do Not Left Blank Phone Number");
                    } else if (password.isEmpty()) {
                        throw new IllegalArgumentException("Do Not Left Blank Password");
                    } else if (repassword.isEmpty()) {
                        throw new IllegalArgumentException("Do Not Left Blank Password");
                    } else if (!password.equalsIgnoreCase(repassword)) {
                        throw new IllegalArgumentException("Password must be the same");
                    } else {
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user =firebaseAuth.getCurrentUser();
                                            if (user != null) {
                                                createUser(fisrtname, lastname, address, phonenumber, email, password, user.getUid());
                                            }
                                        } else {
                                            MotionToast.Companion.createToast(SignUpActivity.this,
                                                    "Notice",
                                                    "Email has already exists!",
                                                    MotionToastStyle.WARNING,
                                                    MotionToast.GRAVITY_BOTTOM,
                                                    MotionToast.SHORT_DURATION,
                                                    ResourcesCompat.getFont(SignUpActivity.this, R.font.helvetica_regular));
                                        }
                                    }
                                });
                    }
                } catch (IllegalArgumentException e) {
                    MotionToast.Companion.createToast(SignUpActivity.this,
                            "Notice",
                            e.getMessage(),
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(SignUpActivity.this, R.font.helvetica_regular));
                }
            }
        });
    }

    private void initView() {
        Paper.init(getApplicationContext());
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        txtFirstName = findViewById(R.id.txtFirstNameSU);
        txtLastName = findViewById(R.id.txtLastNameSU);
        txtAddress = findViewById(R.id.txtAddressSU);
        txtEmail = findViewById(R.id.txtEmailSU);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumberSU);
        txtPassword = findViewById(R.id.txtPasswordSU);
        btnSignUp = findViewById(R.id.btnSignUpSU);
        txtRePassword = findViewById(R.id.txtRePasswordSU);
    }

    public void createUser(String fisrtname, String lastname, String address, String phonenumber, String email, String password, String uid) {
        compositeDisposable.add(apiClothing.signUp(fisrtname, lastname, address, phonenumber, email, password, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()) {
                                Paper.book().write("email", email);
                                Paper.book().write("password", password);
                                MotionToast.Companion.createToast(SignUpActivity.this,
                                        "Notice",
                                        "Sign Up Success",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(getApplicationContext(), R.font.helvetica_regular));
                                Intent login = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(login);
                                finish();
                            } else {
                                MotionToast.Companion.createToast(SignUpActivity.this,
                                        "Notice",
                                        messageModel.getMessage(),
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(SignUpActivity.this, R.font.helvetica_regular));
                            }
                        },
                        throwable -> {
                            MotionToast.Companion.createToast(SignUpActivity.this,
                                    "Notice",
                                    throwable.getMessage(),
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(SignUpActivity.this, R.font.helvetica_regular));
                        }
                ));
    }
}
package com.geniusmind.malek.clevermind.Splash_Login_Register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusmind.malek.clevermind.CheckConnection;
import com.geniusmind.malek.clevermind.HomeScreen.HomeActivity;
import com.geniusmind.malek.clevermind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String DIALOG_FORGET ="FORGET PASSWORD DIALOG";
    private static final String VERIFY_EMAIL="VERIFY EMAIL DIALOG";

    private EditText userName, password;
    private TextView forgetPass, signUp;
    private ImageButton loginBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // for casting and initalize component . .  ;
        initalizeComponents();

    }

    // initalize components . . ;
    private void initalizeComponents() {
        mAuth = FirebaseAuth.getInstance();
        linearLayout = findViewById(R.id.linear_progress_login);
        userName = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        forgetPass = findViewById(R.id.text_forget_pass);
        signUp = findViewById(R.id.text_signup);
        loginBtn = findViewById(R.id.login_btn);
        // listen for click . . ;
        forgetPass.setOnClickListener(this);
        signUp.setOnClickListener(this);
        loginBtn.setOnClickListener(this);


    }

    // create method return intent object for intent and pass data to this activity from other activity . . . ;
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    // listen to click in views and implement the process . . . ;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.text_forget_pass:
                FragmentManager fragmentManager=getSupportFragmentManager();
                ForgetPasswordDialog dialog=new ForgetPasswordDialog();
                dialog.show(fragmentManager,DIALOG_FORGET);
                break;

            case R.id.text_signup:
                startActivity(SignUpActivity.newIntent(this));
                break;

            case R.id.login_btn:
                //  if user is exist then login to home screen . . . ;
                if (CheckConnection.isConnectedToNetwork(this)) {
                    visibleProgress(true);
                    String email = userName.getText().toString();
                    String pass = password.getText().toString();
                    if (!email.isEmpty() || !pass.isEmpty()) {
                        loginUser(email, pass)
                        ;
                    } else {
                        visibleProgress(false);
                        Toast.makeText(this, R.string.fill_views, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
                }


                break;
        }

    }

    // for login user to home screen . . .  ;
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            visibleProgress(false);
                            if(isVerified()){
                            startActivity(HomeActivity.newIntent(LoginActivity.this));
                            Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "please verify your account", Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager=getSupportFragmentManager();
                                VerifyEmailDialog verifyEmailDialog=new VerifyEmailDialog();
                                verifyEmailDialog.show(fragmentManager,VERIFY_EMAIL);
                            }

                        } else {

                            visibleProgress(false);

                            try {
                                throw task.getException();

                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(LoginActivity.this, R.string.error_invalid_email_password, Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(LoginActivity.this, R.string.error_invalid_email_password, Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthRecentLoginRequiredException e) {
                                Toast.makeText(LoginActivity.this, R.string.check_connection, Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Log.e("Login authentication", e.getMessage());
                            }

                        }
                    }
                });
    }

    private void visibleProgress(boolean a) {
        if (a) {
            linearLayout.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            linearLayout.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    // check if user verified . . . .; 
    private boolean isVerified(){
        // get current user from Firebase User class . . . ;
        user=mAuth.getCurrentUser();
        // return boolean type( is email verfied ) . . . ;
        return user.isEmailVerified();
    }


}

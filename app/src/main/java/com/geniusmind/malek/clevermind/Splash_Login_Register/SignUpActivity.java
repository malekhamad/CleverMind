package com.geniusmind.malek.clevermind.Splash_Login_Register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geniusmind.malek.clevermind.CheckConnection;
import com.geniusmind.malek.clevermind.ContractUrl;
import com.geniusmind.malek.clevermind.Model.MySingleton;
import com.geniusmind.malek.clevermind.Model.UserInformation;
import com.geniusmind.malek.clevermind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SignUpActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextInputEditText textName, textUsername, textEmail, textPass, textPhone, textDate;
    private UserInformation user;
    private FirebaseAuth mAuth;
    private static final String TAG = "SIGN UP";
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initalizeComponent();
        setActionBar();
        handleSSLHandshake();

    }

    // initalize component . . . ;
    private void initalizeComponent() {
        mAuth = FirebaseAuth.getInstance();
        MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        linearLayout = findViewById(R.id.linear_progress_signup);
        mToolbar = findViewById(R.id.toolbar_signup);
        textName = findViewById(R.id.signup_fullname);
        textUsername = findViewById(R.id.signup_username);
        textEmail = findViewById(R.id.signup_email);
        textPass = findViewById(R.id.signup_password);
        textDate = findViewById(R.id.signup_birth);
        textPhone = findViewById(R.id.signup_phone);
    }

    // to set attribute and view in toolbar . . . ;
    private void setActionBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.sign_up_title));


    }

    // this method using from another activity to intent and pass data (if exist) to the current activity  . . . ;
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }

    // check event here . . . ;
    public void SignUp_btn(View view) {
        visibleProgress(true);
        putInfo();
        if (!checkViews()) {
            visibleProgress(false);
            return;
        } else {
            // check network connection . . .;
            if (CheckConnection.isConnectedToNetwork(this)) {
                // call createNewUser method . . . ;
                createNewUser(user.getEmail(), user.getPassword());

            } else {
                Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
                visibleProgress(false);
            }
        }

    }

    //
    public void putInfo() {
        String full = textName.getText().toString();
        String textuser = textUsername.getText().toString();
        String textemail = textEmail.getText().toString();
        String textpass = textPass.getText().toString();
        String textdate = textDate.getText().toString();
        String textphone = textPhone.getText().toString();
        // create instance for user information to pass data . . . .;
        user = new UserInformation(full, textuser, textemail, textphone, textpass, textdate);


    }

    // check if informations in views is correct . . . ;
    public boolean checkViews() {
        // check if views is empty . . . ;
        if (user.getFullname().isEmpty() || user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getDate().isEmpty() || user.getPassword().isEmpty() || user.getPhone().isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_views), Toast.LENGTH_LONG).show();
            return false;
        }
        // check if email not contain . and com . . . . ;
        if (!user.getEmail().toLowerCase().contains(".com") || !user.getEmail().contains("@")) {
            Toast.makeText(this, getString(R.string.email_error), Toast.LENGTH_LONG).show();
            return false;
        }
        // check if phone number is at least 10 character . . . ;
        if (user.getPhone().length() < 10) {
            Toast.makeText(this, getString(R.string.phone_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        // check if password at least 8 charachter . . . ;
        if (user.getPassword().length() < 8) {
            Toast.makeText(this, getString(R.string.pass_count_error), Toast.LENGTH_LONG).show();
            return false;
        }
        // check if password contain mixed of digit and letter . . . ;

        if (!passMixed()) {
            Toast.makeText(this, getString(R.string.pass_mixed_error), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // check if password is mixed letter and digit togther . . .
    public boolean passMixed() {
        String password = user.getPassword();
        int letter = 0;
        int digit = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
                letter++;
            } else if (Character.isDigit(password.charAt(i))) {
                digit++;
            }
        }
        if (letter == 0 || digit == 0) {
            return false;
        } else
            return true;
    }

    // for create new user (sign up) . . . ;
    private void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user.setId(String.valueOf(mAuth.getCurrentUser().getUid()));
                            // put data inside database . . . ;

                            putDataInsideDatabase(user.getId(), user.getFullname(), user.getUsername(), user.getEmail(), user.getPhone(), user.getPassword(), user.getDate());

                            visibleProgress(false);
                            // verify email address . . . ;
                            verifyEmailAddress();
                            mAuth.signOut();

                            // logout from account then return to the login activity . . . ;
                            finish();


                        } else {
                            visibleProgress(false);
                            try {
                                throw task.getException();

                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(SignUpActivity.this, R.string.error_invalid_email_password, Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(SignUpActivity.this, R.string.error_invalid_email_password, Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(SignUpActivity.this, R.string.erro_user_exist, Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthRecentLoginRequiredException e) {
                                Toast.makeText(SignUpActivity.this, R.string.check_connection, Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    // for visible progress bar  . . . ;
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

    // verify email address . . . ;
    public void verifyEmailAddress() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // show toast message when task is successfull or failure . . . ;
                            Toast.makeText(SignUpActivity.this, "Verification Email sent to : " + user.getEmail(), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // to put data in database . . . ;
    private void putDataInsideDatabase(final String id, final String fullName, final String userName
            , final String email, final String phone, final String password, final String Date) {

        StringRequest request = new StringRequest(Request.Method.POST, ContractUrl.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("user_exist")) {
                    Log.i("response exist","user_exist");
                } else if (response.contains("signup successfull")) {
                     Log.i("response successful","sign up successfull");
                }else if(response.contains("empty")){
                    Log.i("response empty","something was wrong!");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Log.e("request_error",error.getMessage());
            }
        }) {
            @Override
            // to put data inside database . . . ;
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("full_name", fullName);
                params.put("user_name", userName);
                params.put("email", email);
                params.put("phone", String.valueOf(phone));
                params.put("password", password);
                params.put("birth_date", Date);
                return params;
            }
        };
        // add request to request queue . . . . ;
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    // for ssl certificate . . .
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

}
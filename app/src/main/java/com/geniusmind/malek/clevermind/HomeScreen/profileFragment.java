package com.geniusmind.malek.clevermind.HomeScreen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geniusmind.malek.clevermind.ContractUrl;
import com.geniusmind.malek.clevermind.Model.MySingleton;
import com.geniusmind.malek.clevermind.Model.UserInformation;
import com.geniusmind.malek.clevermind.R;
import com.geniusmind.malek.clevermind.Splash_Login_Register.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profileFragment extends Fragment {
    private View view;
    private TextView text_quotes, text_btn;
    private ImageButton editButton;
    private TextInputEditText full_name, user_name, email, phone_number, date_birth;
    private UserInformation user_info;
    private FirebaseUser firebase_user;
    private LinearLayout linearLayout;
    private ConstraintLayout profileLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeViews();
        getDataFromDatabase();

        getWisdomFromDatabase();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!full_name.isEnabled()) {
                    editButton.setRotationX(180);
                    text_btn.setText(R.string.save);
                    enabledViews(true);
                } else {
                    //put information inside object . . . ;
                      putInformation();
                      // check views if empty . . .  ;
                      if(checkViews()){
                              updateDataFromDatabase();
                      }else {
                          enabledViews(true);
                          return;
                      }

                    // when click on save button . . . ;
                    editButton.setRotationX(360);
                    text_btn.setText(R.string.edit);
                    enabledViews(false);


                }
            }
        });
        return view;
    }

    // for findViewsById . . . ;
    private void initializeViews() {
        MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        SignUpActivity.handleSSLHandshake();
        text_btn = view.findViewById(R.id.text_save);
        linearLayout = view.findViewById(R.id.linear_profile);
        firebase_user = FirebaseAuth.getInstance().getCurrentUser();
        text_quotes = view.findViewById(R.id.text_quotes);
        full_name = view.findViewById(R.id.profile_fullname);
        user_name = view.findViewById(R.id.profile_username);
        email = view.findViewById(R.id.profile_email);
        phone_number = view.findViewById(R.id.profile_phone);
        date_birth = view.findViewById(R.id.profile_birth);
        editButton = view.findViewById(R.id.profile_edit_btn);
        profileLayout=view.findViewById(R.id.profile_layout);
    }

    // put data inside views from UserInformationLab (Singleton class) . . . ;
    private void getDataFromDatabase() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ContractUrl.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("no item")) {
                    Toast.makeText(getActivity(), "User is Not Exist", Toast.LENGTH_SHORT).show();
                    return;
                } else if (response.contains("put id")) {
                    Toast.makeText(getActivity(), "User is not Exist", Toast.LENGTH_SHORT).show();
                } else {
                    // fetch data from json response . . . ;
                    fetchDataFromJsonResponse(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(firebase_user.getUid()));
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void getWisdomFromDatabase() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ContractUrl.WISDOM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("no item")) {
                    return;
                } else {
                    if (response.length() < 50) {
                        text_quotes.setText(response.trim());
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", error.getMessage());

            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private UserInformation fetchDataFromJsonResponse(String response) {
        try {
            JSONObject user = new JSONObject(response);
            String fullName = user.optString("full_name");
            String userName = user.optString("username");
            String email = user.optString("email");
            String phone = user.optString("phone");
            String password = user.optString("password");
            String date = user.optString("date");
            user_info = new UserInformation(fullName, userName, email, phone, password, date);
            putDataInsideViews(user_info);
        } catch (JSONException e) {
            Log.e("json error", e.getMessage());
            return null;
        }

        return user_info;
    }

    private void putDataInsideViews(UserInformation user_info) {
        full_name.setText(user_info.getFullname());
        user_name.setText(user_info.getUsername());
        email.setText(user_info.getEmail());
        phone_number.setText(user_info.getPhone());
        date_birth.setText(user_info.getDate());
        linearLayout.setVisibility(View.GONE);
        profileLayout.setVisibility(View.VISIBLE);
    }

    // create new instance for profile fragment (used for another activity's or fragments) ..... ;
    public static Fragment newInstance() {
        return new profileFragment();
    }

    private void enabledViews(boolean Enabled) {
        full_name.setEnabled(Enabled);
        user_name.setEnabled(Enabled);
        email.setEnabled(Enabled);
        phone_number.setEnabled(Enabled);
        date_birth.setEnabled(Enabled);
    }

    // check if informations in views is correct . . . ;
    public boolean checkViews() {
        // check if views is empty . . . ;
        if (user_info.getFullname().isEmpty() || user_info.getUsername().isEmpty() || user_info.getDate().isEmpty() || user_info.getPhone().isEmpty()||user_info.getEmail().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.fill_views), Toast.LENGTH_LONG).show();
            return false;
        }
        // check if phone number is at least 10 character . . . ;
        if (user_info.getPhone().length() < 10) {
            Toast.makeText(getActivity(), getString(R.string.phone_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateDataFromDatabase(){
        StringRequest updateRequest = new StringRequest(Request.Method.POST, ContractUrl.Edit_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("unsuccessfull")){
                    Toast.makeText(getActivity(),"something was error !", Toast.LENGTH_SHORT).show();
                    Log.i("response error",response);

                }else {
                    Toast.makeText(getActivity(), "Update Successfull !", Toast.LENGTH_SHORT).show();
                getActivity().recreate();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             Log.e("Update Error",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("id",firebase_user.getUid());
                params.put("email",user_info.getEmail());
                params.put("full_name",user_info.getFullname());
                params.put("username",user_info.getUsername());
                params.put("phone",user_info.getPhone());
                params.put("date",user_info.getDate());
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(updateRequest);


    }

    private void putInformation(){
      //  private TextInputEditText full_name, user_name, email, phone_number, date_birth;
        String full=full_name.getText().toString();
        String user=user_name.getText().toString();
        String emails=email.getText().toString();
        String phone=phone_number.getText().toString();
        String date=date_birth.getText().toString();
        user_info=new UserInformation(full,user,emails,phone,null,date);
    }





}


package com.geniusmind.malek.clevermind.Splash_Login_Register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.geniusmind.malek.clevermind.CheckConnection;
import com.geniusmind.malek.clevermind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;

public class ForgetPasswordDialog extends DialogFragment {
TextInputEditText resetEmail;
FirebaseAuth mAuth;
Button resetBtn,cancelBtn;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_forget_password,null);
        mAuth=FirebaseAuth.getInstance();
        resetEmail=view.findViewById(R.id.forget_email);
        resetBtn=view.findViewById(R.id.reset_btn);
        cancelBtn=view.findViewById(R.id.cancel_btn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EmailAdress=resetEmail.getText().toString();

                if(!CheckConnection.isConnectedToNetwork(getActivity())){
                    Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(EmailAdress.isEmpty()){
                    Toast.makeText(getActivity(), R.string.fill_email_view, Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.sendPasswordResetEmail(EmailAdress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Email Sent", Toast.LENGTH_LONG).show();
                                dismiss();
                            }
                            else {
                                try {
                                    throw task.getException();

                                    // for invalid email . . . ;
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    Toast.makeText(getActivity(), "your email is incorrect !", Toast.LENGTH_SHORT).show();

                                    // for time out . . . ;
                                }catch (FirebaseAuthRecentLoginRequiredException e) {
                                    Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();

                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            }
                        });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



    return new AlertDialog.Builder(getActivity())
            .setView(view)
            .setCancelable(true)
            .create();


    }
}

package com.geniusmind.malek.clevermind.Splash_Login_Register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.geniusmind.malek.clevermind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailDialog extends DialogFragment {
    Button verify_btn,cancel_btn;
    FirebaseAuth mAuth;
    View view;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // to set view for dialog . . . ;
         view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_verify_dialog,null);
        mAuth=FirebaseAuth.getInstance();
        verify_btn=view.findViewById(R.id.verify_btn);
        cancel_btn=view.findViewById(R.id.cancel_verify);

        // when click on verify button . . . ;
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEmailAddress();
                mAuth.signOut();
                dismiss();
            }
        });

        // when click on cancel button . . . ;
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                dismiss();
            }
        });



        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(view)
                .create();

    }
    // verify email address . . . ;
    public void verifyEmailAddress(){
        final FirebaseUser user=mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(view.getContext(), "Verification Email sent to : "+user.getEmail(), Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(view.getContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

 package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

 import android.app.Activity;
 import android.app.AlertDialog;
 import android.app.Dialog;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatDialogFragment;

 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.AuthResult;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;

 public class LoginDialog extends AppCompatDialogFragment {
     private EditText et_email;
     private EditText et_password;
     private Button btn_register;
     private Button btn_login;
     private FirebaseAuth mAuth;
     private View view;
     private final static String INVALID = "-9999";

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         mAuth = FirebaseAuth.getInstance();

         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         LayoutInflater inflater = getActivity().getLayoutInflater();
         view = inflater.inflate(R.layout.login_dialog, null);
         builder.setView(view);
         setupUI();
         setupListeners();

         return builder.create();
    }
     @Override
     public void onStart() {
         super.onStart();
         FirebaseUser currUser = mAuth.getCurrentUser();
         if(currUser != null) {
             Log.i("LOGIN", "User currently logged in: " + mAuth.getCurrentUser().toString());
             exitLogin();
         }
     }
     public void setupUI() {
         et_email = view.findViewById(R.id.et_email);
         et_password = view.findViewById(R.id.et_password);
         btn_register = view.findViewById(R.id.btn_register);
         btn_login = view.findViewById(R.id.btn_login);
     }
     public void setupListeners() {
         btn_register.setOnClickListener(v-> {
             RegisterDialog registerDialog = new RegisterDialog();
             registerDialog.show(getParentFragmentManager(),"register");
             exitLogin();
         });
         btn_login.setOnClickListener(v-> {
             String email = et_email.getText().toString();
             String password = et_password.getText().toString();

             if(email.equals(null) || email.trim().isEmpty() || password.trim().isEmpty() || password.equals(null)) {
                 email = "INVALID";
                 password = "INVALID";
             }

             mAuth.signInWithEmailAndPassword(email, password)
                     .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                         AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 // Sign in success, update UI with the signed-in user's information
                                 Log.i("LOGIN", "signInWithEmail:success");
                                 FirebaseUser user = mAuth.getCurrentUser();
                                 Log.i("LOGIN", "USER: " + user.toString());

                                 alertBuilder.setTitle("Success")
                                     .setMessage("You have successfully logged in")
                                     .setPositiveButton("Ok", (dialog, which) -> {
                                         exitLogin();
                                 });
                                 AlertDialog alert = alertBuilder.create();
                                 alert.show();
                             } else {
                                 // If sign in fails, display a message to the user.
                                 Log.i("LOGIN", "signInWithEmail:failure", task.getException());

                                 alertBuilder.setTitle("Invalid")
                                     .setMessage("Email or password is invalid")
                                     .setPositiveButton("Ok", (dialog, which) -> {
                                });
                                 AlertDialog alert = alertBuilder.create();
                                 alert.show();
                             }
                        }
            });
         });
     }
     public void exitLogin() {
         this.dismiss();
     }
 }
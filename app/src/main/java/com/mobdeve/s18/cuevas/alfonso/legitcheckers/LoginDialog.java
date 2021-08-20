 package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

 import android.app.AlertDialog;
 import android.app.Dialog;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;

 import androidx.appcompat.app.AppCompatDialogFragment;

 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityMainBinding;


 public class LoginDialog extends AppCompatDialogFragment {
     private EditText et_email;
     private EditText et_password;
     private Button btn_register;
     private FirebaseAuth mAuth;


     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         LayoutInflater inflater = getActivity().getLayoutInflater();
         View view = inflater.inflate(R.layout.login_dialog, null);
         builder.setView(view);

//         mAuth = FirebaseAuth.getInstance();

         et_email = view.findViewById(R.id.et_email);
         et_password = view.findViewById(R.id.et_password);
         btn_register = view.findViewById(R.id.btn_register);

         btn_register.setOnClickListener(v-> {
             openRegister();
         });
         return builder.create();
    }

     @Override
     public void onStart() {
         super.onStart();

//         FirebaseUser currUser = mAuth.getCurrentUser();
//         if(currUser != null) {
//
//         }
     }

     public void openRegister() {
         RegisterDialog registerDialog = new RegisterDialog();
         registerDialog.show(getChildFragmentManager(),"register");
    }
 }


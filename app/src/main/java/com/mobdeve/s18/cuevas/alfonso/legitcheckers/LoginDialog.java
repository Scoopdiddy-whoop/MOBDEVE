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

 import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityMainBinding;


 public class LoginDialog extends AppCompatDialogFragment {
     private EditText et_username;
     private EditText et_password;
     private Button btn_register;


     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


         LayoutInflater inflater = getActivity().getLayoutInflater();
         View view = inflater.inflate(R.layout.login_dialog, null);
         builder.setView(view);


         et_username = view.findViewById(R.id.et_username);
         et_password = view.findViewById(R.id.et_password);
         btn_register = view.findViewById(R.id.btn_register);

         btn_register.setOnClickListener(v-> {
             openRegister();
         });
         return builder.create();
    }

    public void openRegister() {
         RegisterDialog registerDialog = new RegisterDialog();
         registerDialog.show(getChildFragmentManager(), "register dialog");
    }
 }


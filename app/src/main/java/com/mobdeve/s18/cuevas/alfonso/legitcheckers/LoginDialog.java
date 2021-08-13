 package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

 import android.app.AlertDialog;
 import android.app.Dialog;
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.widget.EditText;

 import androidx.appcompat.app.AppCompatDialogFragment;

 public class LoginDialog extends AppCompatDialogFragment {
     private EditText et_username;
     private EditText et_password;

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

         LayoutInflater inflater = getActivity().getLayoutInflater();
         View view = inflater.inflate(R.layout.login_dialog, null);

         builder.setView(view);
//                 .setTitle("Login")
//                 .setNegativeButton("Cancel", (dialog, which) -> {
//
//                 })
//                 .setPositiveButton("ok", (dialog, which) -> {
//
//                 });

         et_username = view.findViewById(R.id.et_username);
         et_password = view.findViewById(R.id.et_password);
         return builder.create();
    }
 }


package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;

public class SignoutDialog extends AppCompatDialogFragment {
    private View view;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.signout_dialog, null);
        builder.setView(view);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TextView tv_username = view.findViewById(R.id.tv_username);
        Button btn_signout = view.findViewById(R.id.btn_signout);

        Database db = new Database();
        db.getUsername(mAuth.getCurrentUser().getUid(), new Database.FirebaseStringCallback() {
            @Override
            public void onCallBack(String string) {
                tv_username.setText(string);
            }
        });

        btn_signout.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            alertBuilder.setTitle("Success")
                    .setMessage("You have successfully logged out")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        LoginDialog loginDialog = new LoginDialog();
                        loginDialog.show(getParentFragmentManager(), "login dialog");
                        this.dismiss();
                    });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        });
        return builder.create();
    }
}

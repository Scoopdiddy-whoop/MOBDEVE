package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterDialog extends AppCompatDialogFragment {
    private ImageButton btn_back;
    private EditText et_email;
    private EditText et_username;
    private EditText et_password;
    private Button btn_register;
    private FirebaseAuth mAuth;
    private View view;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.register_dialog, null);
        builder.setView(view);

        mAuth = FirebaseAuth.getInstance();
        setupUI();
        setupListeners();

        return builder.create();
    }
    public void setupUI() {
        btn_register = view.findViewById(R.id.btn_register);
        btn_back = view.findViewById(R.id.btn_back);
        et_email = view.findViewById(R.id.et_email);
        et_username = view.findViewById(R.id.et_username);
        et_password = view.findViewById(R.id.et_password);
    }

    public void setupListeners() {
        btn_back.setOnClickListener(v->{
            LoginDialog loginDialog = new LoginDialog();
            loginDialog.show(getParentFragmentManager(), "login dialog");
            this.dismiss();
        });
        btn_register.setOnClickListener(v->{
            String email = et_email.getText().toString();
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();

            if(!validateDataEntered()) {
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) getContext(), task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("REGISTER", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    //connect user to database data
                    User dbUser = new User(user.getUid(), username);
                    Log.i("REGISTER", dbUser.getFriendlist().toString());
                    Database db = new Database();
                    db.addUser(dbUser);

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Success")
                            .setMessage("New Account successfully created")
                            .setPositiveButton("Ok", (dialog, which) -> {
                                this.dismiss();
                            });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    //make it go back to main Activity
                } else {
                    // If sign in fails, display a message to the user.
                    Log.i("REGISTER", "createUserWithEmail:failure", task.getException());

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Error")
                            .setMessage("Failed to create new account")
                            .setPositiveButton("Ok", (dialog, which) -> {
                                ;
                            });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
            });
        });
    }
    public boolean validateDataEntered() {
        validEmail();
        validUsername();
        validPassword();
        
        return validEmail() && validUsername() && validPassword();
    }
    public boolean validPassword(){
        boolean valid;
        String val = et_password.getText().toString();

        if(val.trim().isEmpty()) {
            et_password.setError("Password cannot be empty");
            valid = false;
        }
        else if(val.length()<8){
            et_password.setError("Password must contain at least 8 characters");
            valid = false;
        }
        else {
            String regex = "[\\s]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(val);
            if(matcher.find()) {
                et_password.setError("Password cannot contain spaces");
                valid = false;
            }
            else{
                valid = true;
            }
        }
        if(!valid) {
            Log.i("REGISTER", "INVALID PASSWORD");
        }
        return valid;
    }
    public boolean validUsername() {
        boolean valid;
        String val = et_username.getText().toString();

        if(val.trim().isEmpty()) {
            et_username.setError("Username cannot be empty");
            valid = false;
        }
        else if(val.length()<=8) {
            et_username.setError("Username must contain at least 8 characters");
            valid = false;
        }
        else {
            String regex = "[\\s]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(val);
            if (matcher.find()) {
                et_username.setError("Username cannot contain spaces");
                valid = false;
            } else {
                valid = true;
            }
        }

        if(!valid) {
            Log.i("REGISTER", "INVALID USERNAME");
        }
        return valid;
    }
    public boolean validEmail() {
        String val = et_email.getText().toString();
        boolean valid;

        if(val.trim().isEmpty()) {
            et_email.setError("Email cannot be empty");
            valid = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            et_email.setError("Invalid email format");
            valid = false;
            Log.i("REGISTER", "INVALID EMAIL");
        }
        else{
            valid = true;
        }

        return valid;
    }

}

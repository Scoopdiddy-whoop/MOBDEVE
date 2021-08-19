package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatDialogFragment;

public class RegisterDialog extends AppCompatDialogFragment {
    private ImageButton btn_back;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.register_dialog, null);
        builder.setView(view);

        btn_back = view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v->{
            this.dismiss();
        });

        return builder.create();
    }
}

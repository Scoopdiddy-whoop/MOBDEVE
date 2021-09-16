package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDialogFragment;


public class WinnerDialog extends AppCompatDialogFragment {

    private TextViewOutline title;
    private Button home;
    private Context wContext;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.winner_dialog, null);
        builder.setView(view);

        wContext = view.getContext();

        title = view.findViewById(R.id.winner);
        String winner = getArguments().getString("winner");
        title.setText(winner.toUpperCase() + " HAS WON THE GAME!");

        home = view.findViewById(R.id.btn_goHome);
        home.setOnClickListener(v -> startActivity(new Intent(wContext, MainActivity.class)));

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(wContext, MainActivity.class);
        startActivity(intent);
    }
}

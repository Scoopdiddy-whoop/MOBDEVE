package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDialogFragment;

public class MenuDialog extends AppCompatDialogFragment {

    private Button btn_resume;
    private Button btn_settings;
    private Button btn_quit;
    private Context mContext;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.menu_dialog, null);
        builder.setView(view);

        mContext = view.getContext();

        btn_resume = view.findViewById(R.id.btn_resume);
        btn_settings = view.findViewById(R.id.btn_settings);
        btn_quit = view.findViewById(R.id.btn_quit);

        btn_resume.setOnClickListener(v -> this.dismiss());
        btn_quit.setOnClickListener(v -> startActivity(new Intent(mContext, MainActivity.class)));
        btn_settings.setOnClickListener(v-> {openSettings();});

        return builder.create();
    }

    public void openSettings() {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getChildFragmentManager(),"settings");
    }
}

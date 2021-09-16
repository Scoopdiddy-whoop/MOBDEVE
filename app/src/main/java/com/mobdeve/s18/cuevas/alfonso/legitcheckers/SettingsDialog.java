package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.SettingsBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.util.StoragePreferences;

public class SettingsDialog extends AppCompatDialogFragment {

    SettingsBinding binding;
    private Context sContext;
    private CheckBox checkBox;
    private Switch nightSwitch;
    private ImageView home;
    private boolean playMusic;
    private boolean nightMode;
    private StoragePreferences storagePreferences;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.settings, null);
        builder.setView(view);
        sContext = view.getContext();
        storagePreferences = new StoragePreferences(sContext);


        checkBox = view.findViewById(R.id.cb_music);
        nightSwitch = view.findViewById(R.id.sw_night);
        home = view.findViewById(R.id.homebg);

        playMusic = storagePreferences.getMusicPreferences("Play");
        nightMode = storagePreferences.getThemePreferences("Theme");
        nightSwitch.setChecked(nightMode);
        checkBox.setChecked(playMusic);
        Log.i("TAG", "onCreateDialog:" + playMusic);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    Intent intent = new Intent(sContext, BackgroundSoundService.class);
                    sContext.startService(intent);
                    playMusic=true;
                }
                else{
                    Intent intent = new Intent(sContext, BackgroundSoundService.class);
                    sContext.stopService(intent);
                    playMusic=false;
                }
            }
        });
        nightSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->  {
            nightMode = isChecked;
        });
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Boolean themechanged = storagePreferences.getThemePreferences("Theme") == nightMode;
        storagePreferences.saveMusicPreferences("Play", playMusic);
        storagePreferences.saveThemePreferences("Theme", nightMode);
        if((getActivity().getClass() == MainActivity.class) && !themechanged){
        Intent intent = new Intent(sContext, getActivity().getClass());
        startActivity(intent);
        }
    }
}

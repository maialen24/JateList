package com.example.jatelist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

public class settingsDialog  extends DialogFragment {
    settingsDialog.Listener miListener;

    public interface Listener {
        void alpulsarSave(Boolean mode, int language);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener = (settingsDialog.Listener) getActivity();

        Log.i("info","SETTINGS");




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.settings));
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View elaspecto= inflater.inflate(R.layout.settings_dialog,null);
        builder.setView(elaspecto);


        builder.setPositiveButton(builder.getContext().getString(R.string.saveButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Switch mode=(Switch) elaspecto.findViewById(R.id.modo);
                RadioGroup language=elaspecto.findViewById(R.id.radioGroup);


                miListener.alpulsarSave(mode.isChecked(),language.getCheckedRadioButtonId());
            }
        });


        return builder.create();


    }
}

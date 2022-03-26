package com.example.jatelist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class settingsDialog  extends DialogFragment {
    settingsDialog.Listener miListener;
    String language;
    Boolean night;



    public interface Listener {
        void alpulsarSave(Boolean mode, String language);


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

        Bundle bundle = getArguments();
        String language = bundle.getString("language","es");
        Boolean mode= bundle.getBoolean("mode",false);

        Switch modeS=elaspecto.findViewById(R.id.modo);
        modeS.setChecked(mode);

        RadioGroup rg=elaspecto.findViewById(R.id.radioGroup);

        switch (language) {
            case "eu":
                rg.check(R.id.eu);
            case "en":
                rg.check(R.id.en);
            default:
                rg.check(R.id.es);
        }


        builder.setView(elaspecto);



        builder.setPositiveButton(builder.getContext().getString(R.string.saveButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Switch mode=(Switch) elaspecto.findViewById(R.id.modo);
                //RadioGroup language=elaspecto.findViewById(R.id.radioGroup);
                RadioButton euskera=elaspecto.findViewById(R.id.eu);
                RadioButton ingles=elaspecto.findViewById(R.id.en);
                String language="es";
                if (euskera.isChecked()){
                    language="eu";
                }else if(ingles.isChecked()){
                    language="en";
                }


                miListener.alpulsarSave(mode.isChecked(),language);
            }
        });


        return builder.create();


    }
}

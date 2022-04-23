package com.example.jatelist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class settingsDialog  extends DialogFragment {

    /* This class create settings dialog, toolbar menu option, set user shared preferences (language, theme) */
    settingsDialog.Listener miListener;
    String language;
    Boolean night;


    // login activity implements positive button on click method
    public interface Listener {
        void alpulsarSave(Boolean mode, boolean noti,boolean notir);


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

        //get actual language and theme
        Bundle bundle = getArguments();

        Boolean mode= bundle.getBoolean("mode",false);
        Boolean noti= bundle.getBoolean("noti",false);
        Boolean notir= bundle.getBoolean("notir",false);

        Switch modeS=elaspecto.findViewById(R.id.modo);
        modeS.setChecked(mode);
        CheckBox checkBox=elaspecto.findViewById(R.id.notiRestaurant);
        checkBox.setChecked(noti);

        CheckBox checkBox2=elaspecto.findViewById(R.id.notiRecordatorio);
        checkBox2.setChecked(notir);




        builder.setView(elaspecto);


        // postive button on click method, save new preferences
        builder.setPositiveButton(builder.getContext().getString(R.string.saveButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Switch mode=(Switch) elaspecto.findViewById(R.id.modo);

                CheckBox checkBox=(CheckBox) elaspecto.findViewById(R.id.notiRestaurant);
                CheckBox checkBoxr=(CheckBox) elaspecto.findViewById(R.id.notiRecordatorio);

                miListener.alpulsarSave(mode.isChecked(),checkBox.isChecked(),checkBoxr.isChecked());
            }
        });


        return builder.create();


    }
}

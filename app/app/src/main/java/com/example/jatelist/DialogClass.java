package com.example.jatelist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogClass extends DialogFragment {

    /* This class create a sign up dialog to create new users and insert them into the db */

    Listener miListener;

    //create interface that login class is going to implement
    public interface Listener {
        void alpulsarSI(String user, String password);

        void alpulsarNO();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener = (Listener) getActivity();

        Log.i("info","CREATE NEW USER");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("SIGN UP");
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View elaspecto= inflater.inflate(R.layout.dialog_sigin,null);
        builder.setView(elaspecto);

        // create button, create new user, insert in db
        builder.setPositiveButton(builder.getContext().getString(R.string.crear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText user= (EditText) elaspecto.findViewById(R.id.username);
                EditText password = (EditText) elaspecto.findViewById(R.id.password);
                miListener.alpulsarSI(user.getText().toString(),password.getText().toString());
            }
        });
        // cancel button close dialog, dont create user
        builder.setNegativeButton(builder.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarNO();
            }
        });

        return builder.create();


    }


}

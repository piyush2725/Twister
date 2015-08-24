package com.example.piyush.twister.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.piyush.twister.R;

/**
 * Created by piyush on 01-05-2015.
 */
public class AlertDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState){
        Context context =getActivity();
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.dialogTitle))
        .setMessage(context.getString(R.string.dialogmessage))
        .setPositiveButton(context.getString(R.string.ok_button), null);
        return builder.create();
    }
}

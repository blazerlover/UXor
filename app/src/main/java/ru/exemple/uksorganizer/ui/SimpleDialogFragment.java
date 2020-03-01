package ru.exemple.uksorganizer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ru.exemple.uksorganizer.R;

public class SimpleDialogFragment extends DialogFragment {

    //TODO: передавать строки параметрами
    public interface SimpleDialogListener {
        public void onDeleteDialogPositiveClick(DialogFragment dialog);
        public void onDeleteDialogNegativeClick(DialogFragment dialog);
    }
    SimpleDialogListener mListener;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (SimpleDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SimpleDialogListener");
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_event_q)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDeleteDialogPositiveClick(SimpleDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}

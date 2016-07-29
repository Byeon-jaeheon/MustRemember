package com.mujogun.hyk.lockscreenchanger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.util.Log;

/**
 * Created by leehk on 2016-07-29.
 */
public class select extends DialogFragment {

    Bitmap b;
    int id;

    public interface TestDialogFragmentListener {
        public void onTestDialogClick(android.support.v4.app.DialogFragment dialog, Bitmap b);
        public void pickbackground(android.support.v4.app.DialogFragment dialog, int id);
    }

    TestDialogFragmentListener testDialogFragmentListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            testDialogFragmentListener = (TestDialogFragmentListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TestDialogFragmentListener");
        }
    }

    public void set(Bitmap background) {
        this.b = background;

    }
    public void set(int id) {
        this.id = id;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("이 배경으로 선택하시겠습니까?")
                .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!

                        someAction();
                        /*
                        Intent intent = new Intent(getActivity(), ConfigActivity.class);

                        intent.putExtra("img", b);

                        getActivity().startActivityForResult(intent, 0);
                        */
                    }
                })
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void someAction() {
        testDialogFragmentListener.pickbackground(select.this, id);
    }

}
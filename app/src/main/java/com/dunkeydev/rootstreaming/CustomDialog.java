package com.dunkeydev.rootstreaming;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class CustomDialog {

    public static AlertDialog.Builder builder;
    public static AlertDialog alertDialog;
    private Context context;
    public CustomDialog(Context context)
    {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = ((Activity) context).findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.progressbar_circle, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void ShowDialog(){
        try {
            alertDialog.show();
        }
        catch (WindowManager.BadTokenException ex) {
            ex.printStackTrace();
        }
    }

    public void DismissDialog()
    {
        if (alertDialog.isShowing())
        {
            alertDialog.dismiss();
        }
    }

}

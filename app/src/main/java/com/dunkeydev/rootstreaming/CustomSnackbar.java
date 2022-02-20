package com.dunkeydev.rootstreaming;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class CustomSnackbar {

    public static Snackbar snackbar;
    private Context context;
    private View view;
    public TextView textDesc;

    public CustomSnackbar(Context context, View view)
    {
        this.context = context;
        this.view = view;
        // create an instance of the snackbar
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);

        // inflate the custom_snackbar_view created previously
        View customSnackView = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_snackbar, null);

        // set the background of the default snackbar as transparent
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        // now change the layout of the snackbar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        // set padding of the all corners as 0
        snackbarLayout.setPadding(0, 0, 0, 0);

        // register the button from the custom_snackbar_view layout file
        textDesc = customSnackView.findViewById(R.id.snackbar_text);

        // add the custom snack bar layout to snackbar layout
        snackbarLayout.addView(customSnackView, 0);
    }

    public void show(String text)
    {
        textDesc.setText(text);
        snackbar.show();
    }

    public void dismiss()
    {
        if (snackbar.isShown())
        {
            snackbar.dismiss();
        }
    }
}

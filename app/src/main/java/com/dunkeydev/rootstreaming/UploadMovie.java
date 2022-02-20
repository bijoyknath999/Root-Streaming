package com.dunkeydev.rootstreaming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UploadMovie extends AppCompatActivity{

    Button button;
    EditText editText, editText2, editText3, editText4;
    DatabaseReference databaseReference;
    Member member;
    private CustomDialog customDialog;
    private CustomSnackbar customSnackbar;
    private LinearLayout uploadLayout;
    private String moviename,id;
    private boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadmovie);

        member = new Member();
        databaseReference = FirebaseDatabase.getInstance().getReference("video");

        moviename = getIntent().getStringExtra("name");
        check = getIntent().getBooleanExtra("edit",false);
        id = getIntent().getStringExtra("id");

        customDialog = new CustomDialog(UploadMovie.this);

        button = findViewById(R.id.button_upload_main);
        editText = findViewById(R.id.et_video_name);
        editText2 = findViewById(R.id.et_img_link);
        editText3 = findViewById(R.id.et_video_link);
        editText4 = findViewById(R.id.et_api_key);
        uploadLayout = findViewById(R.id.upload_layout);
        customSnackbar = new CustomSnackbar(UploadMovie.this,uploadLayout);

        if (check)
        {
            databaseReference.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String Videourl = snapshot.child("videourl").getValue().toString();
                    String imgurl = snapshot.child("imgurl").getValue().toString();
                    editText.setText(moviename);
                    editText3.setText(Videourl);
                    String api = Videourl.substring(Videourl.length() - 40);
                    editText4.setText(api);
                    editText2.setText(""+imgurl);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.ShowDialog();
                if (!check)
                    UploadVideo();
                else
                    editvideo();
            }
        });

    }

    private void editvideo() {
        String videoName = editText.getText().toString();
        String  search = editText.getText().toString().toLowerCase();
        String imgurl = editText2.getText().toString();
        String link = editText3.getText().toString();

        String api = editText4.getText().toString();


        String finalink, sharing;


        if (videoName.isEmpty()) {
            customDialog.DismissDialog();
            editText.setError("Video Name is empty!!!");
            customSnackbar.show("Video Name is empty!!!");
        }
        else if (imgurl.isEmpty()) {
            customDialog.DismissDialog();
            editText2.setError("Image url is empty!!!");
            customSnackbar.show("Image url is empty!!!");
        }
        else if (link.isEmpty()) {
            customDialog.DismissDialog();
            editText3.setError("Link is empty!!!");
            customSnackbar.show("Link is empty!!!");
        }
        else if (api.isEmpty()) {
            customDialog.DismissDialog();
            editText4.setError("API is empty!!!");
            customSnackbar.show("API is empty!!!");
        }
        else
        {
            sharing = link.substring(link.length() - 7);

            if (sharing.equals("sharing"))
            {
                String linkrep1 = link.replace("https://drive.google.com/file/d/", "");
                String linkrep2 = linkrep1.replace("/view?usp=sharing", "");
                finalink = "https://www.googleapis.com/drive/v3/files/"+linkrep2+"?alt=media&key="+api;
            }
            else
            {
                String linkrep1 = link.replace("https://drive.google.com/file/d/", "");
                String linkrep2 = linkrep1.replace("/view?usp=drivesdk", "");
                finalink = "https://www.googleapis.com/drive/v3/files/"+linkrep2+"?alt=media&key="+api;
            }

            Map<String, Object> postValues = new HashMap<String,Object>();
            postValues.put("name", videoName);
            postValues.put("imgurl", imgurl);
            postValues.put("search", search);
            postValues.put("videourl", finalink);

            databaseReference.child(id).updateChildren(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    customDialog.DismissDialog();
                    customSnackbar.show("Successfully Edited!!");
                    finish();
                }
            });

        }

    }

    private void UploadVideo(){
        String videoName = editText.getText().toString();
        String  search = editText.getText().toString().toLowerCase();
        String imgurl = editText2.getText().toString();
        String link = editText3.getText().toString();

        String api = editText4.getText().toString();


        String finalink, sharing;


        if (videoName.isEmpty()) {
            customDialog.DismissDialog();
            editText.setError("Video Name is empty!!!");
            customSnackbar.show("Video Name is empty!!!");
        }
        else if (imgurl.isEmpty()) {
            customDialog.DismissDialog();
            editText2.setError("Image url is empty!!!");
            customSnackbar.show("Image url is empty!!!");
        }
        else if (link.isEmpty()) {
            customDialog.DismissDialog();
            editText3.setError("Link is empty!!!");
            customSnackbar.show("Link is empty!!!");
        }
        else if (api.isEmpty()) {
            customDialog.DismissDialog();
            editText4.setError("API is empty!!!");
            customSnackbar.show("API is empty!!!");
        }
        else
        {
            sharing = link.substring(link.length() - 7);

            if (sharing.equals("sharing"))
            {
                String linkrep1 = link.replace("https://drive.google.com/file/d/", "");
                String linkrep2 = linkrep1.replace("/view?usp=sharing", "");
                finalink = "https://www.googleapis.com/drive/v3/files/"+linkrep2+"?alt=media&key="+api;
            }
            else
            {
                String linkrep1 = link.replace("https://drive.google.com/file/d/", "");
                String linkrep2 = linkrep1.replace("/view?usp=drivesdk", "");
                finalink = "https://www.googleapis.com/drive/v3/files/"+linkrep2+"?alt=media&key="+api;
            }

            member.setImgurl(imgurl);
            member.setName(videoName);
            member.setVideourl(finalink);
            member.setSearch(search);
            String i = databaseReference.push().getKey();
            databaseReference.child(i).setValue(member).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    customDialog.DismissDialog();
                    customSnackbar.show("Successfully Uploaded!!");
                    finish();
                }
            });

        }

    }
}

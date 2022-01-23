package com.dunkeydev.rootstreaming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadMovie extends AppCompatActivity{

    Button button;
    EditText editText, editText2, editText3, editText4;
    DatabaseReference databaseReference;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadmovie);

        member = new Member();
        databaseReference = FirebaseDatabase.getInstance().getReference("video");



        button = findViewById(R.id.button_upload_main);
        editText = findViewById(R.id.et_video_name);
        editText2 = findViewById(R.id.et_img_link);
        editText3 = findViewById(R.id.et_video_link);
        editText4 = findViewById(R.id.et_api_key);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadVideo();
            }
        });

    }

    private void UploadVideo(){
        String videoName = editText.getText().toString();
        String  search = editText.getText().toString().toLowerCase();
        String imgurl = editText2.getText().toString();
        String link = editText3.getText().toString();

        String api = editText4.getText().toString();

        String linkrep1 = link.replace("https://drive.google.com/file/d/", "");
        String linkrep2 = linkrep1.replace("/view?usp=sharing", "");
        String finalink = "https://www.googleapis.com/drive/v3/files/"+linkrep2+"?alt=media&key="+api;

        member.setImgurl(imgurl);
        member.setName(videoName);
        member.setVideourl(finalink);
        member.setSearch(search);
        String i = databaseReference.push().getKey();
        databaseReference.child(i).setValue(member);

    }
}

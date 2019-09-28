package com.example.creddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Post_Video_Activity extends AppCompatActivity {

    SharedPref sharedPref;
    CardView open_camera_vid, open_gallary_vid;
    ImageView gallery_video;
    LinearLayout linear_cam_gallery_video;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_post__video_);

        open_camera_vid = findViewById(R.id.post_video_open_camera);
        open_gallary_vid = findViewById(R.id.post_video_open_gallery);

        gallery_video = findViewById(R.id.gallery_video);
        linear_cam_gallery_video = findViewById(R.id.linear_cam_gallery_video);

        open_camera_vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "This will open the camera", Toast.LENGTH_SHORT).show();
            }
        });

        open_gallary_vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            Uri filepath = data.getData();
//            profile_image.setImageURI(filepath);
            try{
                linear_cam_gallery_video.setVisibility(View.GONE);
                gallery_video.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filepath );
                gallery_video.setImageBitmap(bitmap);
//                profile_image.setImageURI(filepath);

//                Zoro image link : https://tinyurl.com/y3tdn867

                Picasso.get().load(filepath).into(gallery_video);
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

}

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

public class Post_Image_Activity extends AppCompatActivity {

    SharedPref sharedPref;
    CardView open_camera, open_gallary;
    ImageView gallery_image;
    LinearLayout linear_cam_gallery;
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

        setContentView(R.layout.activity_post__image_);

        open_camera = findViewById(R.id.post_image_open_camera);
        open_gallary = findViewById(R.id.post_image_open_gallery);

        gallery_image = findViewById(R.id.gallery_image);
        linear_cam_gallery = findViewById(R.id.linear_cam_gallery);

        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "This will open the camera", Toast.LENGTH_SHORT).show();
            }
        });

        open_gallary.setOnClickListener(new View.OnClickListener() {
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
                linear_cam_gallery.setVisibility(View.GONE);
                gallery_image.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filepath );
                gallery_image.setImageBitmap(bitmap);
//                profile_image.setImageURI(filepath);

//                Zoro image link : https://tinyurl.com/y3tdn867

                Picasso.get().load(filepath).into(gallery_image);
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}

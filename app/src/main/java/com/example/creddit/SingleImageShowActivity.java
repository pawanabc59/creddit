package com.example.creddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.ortiz.touchview.TouchImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SingleImageShowActivity extends AppCompatActivity {

    SharedPref sharedPref;
//    ImageView single_card_profile_image;
    PhotoView single_card_image;
    FloatingActionButton imageDownload;
    String pushKey;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
//    TextView single_card_title, single_card_posted_by, singleCardDescription;
//    ImageView single_post_upvote, single_post_downvote, single_post_comment, single_post_share;

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

        setContentView(R.layout.activity_single_image_show);

        //        This code is for transparent status bar in android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");

//        single_card_profile_image = findViewById(R.id.single_card_profile_image);
        single_card_image = findViewById(R.id.single_card_image);
        imageDownload = findViewById(R.id.imageDownload);
//        single_card_title = findViewById(R.id.single_card_title);
//        single_card_posted_by = findViewById(R.id.single_posted_by);
//        single_post_upvote = findViewById(R.id.single_post_upvote);
//        single_post_downvote = findViewById(R.id.single_post_downvote);
//        single_post_comment = findViewById(R.id.single_post_comment);
//        single_post_share = findViewById(R.id.single_post_share);
//        singleCardDescription = findViewById(R.id.singleCardDescription);

        Intent intent = getIntent();
//        String card_title = intent.getExtras().getString("card_title");
//        String card_description = intent.getExtras().getString("card_description");
//        String posted_by = intent.getExtras().getString("posted_by");
        final String cardImage = intent.getExtras().getString("cardImage");
//        String cardProfileImage = intent.getExtras().getString("cardProfileImage");

//        single_card_title.setText(card_title);
//        single_card_posted_by.setText(posted_by);
//        singleCardDescription.setText(card_description);

//        Picasso.get().load(cardProfileImage).into(single_card_profile_image);
        Picasso.get().load(cardImage).into(single_card_image);

        imageDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(cardImage).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        Uri uri = saveWallpaperAndGetUri(bitmap);

                        if (uri != null) {
                            intent1.setDataAndType(uri, "image/*");
                            startActivity(Intent.createChooser(intent1, "mWallpaper"));
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        });

//        single_post_upvote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "post is upvoted", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        single_post_downvote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "post is downvoted", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        single_post_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "comment is clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        single_post_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "share is clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private Uri saveWallpaperAndGetUri(Bitmap bitmap) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);

                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
            return null;
        }
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/cReddit");
        folder.mkdir();

        pushKey = mRef.push().getKey();

        File file = new File(folder, pushKey + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), "Image Downloaded", Toast.LENGTH_SHORT).show();

            return FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}

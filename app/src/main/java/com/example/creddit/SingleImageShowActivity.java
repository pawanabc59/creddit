package com.example.creddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MotionEventCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.ortiz.touchview.TouchImageView;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnViewDragListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.toxa2033.ScaleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//import ua.zabelnikov.swipelayout.layout.frame.SwipeableLayout;
//import ua.zabelnikov.swipelayout.layout.listener.OnLayoutSwipedListener;

public class SingleImageShowActivity extends AppCompatActivity {

    SharedPref sharedPref;
//    ImageView single_card_profile_image;
    PhotoView single_card_image;
    FloatingActionButton imageDownload;
    String pushKey;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    ImageView backgroundWallpaper;
//    ScaleImageView single_card_image;
//    SwipeableLayout swipeableLayout;
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
        backgroundWallpaper = findViewById(R.id.backgroundWallpaper);
        imageDownload = findViewById(R.id.imageDownload);
//        swipeableLayout = findViewById(R.id.swipeableLayout);
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

//        swipeableLayout.setOnSwipedListener(new OnLayoutSwipedListener() {
//            @Override
//            public void onLayoutSwiped() {
//                finish();
//            }
//        });
//        String cardProfileImage = intent.getExtras().getString("cardProfileImage");

//        single_card_title.setText(card_title);
//        single_card_posted_by.setText(posted_by);
//        singleCardDescription.setText(card_description);

//        Picasso.get().load(cardProfileImage).into(single_card_profile_image);
        Picasso.get().load(cardImage).resize(5,5).into(backgroundWallpaper);
        Picasso.get().load(cardImage).into(single_card_image);

//        single_card_image.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int action = MotionEventCompat.getActionMasked(motionEvent);
//                switch(action) {
//                    case (MotionEvent.ACTION_DOWN) :
//                        finish();
//                        return true;
//                    case (MotionEvent.ACTION_UP) :
//                        finish();
//                        return true;
//                    default :
//                        return false;
//                }
//            }
//        });

        single_card_image.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
            @Override
            public void onOutsidePhotoTap(ImageView imageView) {
                finish();
            }
        });

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

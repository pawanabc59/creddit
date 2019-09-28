package com.example.creddit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    ImageView btnLink, btnImage, btnVideo, btnText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        btnLink = view.findViewById(R.id.upload_link);
        btnImage = view.findViewById(R.id.upload_image);
        btnVideo = view.findViewById(R.id.upload_video);
        btnText = view.findViewById(R.id.upload_text);

        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Post_Link_Activity.class);
                startActivity(intent);
            }
        });

        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Post_Text_Activity.class);
                startActivity(intent);
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Post_Image_Activity.class);
                startActivity(intent);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Post_Video_Activity.class);
                startActivity(intent);
            }
        });


        return view;
    }

}

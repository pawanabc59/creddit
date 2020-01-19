package com.example.creddit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.BuildConfig;
import com.example.creddit.Model.CardModal;
import com.example.creddit.R;
import com.example.creddit.SingleImageShowActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.widget.PopupMenu.*;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    private Context mContext;
    private List<CardModal> mData;

    public CardAdapter(Context mContext, List<CardModal> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.card_image_layout, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardAdapter.MyViewHolder holder, final int position) {

        holder.card_title.setText(mData.get(position).card_title);
        holder.posted_by.setText(mData.get(position).posted_by);
        holder.card_description.setText(mData.get(position).card_description);
        Picasso.get().load(mData.get(position).getCard_profile_image()).into(holder.profile_photo);
        Picasso.get().load(mData.get(position).getCard_image()).into(holder.card_image);
        holder.postedTime.setText(mData.get(position).postedTime);

        holder.post_upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "post is upvoted", Toast.LENGTH_SHORT).show();
            }
        });

        holder.post_downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "post is downvoted", Toast.LENGTH_SHORT).show();
            }
        });

        holder.post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "comment is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.post_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "share is clicked", Toast.LENGTH_SHORT).show();
                Picasso.get().load(mData.get(position).getCard_image()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                        intent1.setType("images/*");
//                        intent.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION );
                        intent1.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                        mContext.startActivity(Intent.createChooser(intent1, "mWallpaper"));
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

        holder.card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SingleImageShowActivity.class);
//                intent.putExtra("card_title", holder.card_title.getText().toString());
//                intent.putExtra("card_description", mData.get(position).card_description);
//                intent.putExtra("posted_by", holder.posted_by.getText().toString());
                intent.putExtra("cardImage", mData.get(position).getCard_image());
//                intent.putExtra("cardProfileImage", mData.get(position).getCard_profile_image());
                mContext.startActivity(intent);

            }
        });

        holder.card_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.card_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new MyViewHolder(view));
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMenuItemClickListener {

        TextView card_title, posted_by, card_description,postedTime;
        ImageView profile_photo, card_image, post_upvote, post_downvote, post_comment, post_share, card_menu, post_after_upvote, post_after_downvote;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_title = itemView.findViewById(R.id.card_title);
            posted_by = itemView.findViewById(R.id.posted_by);
            card_description = itemView.findViewById(R.id.card_description);
            profile_photo = itemView.findViewById(R.id.card_profile_image);
            card_image = itemView.findViewById(R.id.card_image);
            post_upvote = itemView.findViewById(R.id.post_upvote);
            post_downvote = itemView.findViewById(R.id.post_downvote);
            post_comment = itemView.findViewById(R.id.post_comment);
            post_share = itemView.findViewById(R.id.post_share);
            card_menu = itemView.findViewById(R.id.card_menu);
            post_after_upvote = itemView.findViewById(R.id.post_after_upvote);
            post_after_downvote = itemView.findViewById(R.id.post_after_downvote);
            postedTime = itemView.findViewById(R.id.postedTime);

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.card_save:
                    Toast.makeText(mContext, "Save is clicked", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, ProfileActivity.class);
//                    mContext.startActivity(intent);
                    break;
                case R.id.card_hide_post:
                    Toast.makeText(mContext, "hide post is clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.card_give_award:
                    Toast.makeText(mContext, "give award is clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.card_report:
                    Toast.makeText(mContext, "report is clicked", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.card_block_user:
                    Toast.makeText(mContext, "block user is clicked", Toast.LENGTH_SHORT).show();
                    break;

            }
            return true;
        }
    }

    private Uri getLocalBitmapUri(Bitmap bitmap) {
        Uri uri = null;
        try {
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "creddit_" + System.currentTimeMillis() + ".png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }
}

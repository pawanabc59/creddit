package com.example.creddit.Adapter;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.Model.CardModal;
import com.example.creddit.R;
import com.example.creddit.SingleImageShowActivity;
import com.squareup.picasso.Picasso;

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
                Toast.makeText(mContext, "share is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SingleImageShowActivity.class);
                intent.putExtra("card_title", holder.card_title.getText().toString());
//                intent.putExtra("card_description", holder.card_description.toString());
                intent.putExtra("posted_by", holder.posted_by.getText().toString());
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

        TextView card_title, posted_by, card_description;
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
}

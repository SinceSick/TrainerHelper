package com.example.trainerhelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainerhelper.R;
import com.example.trainerhelper.pojo.PlayerElement;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    private List<PlayerElement> playerElements;
    private PlayerListener mPlayerListener;

    public PlayerListAdapter(List<PlayerElement> playerElements, PlayerListener playerListener) {
        this.playerElements = playerElements;
        this.mPlayerListener = playerListener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        PlayerViewHolder playerViewHolder = new PlayerViewHolder(v, mPlayerListener);
        return playerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerListAdapter.PlayerViewHolder holder, int position) {
        holder.name.setText(playerElements.get(position).getAbr());
        if(playerElements.get(position).getGame() == 1){
            holder.editImage.setVisibility(View.GONE);
            holder.deleteImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return playerElements.size();
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView name;
        ImageView editImage, deleteImage;
        PlayerListener playerListener;


        PlayerViewHolder(View itemView, PlayerListener playerListener) {
            super(itemView);
            this.playerListener = playerListener;
            cardView = itemView.findViewById(R.id.playerCardView);
            name = itemView.findViewById(R.id.playerName);
            editImage = itemView.findViewById(R.id.editImage);
            deleteImage = itemView.findViewById(R.id.deleteImage);
            cardView.setOnClickListener(this);
            editImage.setOnClickListener(this);
            deleteImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editImage:
                    playerListener.onEditClick(getAdapterPosition());
                    break;
                case R.id.deleteImage:
                    playerListener.onDeleteClick(getAdapterPosition());
                    break;
                case R.id.playerCardView:
                    playerListener.onPlayerClick(getAdapterPosition());
                    break;
            }
        }

    }
    public interface PlayerListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onPlayerClick(int position);
    }
}

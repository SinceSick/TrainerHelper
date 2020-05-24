package com.example.trainerhelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainerhelper.R;
import com.example.trainerhelper.TrainingActivity;
import com.example.trainerhelper.pojo.PlayerElement;

import java.util.List;

public class TrainingElementsAdapter extends RecyclerView.Adapter<TrainingElementsAdapter.TrainingPlayersViewHolder> {
    private List<PlayerElement> playerElements;
    private TrainingPlayersListener mPlayerListener;

    public TrainingElementsAdapter(List<PlayerElement> playerElements, TrainingPlayersListener playerListener) {
        this.playerElements = playerElements;
        this.mPlayerListener = playerListener;
    }


    @NonNull
    @Override
    public TrainingPlayersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_training_elements_item,parent,false);
        TrainingPlayersViewHolder trainingPlayersViewHolder = new TrainingPlayersViewHolder(v,mPlayerListener);
        return trainingPlayersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingElementsAdapter.TrainingPlayersViewHolder holder, int position) {
        holder.name.setText(playerElements.get(position).getAbr());
        holder.hitsCount.setText(playerElements.get(position).getCountString());
    }

    @Override
    public int getItemCount() {
        return playerElements.size();
    }

    public static class TrainingPlayersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardView;
        TextView name,hitsCount;
        TrainingPlayersListener trainingPlayersListener;

        TrainingPlayersViewHolder(View itemView, TrainingPlayersListener trainingPlayersListener){
            super(itemView);
            this.trainingPlayersListener = trainingPlayersListener;
            cardView = itemView.findViewById(R.id.playerCardView);
            name = itemView.findViewById(R.id.playerName);
            hitsCount = itemView.findViewById(R.id.hitsCount);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.playerCardView){
                trainingPlayersListener.onPlayerClick(getAdapterPosition());
            }
        }
    }

    public interface TrainingPlayersListener {
        void onPlayerClick(int position);
    }
}

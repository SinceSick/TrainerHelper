package com.example.trainerhelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainerhelper.R;
import com.example.trainerhelper.pojo.GameElement;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    private List<GameElement> gameElements;
    private GameListener mGameListener;

    public GameListAdapter(List<GameElement> gameElements, GameListener gameListener){
        this.gameElements = gameElements;
        this.mGameListener = gameListener;
    }


    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item,parent,false);
        GameViewHolder gameViewHolder = new GameViewHolder(v,mGameListener);
        return gameViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameListAdapter.GameViewHolder holder, int position) {
        holder.fpName.setText(gameElements.get(position).getFpName());
        holder.spName.setText(gameElements.get(position).getSpName());
        holder.fpFirstSet.setText(String.valueOf(gameElements.get(position).getFpFirstSet()));
        holder.spFirstSet.setText(String.valueOf(gameElements.get(position).getSpFirstSet()));
        holder.fpSecondSet.setText(String.valueOf(gameElements.get(position).getFpSecondSet()));
        holder.spSecondSet.setText(String.valueOf(gameElements.get(position).getSpSecondSet()));
        holder.fpThirdSet.setText(String.valueOf(gameElements.get(position).getFpThirdSet()));
        holder.spThirdSet.setText(String.valueOf(gameElements.get(position).getSpThirdSet()));
        holder.gameName.setText(gameElements.get(position).getGameName());
        holder.date.setText(gameElements.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return gameElements.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardView;
        TextView fpName,fpFirstSet,fpSecondSet,fpThirdSet,spName,spFirstSet,spSecondSet,spThirdSet,gameName,date;
        GameListener gameListener;
        GameViewHolder(View itemView, GameListener gameListener){
            super(itemView);
            this.gameListener = gameListener;
            cardView = itemView.findViewById(R.id.gameCardView);
            fpName = itemView.findViewById(R.id.fpName);
            fpFirstSet = itemView.findViewById(R.id.fpFirstSet);
            fpSecondSet = itemView.findViewById(R.id.fpSecondSet);
            fpThirdSet = itemView.findViewById(R.id.fpThirdSet);
            spName = itemView.findViewById(R.id.spName);
            spFirstSet = itemView.findViewById(R.id.spFirstSet);
            spSecondSet = itemView.findViewById(R.id.spSecondSet);
            spThirdSet = itemView.findViewById(R.id.spThirdSet);
            gameName = itemView.findViewById(R.id.gameName);
            date = itemView.findViewById(R.id.gameDate);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            gameListener.onGameClick(getAdapterPosition());
        }
    }

    public interface GameListener{
        void onGameClick(int position);
    }
}

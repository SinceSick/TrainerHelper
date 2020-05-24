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
import com.example.trainerhelper.pojo.SelectDay;

import java.util.List;

public class AddGroupAdapter extends RecyclerView.Adapter<AddGroupAdapter.SelectDayViewHolder> {

    List<SelectDay> selectDays;
    AddGroupListener mAddGroupListener;



    public AddGroupAdapter(List<SelectDay> selectDays, AddGroupListener addGroupListener){
        this.selectDays = selectDays;
        this.mAddGroupListener = addGroupListener;
    }

    @NonNull
    @Override
    public SelectDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_day_item, parent,false);
        SelectDayViewHolder selectDayViewHolder = new SelectDayViewHolder(v,mAddGroupListener);
        return selectDayViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectDayViewHolder holder, int position) {
        holder.dayOfWeek.setText(selectDays.get(position).getDayOfWeek());
        holder.dayOfWeek.setTextColor(selectDays.get(position).getFontColor());
        holder.cardView.setCardBackgroundColor(selectDays.get(position).getBackground());
    }

    @Override
    public int getItemCount() {
        return selectDays.size();
    }

    public static class SelectDayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView dayOfWeek;
        AddGroupListener addGroupListener;

        SelectDayViewHolder(View itemView, AddGroupListener addGroupListener){
            super(itemView);
            cardView = itemView.findViewById(R.id.dayCardView);
            dayOfWeek = itemView.findViewById(R.id.dayOfWeek);
            this.addGroupListener = addGroupListener;
            cardView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            addGroupListener.onDayClick(getAdapterPosition());
        }
    }

    public interface AddGroupListener {
        void onDayClick(int position);
    }


}

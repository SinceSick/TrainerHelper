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
import com.example.trainerhelper.pojo.GroupElement;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private List<GroupElement> groupElements;
    private GroupListener mGroupListener;

    public GroupListAdapter(List<GroupElement> groupElements, GroupListener groupListener){
        this.groupElements = groupElements;
        this.mGroupListener = groupListener;
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        GroupViewHolder groupViewHolder = new GroupViewHolder(v,mGroupListener);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.GroupViewHolder holder, int position) {
        holder.days.setText(groupElements.get(position).getDays());
        holder.name.setText(groupElements.get(position).getName());
        holder.time.setText(groupElements.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return groupElements.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardView;
        TextView name,days,time;
        ImageView editImage, deleteImage;
        GroupListener groupListener;


        GroupViewHolder(View itemView, GroupListener groupListener){
            super(itemView);
            this.groupListener = groupListener;
            cardView = itemView.findViewById(R.id.groupCardView);
            name = itemView.findViewById(R.id.groupName);
            days = itemView.findViewById(R.id.days);
            time = itemView.findViewById(R.id.time);
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
                    groupListener.onEditClick(getAdapterPosition());
                    break;
                case R.id.deleteImage:
                    groupListener.onDeleteClick(getAdapterPosition());
                    break;
                case R.id.groupCardView:
                    groupListener.onGroupClick(getAdapterPosition());
                    break;
            }
        }
    }

    public interface GroupListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onGroupClick(int position);
    }
}

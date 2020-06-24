package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Places data from model into a row in recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnLongClickListener{
        void onItemLongClick(int pos);
    }
    public interface OnClickListener{
        void onItemClick(int pos);
    }
    List<String> items;
    OnLongClickListener listener;
    OnClickListener click;

    public ItemsAdapter(List<String> i, OnLongClickListener l, OnClickListener c) {
        items = i;
        listener = l;
        click = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layout inflater to inflate view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //Wrap inside ViewHolder then return it
        return new ViewHolder(todoView);
    }

    //binds data to view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //grab item @ position
        String item = items.get(position);
        //bind item to specified view holder
        holder.bind(item);
    }

    // tells RV num items in list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem = itemView.findViewById(android.R.id.text1);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        //update view inside view holder w/ data of item
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.onItemClick(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // notify listener to which position was long pressed
                    listener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}

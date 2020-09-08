package com.example.mvmodelview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends ListAdapter<Note, RecyclerViewAdapter.MyViewHolder>
{
    // We don't require List<Note> notes variable as we will pass the List to the super class ListAdapter and it will take care of it.
    // private List<Note> notes = new ArrayList<>();

    private Context context;
    private OnItemClickListener listener;

    // DiffUtil will take care of the comparision b/w new list and old list.
    public RecyclerViewAdapter()
    {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>()
    {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem)
        {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem)
        {
            // Don't use like this : oldItem.equals(newItem) ---> It will just look that for have same java object reference and it will always false because our
            // liveData provides us the whole new list whenever a change happen which means that the new item we never be same as old item even if they both have
            // the same id.
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        // Note currentNote = notes.get(position);

        // As we passed the list directly to the super class we used getItem().
        Note currentNote = getItem(position);

        holder.title.setText(currentNote.getTitle());
        holder.description.setText(currentNote.getDescription());
        holder.priority.setText(String.valueOf(currentNote.getPriority()));
    }

    // ListAdapter class will take care of it.
//    @Override
//    public int getItemCount() {
//        return notes.size();
//    }

    // If you make class as access then you can't access OnItemClickListener instance as static class and method can access static members only.
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title, description, priority;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.text_view_title);
            description = itemView.findViewById(R.id.text_view_description);
            priority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();

                    // RecyclerView.NO_POSITION ensures that we clicked on the valid position. In case when you have deleted some item but it's still in the
                    // deletion animation state and during the period you clicked the item ( as it's index will be -1 which is invalid ).
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        // listener.onItemClick(notes.get(position));

                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

//    public void setNotes(List<Note> notes)
//    {
//        this.notes = notes;
//
//        notifyDataSetChanged();
//    }

    public Note getNoteAt(int position)
    {
        // return notes.get(position);

        return getItem(position);
    }

    public interface OnItemClickListener
    {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
}
package com.zybooks.inventoryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends
        RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private Context context;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, quantityTextView;
        public Button deleteButton;
        public ImageButton increaseButton, decreaseButton;

        // Create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.item_name);
            quantityTextView = itemView.findViewById(R.id.item_quantity);
            deleteButton = itemView.findViewById(R.id.delete_button);
            increaseButton = itemView.findViewById(R.id.increase_button);
            decreaseButton = itemView.findViewById(R.id.decrease_button);
        }
    }
    // Store a member variable for the items
    private List<Item> mItems;

    private ItemDatabase mItemDb;

    // Pass in the item array into the constructor
    public ItemsAdapter(Context context, List<Item> items) {
        this.context = context;
        mItems = items;
        mItemDb = new ItemDatabase(context);
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.rv_items, parent, false);

        // Return a new holder instance
        return new ViewHolder(itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Item item = mItems.get(position);

        // Set item views
        holder.nameTextView.setText(item.getName());
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));

        // Delete item when delete button is clicked
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemDb.deleteItem(item.getName());
                removeItem(item);
            }
        });

        // Increase item's quantity by 1 when increase button is clicked
        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = item.getQuantity() + 1;
                item.setQuantity(newQuantity);
                mItemDb.updateItem(item);
                notifyDataSetChanged();
            }
        });

        // Decrease item's quantity by 1 when decrease button is clicked
        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = item.getQuantity() - 1;
                item.setQuantity(newQuantity);
                mItemDb.updateItem(item);
                notifyDataSetChanged();
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void removeItem(Item item) {
        int index = mItems.indexOf(item);

        if (index >= 0) {
            // Remove the item
            mItems.remove(index);

            notifyItemRemoved(index);
        }
    }
}

package com.buddies.catchwo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.BookDetailsScreen;
import com.buddies.catchwo.Filter.BookFilter;
import com.buddies.catchwo.Filter.UserSearch;
import com.buddies.catchwo.Model.BookModel;
import com.buddies.catchwo.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.Holder> implements Filterable {


    Context context;
    public ArrayList<BookModel> arrayList,FilterList;
    private BookFilter filter;

    public BookAdapter(Context context, ArrayList<BookModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.FilterList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        Glide.with(context).load(arrayList.get(position).getImage()).into(holder.cover);

        String Id = arrayList.get(position).getBookID();
        String type = arrayList.get(position).getType();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailsScreen.class);
                intent.putExtra("id",Id);
                intent.putExtra("type",type);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new BookFilter(this,FilterList);
        }

        return filter;
    }


    public class Holder extends RecyclerView.ViewHolder {

        ImageView cover;

        public Holder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.cover);

        }
    }
}

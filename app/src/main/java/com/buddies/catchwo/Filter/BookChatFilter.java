package com.buddies.catchwo.Filter;

import android.widget.Filter;

import com.buddies.catchwo.Adapter.BookChatAdapter;
import com.buddies.catchwo.Model.BookChatModel;
import com.buddies.catchwo.Model.FriendModel;

import java.util.ArrayList;

public class BookChatFilter extends Filter {

    private BookChatAdapter adapter;
    private ArrayList<BookChatModel> filterList;

    public BookChatFilter(BookChatAdapter adapter, ArrayList<BookChatModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();

            ArrayList<BookChatModel> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getName().toUpperCase().contains(constraint)
                ){
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.arrayList = (ArrayList<BookChatModel>) results.values;

        adapter.notifyDataSetChanged();

        String fun= "";
    }

}

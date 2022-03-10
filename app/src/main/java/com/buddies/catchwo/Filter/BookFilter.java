package com.buddies.catchwo.Filter;

import android.widget.Filter;

import com.buddies.catchwo.Adapter.BookAdapter;
import com.buddies.catchwo.Model.BookModel;
import com.buddies.catchwo.Model.Users;

import java.util.ArrayList;

public class BookFilter extends Filter {

    private BookAdapter adapter;
    private ArrayList<BookModel> filterList;

    public BookFilter(BookAdapter adapter, ArrayList<BookModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();

            ArrayList<BookModel> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getName().toUpperCase().contains(constraint) ||
                        filterList.get(i).getCategory().toUpperCase().contains(constraint) ||
                        filterList.get(i).getWritten().toUpperCase().contains(constraint)
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
        adapter.arrayList = (ArrayList<BookModel>) results.values;

        adapter.notifyDataSetChanged();

        String fun= "";
    }
}


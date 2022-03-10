package com.buddies.catchwo.Filter;

import android.widget.Filter;

import com.buddies.catchwo.Adapter.MemoryAdapter;
import com.buddies.catchwo.Adapter.PostAdapter;
import com.buddies.catchwo.Model.PostModel;

import java.util.ArrayList;

public class PostFilter extends Filter {

    private PostAdapter adapter;
    private ArrayList<PostModel> filterList;

    public PostFilter(PostAdapter adapter, ArrayList<PostModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();

            ArrayList<PostModel> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getDesc().toUpperCase().contains(constraint)
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
        adapter.arrayList = (ArrayList<PostModel>) results.values;

        adapter.notifyDataSetChanged();

        String fun= "";
    }
}


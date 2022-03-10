package com.buddies.catchwo.Filter;

import android.widget.Filter;

import com.buddies.catchwo.Adapter.FriendAdapter;
import com.buddies.catchwo.Model.FriendModel;
import com.buddies.catchwo.Model.Users;

import java.util.ArrayList;

public class FriendFilter extends Filter{

    private FriendAdapter adapter;
    private ArrayList<FriendModel> filterList;

    public FriendFilter(FriendAdapter adapter, ArrayList<FriendModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();

        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();

            ArrayList<FriendModel> filteredModels = new ArrayList<>();
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
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        adapter.arrayList = (ArrayList<FriendModel>) results.values;

        adapter.notifyDataSetChanged();

        String fun= "";
    }
}

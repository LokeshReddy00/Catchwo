package com.buddies.catchwo.Filter;

import android.widget.Filter;

import com.buddies.catchwo.Adapter.CommitteeAdapter;
import com.buddies.catchwo.Model.CommitteeModel;
import com.buddies.catchwo.Model.Users;

import java.util.ArrayList;

public class CommitteeFilter extends Filter {

    private CommitteeAdapter adapter;
    private ArrayList<CommitteeModel> filterList;

    public CommitteeFilter(CommitteeAdapter adapter, ArrayList<CommitteeModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();

            ArrayList<CommitteeModel> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getComm_name().toUpperCase().contains(constraint) ||
                        filterList.get(i).getEmail().toUpperCase().contains(constraint) ||
                        filterList.get(i).getCreatedBy().toUpperCase().contains(constraint)||
                        filterList.get(i).getPhone().toUpperCase().contains(constraint)
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
        adapter.arrayList = (ArrayList<CommitteeModel>) results.values;

        adapter.notifyDataSetChanged();

        String fun= "";
    }
}

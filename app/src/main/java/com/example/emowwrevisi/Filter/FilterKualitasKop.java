package com.example.emowwrevisi.Filter;

import android.widget.Filter;

import com.example.emowwrevisi.Adapter.AdapterKualitasKop;
import com.example.emowwrevisi.Adapter.AdapterSetoranKop;
import com.example.emowwrevisi.Model.ModelKualitasKop;
import com.example.emowwrevisi.Model.ModelSetoranKop;

import java.util.ArrayList;

public class FilterKualitasKop extends Filter {
    private AdapterKualitasKop adapterKualitasKop;
    private ArrayList<ModelKualitasKop> filterList;

    public FilterKualitasKop(AdapterKualitasKop adapterKualitasKop, ArrayList<ModelKualitasKop> filterList) {
        this.adapterKualitasKop = adapterKualitasKop;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //validate data for search query
        if (constraint !=null && constraint.length()>0){
            //change to upper case, to make case insensitive
            constraint = constraint.toString().toUpperCase();
            //store our filtered list
            ArrayList<ModelKualitasKop> filteredModels = new ArrayList<>();
            for (int i=0; i<filterList.size(); i++){
                //check, search by title and catagory
                if (filterList.get(i).getNamapeternak().toUpperCase().contains(constraint)||
                        filterList.get(i).getNamapeternak().toLowerCase().contains(constraint)){
                    //add filtered data to list
                    filteredModels.add(filterList.get(i));
                }

            }
            results.count = filteredModels.size();
            results.values = filteredModels;

        } else {
            //search filed empty, not searching, return orginal/all/complate list
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterKualitasKop.kualitasKops = (ArrayList<ModelKualitasKop>)  results.values;
        //refersh adapter
        adapterKualitasKop.notifyDataSetChanged();

    }
}

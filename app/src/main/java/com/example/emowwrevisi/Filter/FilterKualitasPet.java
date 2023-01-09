package com.example.emowwrevisi.Filter;

import android.widget.Filter;

import com.example.emowwrevisi.Adapter.AdapterKualitasKop;
import com.example.emowwrevisi.Adapter.AdapterKualitasPet;
import com.example.emowwrevisi.Model.ModelKualitasKop;
import com.example.emowwrevisi.Model.ModelKualitasPet;

import java.util.ArrayList;

public class FilterKualitasPet extends Filter {
    private AdapterKualitasPet adapterKualitasPet;
    private ArrayList<ModelKualitasPet> filterList;

    public FilterKualitasPet(AdapterKualitasPet adapterKualitasPet, ArrayList<ModelKualitasPet> filterList) {
        this.adapterKualitasPet = adapterKualitasPet;
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
            ArrayList<ModelKualitasPet> filteredModels = new ArrayList<>();
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
        adapterKualitasPet.kualitasPets = (ArrayList<ModelKualitasPet>)  results.values;
        //refersh adapter
        adapterKualitasPet.notifyDataSetChanged();

    }
}

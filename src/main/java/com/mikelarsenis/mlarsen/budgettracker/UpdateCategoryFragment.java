package com.mikelarsenis.mlarsen.budgettracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateCategoryFragment extends Fragment {

    View rootView;
    Spinner spnrUpdateCategories;
    EditText edtUpdateCatName;
    EditText edtUpdateAllotment;

    public UpdateCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_update_category, container, false);

        //hide menu items
        MainActivity ma = (MainActivity) getActivity();
        ma.hideMenuItems();

        //get values to add to spinner
        String [] spinnerValues = new String[ma.getCategoryValuesSize()];

        //populate array
        for(int i=0; i<ma.getCategoryValuesSize(); i++)
        {
            spinnerValues[i] = ma.getCategoryValuesAtIndex(i).name;
        }

        //wire up dynamic elements
        edtUpdateAllotment = (EditText) rootView.findViewById(R.id.edtUpdateAllotment);
        edtUpdateCatName = (EditText) rootView.findViewById(R.id.edtUpdateCatName);

        spnrUpdateCategories = (Spinner) rootView.findViewById(R.id.spnrUpdateCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrUpdateCategories.setAdapter(adapter);

        spnrUpdateCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    //gets update values and saves to ArrayList
    public void updateCategory()
    {
        String catName = spnrUpdateCategories.getSelectedItem().toString();

        //find index of category with value
        MainActivity ma = (MainActivity) getActivity();
        int index = -1;
        for(int i = 0; i<ma.getCategoryValuesSize();i++)
        {
            if(catName.equals(ma.getCategoryValuesAtIndex(i).name))
            {
                index = i;
                break;
            }
        }

        double value = Double.parseDouble(edtUpdateAllotment.getText().toString());

        ma.setCategoryValuesAtIndex(index, edtUpdateCatName.getText().toString(), (int) (value*100));

        //remove any expense under category name
        Iterator<Expense> it = ma.getExpenseValues().iterator();
        while(it.hasNext())
        {
            Expense expense = it.next();
            if(expense.catName.equals(catName))
                it.remove();
        }

        ma.displayBudgetList();
    }

    //updates editText values when spinner changed
    private void updateValues()
    {
        String catName = spnrUpdateCategories.getSelectedItem().toString();

        //find index of category with value
        MainActivity ma = (MainActivity) getActivity();
        int index = -1;
        for(int i = 0; i<ma.getCategoryValuesSize();i++)
        {
            if(catName.equals(ma.getCategoryValuesAtIndex(i).name))
            {
                index = i;
                break;
            }
        }

        edtUpdateCatName.setText(ma.getCategoryValuesAtIndex(index).name.toString());

        double value = (ma.getCategoryValuesAtIndex(index).monthlyValue);
        value /= 100;
        DecimalFormat df = new DecimalFormat("0.00");

        edtUpdateAllotment.setText(df.format(value));

    }

}

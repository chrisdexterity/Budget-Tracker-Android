package com.mikelarsenis.mlarsen.budgettracker;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteCategoryFragment extends Fragment {

    Spinner spnrDeleteCategories;

    public DeleteCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delete_category, container, false);
        MainActivity ma = (MainActivity) getActivity();
        TextView txvCatRemove = (TextView) rootView.findViewById(R.id.txvCatRemove);
        txvCatRemove.setTypeface(Typeface.DEFAULT);

        //hide menu items
        ma.hideMenuItems();

        //get values to add to spinner
        String [] spinnerValues = new String[ma.getCategoryValuesSize()];

        //populate array
        for(int i=0; i<ma.getCategoryValuesSize(); i++)
        {
            spinnerValues[i] = ma.getCategoryValuesAtIndex(i).name;
        }

        //wire up dynamic spinner
        spnrDeleteCategories = (Spinner) rootView.findViewById(R.id.spnrDeleteCategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrDeleteCategories.setAdapter(adapter);

        return rootView;
    }

    //gets the values from the spinner and returns to MainActivity to remove from ArrayList
    public void removeCategory()
    {
        // Yes/No Dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(false);
        builder.setTitle("Permanently Remove Category?");
        builder.setMessage("Are you sure you want to permanently delete "+ spnrDeleteCategories.getSelectedItem().toString() +"?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                MainActivity ma = (MainActivity) getActivity();
                ma.removeCategoryVar(spnrDeleteCategories.getSelectedItem().toString());
                ma.displayBudgetList();

                Toast toast = Toast.makeText(ma, "Category Deleted", Toast.LENGTH_SHORT);
                toast.show();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // do nothing
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}

package com.mikelarsenis.mlarsen.budgettracker;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * Fragment used to create other categories
 */
public class NewCategoryFragment extends Fragment
{
    View rootView;
    EditText edtNewCatName;
    EditText edtNewCatMonthlyAmount;

    public NewCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_new_category, container, false);

        //wire up dynamic elements
        edtNewCatName = (EditText) rootView.findViewById(R.id.edtNewCatName);
        edtNewCatMonthlyAmount = (EditText) rootView.findViewById(R.id.edtNewMonthlyAmountVal);

        //hide menu buttons
        MainActivity ma = (MainActivity) getActivity();
        ma.hideMenuItems();

        return rootView;
    }

    public void addCategory()
    {
        if( edtNewCatName.getText().toString().isEmpty() || edtNewCatMonthlyAmount.getText().toString().isEmpty() )
        {
            //dialog box alerting of blank spaces
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setCancelable(false);
            builder.setTitle("Required Information");
            builder.setMessage("Please enter new category name and monthly value.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //do nothing
                }

            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else //got all the information needed
        {
            MainActivity ma = (MainActivity) getActivity();
            int monthlyVal = (int) (Double.parseDouble((edtNewCatMonthlyAmount.getText().toString()))*100);
            ma.createNewCategoryVar(edtNewCatName.getText().toString(), monthlyVal, monthlyVal);
            ma.displayBudgetList();
        }
    }

}

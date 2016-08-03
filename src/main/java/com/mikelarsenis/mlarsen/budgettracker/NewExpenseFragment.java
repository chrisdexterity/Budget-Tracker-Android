package com.mikelarsenis.mlarsen.budgettracker;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewExpenseFragment extends Fragment {

    View rootView;
    Spinner spnrNewExpense;
    EditText edtExpenseAmount;
    EditText edtExpenseNotes;

    public NewExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.fragment_new_expense, container, false);

        //get values to add to spinner
        MainActivity ma = (MainActivity) getActivity();
        String [] spinnerValues = new String[ma.getCategoryValuesSize()];

        //populate array
        for(int i=0; i<ma.getCategoryValuesSize(); i++)
        {
            spinnerValues[i] = ma.getCategoryValuesAtIndex(i).name;
        }

        spnrNewExpense = (Spinner) rootView.findViewById(R.id.spnrNewExpense);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrNewExpense.setAdapter(adapter);

        //wire up editViews
        edtExpenseAmount = (EditText) rootView.findViewById(R.id.edtExpenseValue);
        edtExpenseNotes = (EditText) rootView.findViewById(R.id.edtExpenseNotes);

        return rootView;
    }

    public void addExpense()
    {
        MainActivity ma = (MainActivity) getActivity();

        if(edtExpenseAmount.getText().toString().equals(""))
        {
            //dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setCancelable(false);
            builder.setTitle("Missing amount");
            builder.setMessage("Please enter an amount for the expense.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
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
        else
        {
            double temp = Double.parseDouble(edtExpenseAmount.getText().toString());
            int value = (int) (temp*100);

            ma.createNewExpenseVar(spnrNewExpense.getSelectedItem().toString(),
                    value, edtExpenseNotes.getText().toString());

            ma.displayBudgetList();
        }
    }

}

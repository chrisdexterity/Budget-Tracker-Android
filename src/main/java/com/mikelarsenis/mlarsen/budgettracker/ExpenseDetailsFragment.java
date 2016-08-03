package com.mikelarsenis.mlarsen.budgettracker;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseDetailsFragment extends Fragment
{

    View rootView;
    ListView lsvExpenses;

    public ExpenseDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_expense_details, container, false);

        lsvExpenses = (ListView) rootView.findViewById(R.id.lsvExpenses);

        return rootView;
    }



    //populates the list of expenses for a given category
    public void setList(final String catName)
    {
        //long item pressed listener
        lsvExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int index = position;

                AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity) getActivity());

                builder.setCancelable(false);
                builder.setTitle("Remove Expense?");
                builder.setMessage("Are you sure you want to permanently remove this expense?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        MainActivity ma = (MainActivity) getActivity();

                        //add expense amount back to category
                        int iExpenseAmount = ma.getExpenseValuesAtIndex(index).amount;
                        ma.addToCategoryVal(catName, iExpenseAmount);

                        ma.removeExpenseVar(index);


                        double dExpenseAmount = (double)(iExpenseAmount) /100;
                        DecimalFormat df = new DecimalFormat("$###,##0.00");
                        Toast toast = Toast.makeText(ma,"Expense Removed.\n\n" + df.format(dExpenseAmount) +
                                                         " added back to " + catName, Toast.LENGTH_LONG);
                        //center toast text
                        TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
                        if( view != null) view.setGravity(Gravity.CENTER);
                        toast.show();

                        setList(catName);
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

                return true;
            }
        });



        //set label
        TextView txvCategoryExpensesLabel = (TextView) rootView.findViewById(R.id.txvCategoryExpensesLabel);
        txvCategoryExpensesLabel.setText(catName + " Expenses");

        MainActivity ma = (MainActivity) getActivity();

        String[] expenses = new String[ma.getExpenseValues().size()];

        //create adapter using expenses list
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1);

        Iterator<Expense> it = ma.getExpenseValues().iterator();
        boolean empty = true;

        while(it.hasNext())
        {
            Expense ex = it.next();

            //remove any trailing whitespace
            String temp = ex.catName;
            temp = ma.removeTrailingWhitespace(temp);

            if(temp.equals(catName))
            {
                empty = false;
                double amount = ((double) ex.amount)/100;
                DecimalFormat df = new DecimalFormat("$#,##0.00");

                if(!ex.notes.equals(""))
                {
                    adapter.add("Date: " + ex.date +
                            "\nAmount: " + df.format(amount) +
                            "\n   Notes: " + ex.notes);
                }
                else // no notes
                {
                    adapter.add("Date: " + ex.date +
                            "\nAmount: " + df.format(amount));
                }
            }
        }
        lsvExpenses.setAdapter(adapter);

        TextView txvNoExpenses = (TextView) rootView.findViewById(R.id.txvNoExpenses);
        //no expenses
        if(empty)
        {
            //hide no expenses textView
            txvNoExpenses.setVisibility(View.VISIBLE);
            lsvExpenses.setVisibility(View.GONE);
            txvNoExpenses.setText("There are no expenses for the " + catName + " category.");
        }
        else
        {
            txvNoExpenses.setVisibility(View.GONE);
            lsvExpenses.setVisibility(View.VISIBLE);

            Toast toast = Toast.makeText(ma, "Long press an item to delete it.", Toast.LENGTH_SHORT);
            toast.show();
        }

        //refresh fragment
        rootView.invalidate();
    }



}

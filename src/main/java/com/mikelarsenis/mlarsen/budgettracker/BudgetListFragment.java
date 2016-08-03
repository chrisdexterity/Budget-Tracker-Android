package com.mikelarsenis.mlarsen.budgettracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Iterator;

/**
 * Holds category fragments. Allows for creating new expenses from this fragment
 */
public class BudgetListFragment extends Fragment
{

    View rootView;
    boolean listPopulated = false;
    TextView txvBudgetTotal;
    TextView txvTotalRemaining;
    TextView txvTotalSpent;
    TextView txvCategories;

    public BudgetListFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_budget_list, container, false);

        return rootView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("Lifecycle", "BudgetListFragment onPause()");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("Lifecycle", "BudgetListFragment onResume()");

        //wire up buttons
        Button btnAddCat = (Button) rootView.findViewById(R.id.btnAddCat);
        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) getActivity();
                ma.displayNewCategory();
            }
        });

        Button btnNewExpense = (Button) rootView.findViewById(R.id.btnNewExpense);
        btnNewExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open new item fragment
                MainActivity ma = (MainActivity) getActivity();
                ma.displayNewExpense();
            }
        });

        MainActivity ma = (MainActivity) getActivity();
        //wire up dynamic elements
        TextView txvCategoryInstructions = (TextView) rootView.findViewById(R.id.txvBudgetListInstructions);
        txvBudgetTotal = (TextView) rootView.findViewById(R.id.txvTotalBudget);

        //set total amount
        double value =  ma.getTotalBudgetAmount();
        DecimalFormat df = new DecimalFormat("$0.00");
        String stringValue = "Total Budget:\n" + df.format(value);

        txvBudgetTotal.setText( stringValue );

        //set remaining amount
        txvTotalRemaining = (TextView) rootView.findViewById(R.id.txvTotalRemaining);
        value = ma.getTotalRemainingAmount();
        stringValue = "Remaining:\n" + df.format(value);
        txvTotalRemaining.setText( stringValue );
        if(ma.getExpenseValues().size()>1 && value < 1)
        {
            txvTotalRemaining.setTextColor(Color.RED);
            if(ma.firstLoad)
            {
                ma.firstLoad = false;
                Toast toast = Toast.makeText(ma, "You have currently spent your total budget.\nTime to bust out the Ramen Noodles...", Toast.LENGTH_LONG);
                //center toast text
                TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
                if( view != null)
                    view.setGravity(Gravity.CENTER);
                toast.show();
            }
        }

        //set total spent amount
        txvTotalSpent = (TextView) rootView.findViewById(R.id.txvTotalSpent);
        value = ma.getTotalSpentAmount();
        stringValue = "Spent:\n" + df.format(value);
        txvTotalSpent.setText( stringValue );
        View vTotals = rootView.findViewById(R.id.Totals);
        txvCategories = (TextView) rootView.findViewById(R.id.txvCategories);


        if(!listPopulated)  //list has not been populated, populate it
        {
            listPopulated = true;

            FragmentManager fm = getChildFragmentManager();

            if(ma.getCategoryValuesSize() > 0)
            {
                btnNewExpense.setVisibility(View.VISIBLE);
                vTotals.setVisibility(View.VISIBLE);

                //hide no cats instructions
                txvCategoryInstructions.setVisibility(View.GONE);

                //iterate through category list and add to fragment
                Iterator<Category> it = ma.getCategoryValues().iterator();
                while(it.hasNext())
                {
                    Category category = it.next();
                    CategoryFragment categoryFragment = new CategoryFragment();
                    fm.beginTransaction()
                            .add(R.id.categoryContainer, categoryFragment, "")
                            .commit();
                    fm.executePendingTransactions();
                    categoryFragment.setValues(category.name, category.monthlyValue, category.currentValue);
                }
                fm.executePendingTransactions();
            }
            else //empty list
            {
                //hide new expense button
                btnAddCat.setVisibility(View.VISIBLE);
                btnNewExpense.setVisibility(View.GONE);
                vTotals.setVisibility(View.GONE);
                txvCategories.setVisibility(View.GONE);

                ma.hideUpdateDeleteItems();

                //show instructions
                txvCategoryInstructions.setVisibility(View.VISIBLE);
            }
        }



    } // end resume

}

package com.mikelarsenis.mlarsen.budgettracker;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewExpenseButtonsFragment extends Fragment {


    public NewExpenseButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_new_expense_buttons, container, false);

        //wire up buttons
        Button btnCancelExpense = (Button) rootView.findViewById(R.id.btnCancelExpense);
        btnCancelExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) getActivity();

                //hide keyboard
                View view = ma.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)ma.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ma.displayBudgetList();
            }
        });

        Button btnAddExpense = (Button) rootView.findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) getActivity();

                //hide keyboard
                View view = ma.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)ma.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ma.newExpense();
            }
        });

        return rootView;
    }

}

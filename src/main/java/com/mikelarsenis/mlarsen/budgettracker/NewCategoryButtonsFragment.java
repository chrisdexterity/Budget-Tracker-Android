package com.mikelarsenis.mlarsen.budgettracker;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
public class NewCategoryButtonsFragment extends Fragment {


    public NewCategoryButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_category_buttons, container, false);

        //wire up dynamic elements
        Button btnCancelNewCat = (Button) rootView.findViewById(R.id.btnCancelNewCat);
        btnCancelNewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) getActivity();

                //hide keyboard
                View view = ma.getCurrentFocus();
                if (view != null)
                {
                    InputMethodManager imm = (InputMethodManager)ma.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ma.displayBudgetList();
            }
        });

        Button btnAdd = (Button) rootView.findViewById(R.id.btnAddNewCat);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) getActivity();

                //hide keyboard
                View view = ma.getCurrentFocus();
                if (view != null)
                {
                    InputMethodManager imm = (InputMethodManager)ma.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ma.addNewCategory();
            }
        });
        return rootView;
    }

}

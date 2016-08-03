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
public class DeleteCategoryButtonsFragment extends Fragment {


    public DeleteCategoryButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delete_category_buttons, container, false);

        //wire up buttons and set onclick listeners
        Button btnCancelDeleteCat = (Button) rootView.findViewById(R.id.btnCancelDeleteCat);
        btnCancelDeleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MainActivity ma = (MainActivity) getActivity();
                ma.displayBudgetList();
            }
        });

        Button btnRemoveDeleteCat = (Button) rootView.findViewById(R.id.btnRemoveDeleteCat);
        btnRemoveDeleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) getActivity();
                ma.removeCategory();
            }
        });
        return rootView;
    }

}

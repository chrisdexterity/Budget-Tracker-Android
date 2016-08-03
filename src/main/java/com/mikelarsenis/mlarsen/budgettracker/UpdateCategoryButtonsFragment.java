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
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateCategoryButtonsFragment extends Fragment {


    public UpdateCategoryButtonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_category_buttons, container, false);

        //wire up buttons and set onclick
        Button btnCancelUpdateCat = (Button) rootView.findViewById(R.id.btnCancelUpdateCat);
        btnCancelUpdateCat.setOnClickListener(new View.OnClickListener() {
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

        Button btnSaveUpdateCat = (Button) rootView.findViewById(R.id.btnSaveUpdateCat);
        btnSaveUpdateCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setCancelable(false);
                builder.setTitle("Update Category?");
                builder.setMessage("Are you sure you want to update this category? This will reset current values for this category and may affect your budget totals.");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        MainActivity ma = (MainActivity) getActivity();

                        //hide keyboard
                        View view = ma.getCurrentFocus();
                        if (view != null)
                        {
                            InputMethodManager imm = (InputMethodManager)ma.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        ma.updateCategory();

                Toast toast = Toast.makeText(ma, "Category Updated", Toast.LENGTH_SHORT);
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
        });

        return rootView;
    }

}

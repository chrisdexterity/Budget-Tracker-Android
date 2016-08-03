package com.mikelarsenis.mlarsen.budgettracker;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.text.DecimalFormat;


/**
 * Used as a template to create new categories
 */
public class CategoryFragment extends Fragment {

    View rootView;
    TextView txvCatName;
    TextView txvCatVal;
    ProgressBar pbCatProgress;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        MainActivity ma = (MainActivity) getActivity();

        //wire up dynamic elements
        txvCatName = (TextView) rootView.findViewById(R.id.txvCatName);
        txvCatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent();
            }
        });

        txvCatVal = (TextView) rootView.findViewById(R.id.txvCatVal);
        txvCatVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent();
            }
        });

        pbCatProgress = (ProgressBar) rootView.findViewById(R.id.pbCatProgress);
        pbCatProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    //set progress bar color
    private static void setProgressColor(ProgressBar pb)
    {
        //setting progress color can only happen on phones LOLLIPOP and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            double progressMax = pb.getMax();
            double progressCurr = pb.getProgress();
            double progressPerc = (progressCurr/progressMax);

            if(.66 < progressPerc ) // 3/3 - 2/3
            {

                pb.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

            }
            else if(.33 < progressPerc) // 2/3 - 1/3
            {
                pb.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            }
            else //less than 1/3
            {
                pb.setProgressTintList(ColorStateList.valueOf(Color.RED));
            }
        }
    }

    //sets values of dynamic elements
    public void setValues(String catName, int progressMax, int progressCurr)
    {


        if (progressCurr < 0)
        {
            DecimalFormat df = new DecimalFormat("$0.00");
            txvCatVal.setTextColor(Color.RED);
            txvCatVal.setText(
                    df.format((double)progressCurr/100)  );
        }
        else
        {
            DecimalFormat df = new DecimalFormat("$0.00");
            txvCatVal.setText(df.format( (double)progressCurr/100) );
        }
        txvCatName.setText( catName + ": " );
        pbCatProgress.setMax(progressMax);
        pbCatProgress.setProgress(progressCurr);
        setProgressColor(pbCatProgress);
    }

    private void onClickEvent()
    {
        String catName = txvCatName.getText().toString();

        //trim tailing whitespace
        catName = catName.replaceFirst("\\s+$", "");

        //remove colon at the end of the catName
        catName = catName.substring(0, catName.length()-1);

        MainActivity ma = (MainActivity) getActivity();
        ma.displayExpenseDetails(catName);
    }


}

package com.mikelarsenis.mlarsen.budgettracker;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

//TODO: Log of past months expenses

public class MainActivity extends AppCompatActivity
{
    boolean login = true;
    public static ArrayList<Category> categoryValues = new ArrayList<>();
    public static ArrayList<Expense> expenseValues = new ArrayList<>();
    Menu myMenu;
    public boolean firstLoad = true;
    //public Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //font = (Typeface)Typeface.createFromAsset(getAssets(),"walkway.ttf");

        //displaySplashScreen();
        displayBudgetList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d("LifeCycle", "MainActivity onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu, menu);
        myMenu = menu;

        if(categoryValues.size()<1)
        {
            myMenu.findItem(R.id.action_deleteCategory).setVisible(false);
            myMenu.findItem(R.id.action_updateCategory).setVisible(false);
            myMenu.findItem(R.id.action_ResetCategories).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_newCategory:
                displayNewCategory();
                return true;
            case R.id.action_deleteCategory:
                displayDeleteCategory();
                return true;
            case R.id.action_updateCategory:
                displayUpdateCatagory();
                return true;
            case R.id.action_ResetCategories:
                resetCategoryValues();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        //display confirmation prompt to exit app
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setTitle("Exit?");
        builder.setMessage("Are you sure you want to exit the app?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
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

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("Lifecycle", "MainActivity onPause()");

        //saving lists to json string
        Gson gson = new Gson();
        String jsonCatList = gson.toJson(categoryValues);
        String jsonExpenseList = gson.toJson(expenseValues);
        Log.d("Persistence", "Set Category Json: " + jsonCatList);
        Log.d("Persistence", "Set Expense Json: " + jsonExpenseList);

        //saving jsons to sharedPrefs
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jsonCatList", jsonCatList);
        editor.putString("jsonExpenseList", jsonExpenseList);
        editor.apply();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("Lifecycle", "MainActivity onResume()");

        //retrieve list jsons
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String jsonCatList = prefs.getString("jsonCatList", null);
        String jsonExpenseList = prefs.getString("jsonExpenseList", null);

        if(jsonCatList != null)
        {
            Log.d("Persistence", "Get Category Json: " + jsonCatList);

            //read json value and set to list
            Type type = new TypeToken<ArrayList<Category>>() {
            }.getType();
            Gson gson = new Gson();
            categoryValues = gson.fromJson(jsonCatList, type);
        }
        if(jsonExpenseList != null)
        {
            Log.d("Persistence", "Get Expense Json: " + jsonExpenseList);
            //read json value and set to list
            Type type = new TypeToken<ArrayList<Expense>>() {
            }.getType();
            Gson gson = new Gson();
            expenseValues = gson.fromJson(jsonExpenseList, type);
        }

    }

    //display budget list fragment
    public void displayBudgetList()
    {
        FragmentManager fm = getSupportFragmentManager();
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);

        if(fm.findFragmentByTag("ExpenseDetails_Frag") != null)
        {
            //show fragmentContainer
            ScrollView fc = (ScrollView) findViewById(R.id.fragmentContainerScrollView);
            fc.setVisibility(View.VISIBLE);

            fm.beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("ExpenseDetails_Frag"))
                    .commit();
            fm.executePendingTransactions();

            //add budget list
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, new BudgetListFragment(), "BudgetList_Frag")
                    .commit();
        }
        else
        {
            //swap to budget list
            fm.beginTransaction()
                    .replace(R.id.fragmentContainer, new BudgetListFragment(), "BudgetList_Frag")
                    .commit();
            fm.executePendingTransactions();
        }


        //remove buttons
        if(fm.findFragmentByTag("NewCat_Buttons") != null)
        {
            fm.beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("NewCat_Buttons"))
                    .commit();
            fm.executePendingTransactions();

            //show menu items
            showMenuItems();
        }
        if(fm.findFragmentByTag("DeleteCat_Buttons") != null)
        {
            fm.beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("DeleteCat_Buttons"))
                    .commit();
            fm.executePendingTransactions();

            //show menu items
            showMenuItems();
        }
        if(fm.findFragmentByTag("UpdateCat_Buttons") != null)
        {
            fm.beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("UpdateCat_Buttons"))
                    .commit();
            fm.executePendingTransactions();

            //show menu items
            showMenuItems();
        }
        if(fm.findFragmentByTag("NewExpense_Buttons") != null)
        {
            fm.beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("NewExpense_Buttons"))
                    .commit();
            fm.executePendingTransactions();

            //show menu items
            showMenuItems();
        }
        if(fm.findFragmentByTag("ExpenseDetails_Buttons") != null)
        {
            fm.beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag("ExpenseDetails_Buttons"))
                    .commit();
            fm.executePendingTransactions();

            //show menu items
            showMenuItems();
        }

    }

    //display New Expense fragment
    public void displayNewExpense()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new NewExpenseFragment(), "NewExpense_Frag")
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.buttonContainer, new NewExpenseButtonsFragment(), "NewExpense_Buttons")
                .commit();

    }

    //display new category fragment
    public void displayNewCategory()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new NewCategoryFragment(), "NewCat_Frag")
                .add(R.id.buttonContainer, new NewCategoryButtonsFragment(), "NewCat_Buttons")
                .commit();
    }


    //getters and setters for values
    public static ArrayList<Category> getCategoryValues() { return categoryValues; }
    public static Category getCategoryValuesAtIndex(int index) {
        return categoryValues.get(index);
    }
    public static void setCategoryValuesAtIndex(int index, String catName, int catAllotment)
    {
        //get item at index
        Category cat = getCategoryValuesAtIndex(index);
        cat.name = catName;
        cat.monthlyValue = catAllotment;
        cat.currentValue = catAllotment;

        categoryValues.set(index, cat);
    }
    public static void setCategoryValuesAtIndex(int index, int currAmount)
    {
        Category cat = getCategoryValuesAtIndex(index);
        Log.d("Expense", "Previous Amount: " + cat.currentValue);
        cat.currentValue -= currAmount;
        Log.d("Expense", "Current Amount: " + cat.currentValue);
    }
    public ArrayList<Expense> getExpenseValues() { return expenseValues; }
    public static Expense getExpenseValuesAtIndex(int index) { return expenseValues.get(index); }

    //returns the size of the category value array list
    public static int getCategoryValuesSize()
    {
        return categoryValues.size();
    }

    //displays fragment to delete categories
    private void displayDeleteCategory()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new DeleteCategoryFragment(), "DeleteCat_Frag")
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.buttonContainer, new DeleteCategoryButtonsFragment(), "DeleteCat_Buttons")
                .commit();
    }

    //transition from NewCategoryButtonsFragment to NewCategoryFragment
    public void addNewCategory()
    {
        NewCategoryFragment nc = (NewCategoryFragment) getSupportFragmentManager().findFragmentByTag("NewCat_Frag");
        nc.addCategory();
    }

    //creates category variables and adds to Category List
    public void createNewCategoryVar(String catName, int monthlyValue, int progressCurr)
    {
        categoryValues.add(new Category(catName, monthlyValue));
    }

    //creates expense variables and adds to Expense List
    public void createNewExpenseVar(String catName, int amount, String notes)
    {
        expenseValues.add(new Expense(catName, amount, notes));

        //find category associated with expense and deduct from current amount
        int index = -1;
        for(int i = 0; i<getCategoryValuesSize(); i++)
        {
            if(getCategoryValuesAtIndex(i).name.equals(catName))
                index = i;
        }

        setCategoryValuesAtIndex(index, amount);

    }

    //hide menu items
    public void hideMenuItems()
    {
        myMenu.findItem(R.id.action_deleteCategory).setVisible(false);
        myMenu.findItem(R.id.action_newCategory).setVisible(false);
        myMenu.findItem(R.id.action_updateCategory).setVisible(false);
    }

    //hide update, delete, and reset items
    public void hideUpdateDeleteItems()
    {
        //recreate the menu where items are hidden
        invalidateOptionsMenu();
    }

    //show menu items
    public void showMenuItems()
    {
        myMenu.findItem(R.id.action_deleteCategory).setVisible(true);
        myMenu.findItem(R.id.action_newCategory).setVisible(true);
        myMenu.findItem(R.id.action_updateCategory).setVisible(true);
        myMenu.findItem(R.id.action_ResetCategories).setVisible(true);
    }

    //transition between DeleteCategoryButtonsFragment to DeleteCategoryFragment
    public void removeCategory()
    {
        DeleteCategoryFragment dc = (DeleteCategoryFragment) getSupportFragmentManager().findFragmentByTag("DeleteCat_Frag");

        dc.removeCategory();
    }

    //removes category from ArrayList
    public void removeCategoryVar(String catName)
    {
        Iterator<Category> itc = categoryValues.iterator();
        while(itc.hasNext())
        {
            Category cat = itc.next();
            if(cat.name.equals(catName))
                itc.remove();
        }

        //remove any expense under category name
        Iterator<Expense> it = expenseValues.iterator();
        while(it.hasNext())
        {
            Expense expense = it.next();
            if(expense.catName.equals(catName))
                it.remove();
        }

    }

    //remove expense from arraylist
    public void removeExpenseVar(int index)
    {
        expenseValues.remove(index);
    }

    //displays the update category fragment
    public void displayUpdateCatagory()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new UpdateCategoryFragment(), "UpdateCat_Frag")
                .commit();

        //display buttons
        getSupportFragmentManager().beginTransaction()
                .add(R.id.buttonContainer, new UpdateCategoryButtonsFragment(), "UpdateCat_Buttons")
                .commit();
    }

    //transition between UpdateCategoryButtonsFragment to UpdateCategoryFragment
    public void updateCategory()
    {
        UpdateCategoryFragment uc = (UpdateCategoryFragment) getSupportFragmentManager().findFragmentByTag("UpdateCat_Frag");
        uc.updateCategory();
    }

    //transition between NewExpenseCategoryButtons and NewExpenseCategory
    public void newExpense()
    {
        NewExpenseFragment ne = (NewExpenseFragment) getSupportFragmentManager().findFragmentByTag("NewExpense_Frag");
        ne.addExpense();
    }

    //returns total budget amount
    public double getTotalBudgetAmount()
    {
        //iterate through category list
        double total = 0;
        for(int i = 0; i<getCategoryValuesSize();i++)
        {
            total += getCategoryValuesAtIndex(i).monthlyValue;
        }

        total /= 100;

        return total;
    }

    //returns total amount spent
    public double getTotalSpentAmount()
    {
        //iterate through category list
        double total = 0;
        for(int i = 0; i<getCategoryValuesSize();i++)
        {
            total += ( getCategoryValuesAtIndex(i).monthlyValue - getCategoryValuesAtIndex(i).currentValue );
        }

        total /= 100;

        return total;
    }

    //returns the total remaining in budget
    public double getTotalRemainingAmount()
    {
        return getTotalBudgetAmount() - getTotalSpentAmount();
    }

    //displays expense details fragment
    public void displayExpenseDetails(String catName)
    {
        //hide fragmentContainer
        ScrollView fc = (ScrollView) findViewById(R.id.fragmentContainerScrollView);
        fc.setVisibility(View.GONE);

        //remove from fragment container
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("BudgetList_Frag"))
                .commit();
        fm.executePendingTransactions();

        //add to expense container
        fm.beginTransaction()
                .add(R.id.expenseContainer, new ExpenseDetailsFragment(), "ExpenseDetails_Frag")
                .commit();
        fm.executePendingTransactions();

        fm.beginTransaction()
                .add(R.id.buttonContainer, new ExpenseDetailsButtonsFragment(), "ExpenseDetails_Buttons")
                .commit();
        fm.executePendingTransactions();

        ExpenseDetailsFragment ed = (ExpenseDetailsFragment) fm.findFragmentByTag("ExpenseDetails_Frag");
        ed.setList(catName);

        hideMenuItems();
    }

    //removes trailing whitespace
    public String removeTrailingWhitespace(String text)
    {
        return text.replaceFirst("\\s+$","");
    }

    //resets all category values to the start of the month
    private void resetCategoryValues()
    {
        //display confirmation box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setTitle("Reset all values?");
        builder.setMessage("Are you sure you wish to proceed? This will reset all category values and erase all expense records.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //iterate through all categories and set values to max
                Iterator<Category> it = categoryValues.iterator();
                while(it.hasNext())
                {
                    Category cat = it.next();
                    cat.currentValue = cat.monthlyValue;
                }

                //remove all expense records
                expenseValues.clear();

                displayBudgetList();
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

    //add value to category
    public void addToCategoryVal(String catName, int value)
    {
        Iterator<Category> it = categoryValues.iterator();
        while(it.hasNext())
        {
            Category cat = it.next();
            if(cat.name.equals(catName))
            {
                cat.currentValue += value;
            }
        }
    }

    //display Splash Screen
    private void displaySplashScreen()
    {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.SplashContainer, new SplashScreenFragment(), "Splash_Frag")
                .commit();
        fm.executePendingTransactions();

        findViewById(R.id.SplashContainer).invalidate();

        //TODO: remove and display budget list
    }

    //remove splash screen and show budgetlist
    public void removeSplashScreen()
    {
        getSupportActionBar().show();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("Splash_Frag"))
                .commit();
        fm.executePendingTransactions();

        displayBudgetList();

    }


}

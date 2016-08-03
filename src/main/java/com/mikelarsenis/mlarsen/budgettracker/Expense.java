package com.mikelarsenis.mlarsen.budgettracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Holds information about an expense: Category Name, Expense Amount, Notes
 */
public class Expense
{
    String catName;
    int amount;
    String notes;
    String date;

    //constructors
    Expense(String categoryName, int expenseAmount)
    {
        catName = categoryName;
        amount = expenseAmount;
        notes = "";

        //set date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        date = df.format(c.getTime());
    }
    Expense(String categoryName, int expenseAmount, String expenseNotes)
    {
        catName = categoryName;
        amount = expenseAmount;
        notes = expenseNotes;

        //set date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        date = df.format(c.getTime());
    }

}

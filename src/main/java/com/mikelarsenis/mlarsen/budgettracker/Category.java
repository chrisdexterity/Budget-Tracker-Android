package com.mikelarsenis.mlarsen.budgettracker;

/**
 * Holds information about a category: Name, Monthly Allotment, Current Value
 */
//category objects that hold names an values
class Category
{
    String name;
    int monthlyValue;
    int currentValue;

    //constructors
    Category(String catName, int catValue)
    {
        name = catName;
        monthlyValue = catValue;
        currentValue = catValue;
    }
    Category(String catName, int catValue, int catCurrValue)
    {
        name = catName;
        monthlyValue = catValue;
        currentValue = catCurrValue;
    }
}

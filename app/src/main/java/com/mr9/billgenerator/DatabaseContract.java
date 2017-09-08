package com.mr9.billgenerator;

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract() {

    }

    /* Inner class that defines the table contents */
    public static class InitialBillTable implements BaseColumns {
        static final String TABLE_NAME = "initialBill";
        static final String COLUMN_NAME_billNo = "billNumber";
        static final String COLUMN_NAME_billQuant = "billQuantity";
        static final String COLUMN_NAME_billRate = "billRate";
    }
}

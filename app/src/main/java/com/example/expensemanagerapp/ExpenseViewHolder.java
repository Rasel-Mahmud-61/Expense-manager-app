package com.example.expensemanagerapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ExpenseViewHolder extends RecyclerView.ViewHolder {

    View mExpenseView;

    public ExpenseViewHolder(View itemView) {
        super(itemView);
        mExpenseView = itemView;

    }

    public void setExpenseType(String type) {
        TextView mtype = mExpenseView.findViewById(R.id.type_expense_ds);
        mtype.setText(type);
    }

    public void setExpenseAmmount(int ammount) {
        TextView mAmmount = mExpenseView.findViewById(R.id.ammoun_expense_ds);
        String strAmmount = String.valueOf(ammount);
        mAmmount.setText(strAmmount);
    }

    public void setExpenseDate(String date) {
        TextView mDate = mExpenseView.findViewById(R.id.date_expense_ds);
        mDate.setText(date);
    }

}

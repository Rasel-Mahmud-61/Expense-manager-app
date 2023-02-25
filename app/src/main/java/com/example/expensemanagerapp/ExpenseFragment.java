package com.example.expensemanagerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensemanagerapp.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpenseFragment() {
        // Required empty public constructor
    }
 // firebase database
    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;
  private  RecyclerView recyclerView ;
    private  TextView expenseSumResult;



    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          View myview= inflater.inflate(R.layout.fragment_expense, container, false);

            mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();
        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);
         expenseSumResult =myview.findViewById(R.id.expense_txt_result);
        recyclerView =myview.findViewById(R.id.recycler_id_expense);

        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getActivity());
                layoutManager.setStackFromEnd(true);
                layoutManager.setReverseLayout(true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                mExpenseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int expenseSum =0;
                            for(DataSnapshot mysanapshot :dataSnapshot.getchildren()){
                                Data data =mysanapshot.getValue(Data.class);
                                expenseSum+=data.getAmount();
                                String strExpensesum=String.valueOf(expenseSum);
                                expenseSumResult.setText(strExpensesum);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

          return myview;
    }
    public  void onStart() {
  super.onStart();
        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDatabase, Data.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Data, IncomeFragment.MyViewHolder>(options) {


            protected void onBindViewHolder(IncomeFragment.MyViewHolder holder, int position, @NonNull Data model) {
                holder.setAmmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
            }
        };
        recyclerView.setAdapter(adapter);
    }
    private  static class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MyViewHolder(view itemView){
            super(itemView);
            mView=itemView;

        }
        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }
        private  void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }
        private void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }
        private  void setAmmount(int ammount){
            TextView mAAmmount=mView.findViewById(R.id.ammount_txt_expense);
            String strammount =String.valueOf(ammount);
            mAAmmount.setText(strammount);

        }

    }
}
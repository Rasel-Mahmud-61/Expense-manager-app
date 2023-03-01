package com.example.expensemanagerapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanagerapp.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;
    private boolean isOpen=false;
    private Animation FadOpen,FadeClose;

    //dashboard income and expense data
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;


    //Firebase...

    private FirebaseAuth mAuth;
  private DatabaseReference mIncomeDatabase;
  private DatabaseReference mExpenseDatabase;




        // floating button textview

    private TextView fab_income_txt;
    private TextView fab_expense_txt;
    private String mDate;
// recycler view
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;


    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        View myview= inflater.inflate(R.layout.fragment_dash_board, container, false);


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser =mAuth.getCurrentUser();
        String uid=mUser.getUid();
        mIncomeDatabase=FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

       mExpenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        //connect floating button
        fab_main_btn=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_Ft_btn);
        fab_expense_btn=myview.findViewById(R.id.expense_ft_btn);
        //connect floatoing text
        fab_income_txt=myview.findViewById(R.id.expense_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);
        // total income and expense data
        totalIncomeResult =myview.findViewById(R.id.income_set_result);
        totalExpenseResult =myview.findViewById(R.id.expense_set_result);
        //recyclerview
        mRecyclerIncome= myview.findViewById(R.id.recycler_income);

        mRecyclerExpense-myview.findViewById(R.id.recycler_expense);


        //conect animation
        FadOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                if(isOpen){
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);
                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen=false;

                }else{
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen=true;
                }
            }
        });

        //calculate total income
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum=0;
                    for(DataSnapshot mysnap:snapshot.getChildren()){
                        Data data =mysnap.getValue(Data.class);
                        totalsum+=data.getAmount();
                        String stResult=String.valueOf(totalsum);
                        totalIncomeResult.setText(stResult+".00");


                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // calculate total expense data
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum=0;
                for(DataSnapshot mysnapshot:snapshot.getChildren() ){
                    Data data =mysnapshot.getValue(Data.class);
                    totalsum+=data.getAmount();
                    String strTotalSum=String.valueOf(totalsum);
                    totalExpenseResult.setText(strTotalSum+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //recycler
        LinearLayoutManager layoutManagerIncome=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense =new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);


        return myview;
    }
    private  void ftAnimation(){

        if(isOpen){
            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);
            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen=false;

        }else{
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen=true;
        }
    }
    private void addData(){
        //fab Button income
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            incomeDataInsert();
            }
        });
        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }

    public  void incomeDataInsert(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =LayoutInflater.from(getActivity());
         View myviewm =inflater.inflate(R.layout.custom_layout_for_insertdata,null);
         mydialog.setView(myviewm);
       final   AlertDialog dialog =mydialog.create();
        dialog.setCancelable(false);
      final   EditText edtAmmount =myviewm.findViewById(R.id.amount_edt);
       final EditText edtType =myviewm.findViewById(R.id.type_edt);
      final   EditText edtNote =myviewm.findViewById(R.id.note_edt);

        Button btnSave =myviewm.findViewById(R.id.btnSave);
        Button btnCansel =myviewm.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String type =edtType.getText().toString().trim();
               String ammount=edtAmmount.getText().toString().trim();
               String note=edtNote.getText().toString().trim();

               if(TextUtils.isEmpty(type)){
                   edtType.setError("Required Field...");
                   return;
               }

                if(TextUtils.isEmpty(ammount)){
                    edtAmmount.setError("Required Field...");
                    return;
                }

                int ourammontint=Integer.parseInt(ammount);

                if(TextUtils.isEmpty(note)){
                    edtNote.setError("Required Field...");
                    return;
                }
                String id =mIncomeDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());

                Data data  =new Data(ourammontint,type,note,id,mDate);
                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data ADDED",Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();


            }
        });


        btnCansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public void expenseDataInsert(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =LayoutInflater.from(getActivity());
         View myview =inflater.inflate(R.layout.custom_layout_for_insertdata,null);
         mydialog.setView(myview);
        mydialog.setCancelable(false);
        final  AlertDialog dialog =mydialog.create();
      final   EditText amount =myview.findViewById(R.id.amount_edt);
     final    EditText type =myview.findViewById(R.id.type_edt);
      final   EditText note =myview.findViewById(R.id.note_edt);

         Button btnSave=myview.findViewById(R.id.btnSave);
         Button btnCansel =myview.findViewById(R.id.btnCancel);
         btnSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                  String tmAmount =amount.getText().toString().trim();
                  String tmtype =type.getText().toString().trim();
                  String tmnote =note.getText().toString().trim();
                if(TextUtils.isEmpty(tmAmount)){
                    amount.setError("Required Field..");
                    return;
                }
                int inamount =Integer.parseInt(tmAmount);
                if(TextUtils.isEmpty(tmtype)){
                    type.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(tmnote)){
                    note.setError("Required Field..");
                    return;
                }
                String id=mExpenseDatabase.getKey();
                 String mDate= DateFormat.getDateInstance().format(new Date());

                 Data data =new Data(inamount,tmtype,tmnote,id,mDate);
                mExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data  added",Toast.LENGTH_SHORT).show();


                ftAnimation();
                dialog.dismiss();
             }
         });
         btnCansel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ftAnimation();
                 dialog.dismiss();
             }
         });
         dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,IncomeViewHolder>IncomeAdapter=new FirebaseRecyclerAdapter<Data,IncomeViewHolder>
                (
                    Data.class,
                    R.layout.dashboard_income,
                    DashBoardFragment.IncomeViewHolder.class,
                    mIncomeDatabase
                ){
             @Override
            protected void populateViewHolder(IncomeViewHolder viewHolder,Data model,int position){
                 viewHolder.setIncomeType(model.getType());
                 viewHolder.setIncomeAmmount(model.getAmount());
                 viewHolder.setIncomeDate(model.getDate());
             }
        };

        mRecyclerIncome.setAdapter(incomeAdapter);
    }

    //For Income Data

    public static class IncomeViewHolder extends RecyclerView.ViewHolder{
        View mIncomeView;
        public  IncomeViewHolder(View itemView){
            super(itemView);
            mIncomeView=itemView;
        }

        public void setIncomeType(String type){
            TextView mtype=mIncomeView.findViewById(R.id.type_Income_ds);
            mtype.setText(type);
        }
        public void setIncomeAmmount(int Ammount){
            TextView mAmmount =mIncomeView.findViewById(R.id.ammoun_income_ds);
            String strAmmount =String.valueOf(Ammount);
            mAmmount.setText(strAmmount);
        }

        public void setIncomeDate(String date){
            TextView mDate=mIncomeView.findViewById(R.id.date_income_ds);
            mDate.setText(date);

        }


    }
}
package com.example.expensemanagerapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expensemanagerapp.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment<FirebaseRecyclerOptions> extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //firebase database
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase ;

// recycler view
    private RecyclerView recyclerView ;
    //text view
    private  TextView incometotalSum;

    // update edit text
    private EditText edtAmmount;
    private EditText edtType;
    private EditText edtNote;
    // button for update and delete
    private Button btnUpdate;
    private  Button btnDelete;

    public IncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
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
         View myview= inflater.inflate(R.layout.fragment_income, container, false);
         mAuth =FirebaseAuth.getInstance();
        FirebaseUser mUser =mAuth.getCurrentUser();
        String uid= mUser.getUid();
        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        incometotalSum=myview.findViewById(R.id.income_txt_result);
recyclerView=myview.findViewById(R.id.recycler_id_income);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

       RecyclerView.LayoutManager.setReverseLayout(true);
       layoutManager.setStackFromEnd(true);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(layoutManager);
       
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totlatvalue =0;
                for(DataSnapshot mysanapshort: dataSnapshot.getChildren()){
                    Data data =mysanapshort.getValue(Data.class);
                    totlatvalue+=data.getAmount();
                    String StTotalvale=String.valueOf(totlatvalue);
                        incometotalSum.setText(StTotalvale);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        


         return myview;
    }
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDatabase, Data.class)
                .build();

      adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {


            protected void onBindViewHolder(MyViewHolder holder, int position, @NonNull Data model) {
                holder.setAmmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDataItem();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public  static class MyViewHolder extends  RecyclerView.ViewHolder {
        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setType(String type) {
            TextView mType = mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private void setNote(String note){
            TextView mNote =mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private  void  setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_income);

        }
        private void  setAmmount(int ammount){
            TextView mAmmount =mView.findViewById(R.id.ammount_txt_income);
            String stammount =String.valueOf(ammount);
            mAmmount.setText(stammount);

        }
    }
    private  void updateDataItem(){
        AlertDialog.Builder mydialog =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =LayoutInflater.from(getActivity());
        View myview =inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);
        edtAmmount=myview.findViewById(R.id.amount_edt);
        edtType=myview.findViewById(R.id.type_edt);
        edtNote=myview.findViewById(R.id.note_edt);
        btnUpdate =myview.findViewById(R.id.btn_upd_Update);
        btnDelete=myview.findViewById(R.id.btnuPD_Delete);
        AlertDialog dialog =mydialog.create();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
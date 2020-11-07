package com.example.expenseapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expenseapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class ExpenseFragment extends Fragment {


    //Firebase database..

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;
    private FirebaseRecyclerAdapter adapter;

    //Recyclerview..

    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        recyclerView=myview.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        return  myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpenseDatabase, Data.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Data, com.example.expenseapp.MyViewHolder>(options) {

            public com.example.expenseapp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new com.example.expenseapp.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data, parent, false));
            }

            protected void onBindViewHolder(com.example.expenseapp.MyViewHolder holder, int position, @NonNull Data model) {
                holder.setAmmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }

        void setAmmount(int ammount){
            TextView mAAmmount=mView.findViewById(R.id.ammount_txt_expense);

            String strammount=String.valueOf(ammount);

            mAAmmount.setText(strammount);

        }

    }
}

package com.example.expenseapp;

import android.app.AlertDialog;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import com.example.expenseapp.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    //floating button
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //floating button textview...

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    //boolean
    private boolean isOpen = false;


    //Animation.
    private Animation FadeOpen, FadeClose;

    //firebase..

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_dash_board, container, false);

        //Connect Floating button to layout

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        mIncomeDatabase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);

        //connect floating

        fab_main_btn = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myview.findViewById(R.id.income_Ft_btn);
        fab_expense_btn = myview.findViewById(R.id.expense_Ft_btn);

        //connect floating text.

        fab_income_txt = myview.findViewById(R.id.income_ft_text);
        fab_expense_txt = myview.findViewById(R.id.expense_ft_text);

        //Animation Connection..
        FadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                if (isOpen) {
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen = false;
                } else {
                    fab_income_btn.startAnimation(FadeOpen);
                    fab_expense_btn.startAnimation(FadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadeOpen);
                    fab_expense_txt.startAnimation(FadeOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen = true;

                }
            }
        });

        return myview;
    }

    //Floating button animation

    private void ftAnimation(){
        if (isOpen){

            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen=false;

        }else {
            fab_income_btn.startAnimation(FadeOpen);
            fab_expense_btn.startAnimation(FadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadeOpen);
            fab_expense_txt.startAnimation(FadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen=true;

        }

    }



    private void addData() {

        //Fab Button income..

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

    public void incomeDataInsert() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myviewm);

        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        final EditText edtAmmount = myviewm.findViewById(R.id.ammount_edt);
        final EditText edtType = myviewm.findViewById(R.id.type_edt);
        final EditText edtNote = myviewm.findViewById(R.id.note_edt);

        Button btnSave = myviewm.findViewById(R.id.btnSave);
        Button btnCancel = myviewm.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = edtType.getText().toString().trim();
                String amount = edtAmmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(type)) {
                    edtType.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(amount)) {
                    edtAmmount.setError("Required Field..");
                    return;
                }

                int ourammontint = Integer.parseInt(amount);

                if (TextUtils.isEmpty(note)) {
                    edtNote.setError("Required Field..");
                    return;
                }

                String id=mIncomeDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(ourammontint,type,note,id,mDate);
                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data ADDED",Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();



            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void expenseDataInsert(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);

        final AlertDialog dialog=mydialog.create();

        dialog.setCancelable(false);

        final EditText ammount=myview.findViewById(R.id.ammount_edt);
        final EditText type=myview.findViewById(R.id.type_edt);
        final EditText note=myview.findViewById(R.id.note_edt);

        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCansel=myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tmAmmount=ammount.getText().toString().trim();
                String tmtype=type.getText().toString().trim();
                String tmnote=note.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmmount)){
                    ammount.setError("Required Field..");
                    return;
                }

                int inamount=Integer.parseInt(tmAmmount);

                if (TextUtils.isEmpty(tmtype)){
                    type.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(tmnote)){
                    note.setError("Required Field..");
                    return;
                }


                String id=mExpenseDatabase.push().getKey();
                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(inamount,tmtype,tmnote,id,mDate);
                mExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data added",Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();
            }
        });


        btnCansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
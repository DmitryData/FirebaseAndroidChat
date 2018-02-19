package com.firebase_info.firebaseandroidchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mMessageListView;
    private EditText mEditText;
    private Button mSendButton;
    private MessageAdapter mMessageAdapter;

    private FirebaseDatabase mFirewbaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        mFirewbaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirewbaseDatabase.getReference().child("Messages");

        if (mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    mMessageAdapter.add(message);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mMessageDatabaseReference.addChildEventListener(mChildEventListener);

            List<Message> messages = new ArrayList<>();

            mMessageAdapter = new MessageAdapter(this,R.layout.item_message,messages);
            mMessageListView.setAdapter(mMessageAdapter);

            mSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message message = new Message(mEditText.getText().toString());
                    mMessageDatabaseReference.push().setValue(message);
                    mEditText.setText("");



                 }
            });

            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(charSequence.toString().trim().length()>0){
                        mSendButton.setEnabled(true);
                    }else{
                        mSendButton.setEnabled(false);
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }


    }

    @Override
    protected void onDestroy() {
        if(mChildEventListener != null){
            mMessageDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
            super.onDestroy();
        }

    }
}
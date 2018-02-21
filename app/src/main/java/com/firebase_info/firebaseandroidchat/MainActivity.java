package com.firebase_info.firebaseandroidchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
    // 1. Пользовательский интерфейс (UI)
    private ListView mMessageListView;
    private EditText mEditText;
    private Button mSendButton;
    private MessageAdapter mMessageAdapter;
    // 2. Firebase
    private FirebaseDatabase mFirewbaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;

    //создаем меню для кнопки в шапке
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
            menu.findItem(R.id.menu_refresh).setVisible(true);
        return true;
    }
    //listener для кнопки в шапке
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                //удаляем всю информацию с сервера и очищаем адаптер
                mMessageDatabaseReference.removeValue();
                mMessageAdapter.clear();
                mMessageAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 3. Связываем UI
        setTitle("SLyne" );
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        if (Main2Activity.name == null) {
            Intent intent1 = new Intent(this, Main2Activity.class);
            startActivity(intent1);
        }
        // 4. Инициализируем Firebase
        mFirewbaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirewbaseDatabase.getReference().child("Messages");
        // 5. Создаем слушатель базы данных
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
            // 6. Устанавливаем слушатель базы данных
            mMessageDatabaseReference.addChildEventListener(mChildEventListener);
            // 7. Создаем лист где будем хранить сообщения
            List<Message> messages = new ArrayList<>();
            // 8. Создаем и устанавливаем Адаптер для сообщений
            mMessageAdapter = new MessageAdapter(this,R.layout.item_message,messages);
            mMessageListView.setAdapter(mMessageAdapter);
            // 9. Устанавливаем слушатель клика на кнопку, создаем сообщение, отправляем сообщение в базу, удаляем текст
            mSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message message = new Message(Main2Activity.name + " |  " +mEditText.getText().toString());
                    mMessageDatabaseReference.push().setValue(message);
                    mEditText.setText("");
                 }
            });
            // * устанавливаем слушатель, который срабатывает при вводе текста
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
    // 10. Удаляем слушатель базы данных, в целях избежания утечки памяти
    @Override
    protected void onDestroy() {
        if(mChildEventListener != null){
            mMessageDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
            super.onDestroy();
        }

    }
}

package com.firebase_info.firebaseandroidchat;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {

    EditText eText2;
    FloatingActionButton eButton2;
    static String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("SLyne" );

        eText2 = (EditText) findViewById(R.id.editText2);
        eButton2 = (FloatingActionButton) findViewById(R.id.sendButton2);
    }

    public void choise(View view) {
       name =  eText2.getText().toString();
        Intent intent2 = new Intent (this, MainActivity.class);
        startActivity(intent2);
    }
}

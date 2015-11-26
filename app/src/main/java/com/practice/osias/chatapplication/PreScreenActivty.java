package com.practice.osias.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreScreenActivty extends AppCompatActivity {
    @Bind(R.id.usernameTextBox)
    EditText username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_screen_activty);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.connectButton)
    public void goToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        Bundle bundle = intent.getExtras();
        if(bundle==null)
            bundle = new Bundle();
        bundle.putString("username",username.getText().toString());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
}

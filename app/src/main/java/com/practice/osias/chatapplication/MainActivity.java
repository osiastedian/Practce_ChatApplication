package com.practice.osias.chatapplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.net.URISyntaxException;
import java.util.TooManyListenersException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public String serverLocation = "http://192.168.1.6:3000";
    @Bind(R.id.username)
    TextView usernameText;
    @Bind(R.id.messageBox)
    EditText messageBox;
    @Bind(R.id.chatList)
    ListView chatList;
    Socket socket;
    {
        try {
            IO.Options option = new IO.Options();
            option.port = 3000;
            option.host = "192.168.1.6";
            socket = IO.socket(serverLocation,option);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String username = this.getIntent().getExtras().getString("username");
        if(username == null)
            username = "Guest";
        usernameText.setText(username);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String[0]);
        chatList.setAdapter(arrayAdapter);
        socket.on("new message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject data = new JSONObject(args[0].toString());
                    addMessageToList(data.getString("sender"), data.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        socket.connect();
        if(socket.connected())
            Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.sendButton)
    public void sendMessage(){
        if(socket!=null && socket.connected()){
            JSONObject json = new JSONObject();
            try {
                json.put("sender",this.usernameText.getText().toString());
                json.put("message",this.messageBox.getText().toString());
                socket.emit("chat message",json.toString());
                messageBox.setText("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMessageToList(String sender,String message){

        ((ArrayAdapter<String>) chatList.getAdapter()).add(sender+": "+message);
        // TODO: Add message to chatlist
    }
}

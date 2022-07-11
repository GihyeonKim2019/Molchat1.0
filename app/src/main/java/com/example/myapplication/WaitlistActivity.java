package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.net.URISyntaxException;
import java.util.List;
import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;

public class WaitlistActivity extends AppCompatActivity {
    private Socket mSocket;
    private Gson gson = new Gson();
    private List<String> realWaitlist;
    private TextView waitlist_text;
    private String myId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitlist);
        waitlist_text = (TextView)findViewById(R.id.waitlist_text);
        try {
            mSocket = IO.socket("https://e6f5-143-248-168-57.jp.ngrok.io");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        Toast.makeText(this,"connected"+mSocket.id(), Toast.LENGTH_SHORT).show();
        mSocket.on(Socket.EVENT_CONNECT, args ->{
            mSocket.emit("WAITLIST_ENTER", ((GlobalId)getApplication()).getGlobalId());
        });
        mSocket.on("MATCHING", args -> {
            MatchingData data = gson.fromJson(args[0].toString(), MatchingData.class);
            myId = ((GlobalId)getApplication()).getGlobalId();
            if (data.getUserid().equals(myId) ) {
                matchingSuccess(data.getRoomnumber(), data.getWaiterid());
            }else if (data.getWaiterid().equals(myId)) {
                matchingSuccess(data.getRoomnumber(), data.getUserid());
            }
            else {
                matchingSuccess(myId, data.getUserid());
            }
        });
    }

    private void matchingSuccess(String roomnumber, String opponent) {
        runOnUiThread(() -> {
            waitlist_text.setText("matchingsuccess");
            Intent intent = new Intent(this,MatchingRoomActivity.class);
            intent.putExtra("roomNumber",roomnumber);
            intent.putExtra("username",myId);
            intent.putExtra("opponent",opponent);
            startActivity(intent);
            mSocket.disconnect();
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("WAITLIST_LEFT", ((GlobalId)getApplication()).getGlobalId());
        mSocket.disconnect();
    }



}

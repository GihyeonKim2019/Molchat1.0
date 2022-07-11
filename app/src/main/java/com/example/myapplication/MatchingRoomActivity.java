package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityMatchingroomBinding;
import com.google.gson.Gson;


public class MatchingRoomActivity extends AppCompatActivity {
    private String roomNumber;
    private String username;
    private String opponent;
    private ActivityMatchingroomBinding binding;
    private Socket mSocket;
    private RetrofitClient retrofitClient;
    private ChatAdapter adapter;
    private Gson gson = new Gson();
    private final int SELECT_IMAGE = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchingroomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        try {
            mSocket = IO.socket("https://e6f5-143-248-168-57.jp.ngrok.io");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        retrofitClient = RetrofitClient.getInstance();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        roomNumber = intent.getStringExtra("roomNumber");
        opponent = intent.getStringExtra("opponent");

        adapter = new ChatAdapter(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
        binding.addbutton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("상대방에게 친구 요청을 보내시겠습니까?");
            builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {return;}
            });
            builder.setNegativeButton("친구 요청 보내기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mSocket.emit("ADD_REQUEST",username,opponent,roomNumber);
                }
            });
            builder.show();
        });
        binding.backbutton.setOnClickListener(v -> onBackPressed());
        // 메시지 전송 버튼
        binding.sendBtn.setOnClickListener(v -> sendMessage());
        // 이미지 전송 버튼
        binding.imageBtn.setOnClickListener(v -> {
            //권한 얻기
            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permission2 = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                }
                return;
            }

            Intent imageIntent = new Intent(Intent.ACTION_PICK);
            imageIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(imageIntent, SELECT_IMAGE);
        });

        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT, args -> {
            mSocket.emit("enter", gson.toJson(new RoomData(username, roomNumber)));
        });
        mSocket.on("update", args -> {
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            wearegettingupdates();
            addChat(data);
        });
        mSocket.on("ADD_REQUEST", (args) -> {
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            wearegettingupdates();
            addChat(data);
        });
    }
    // 리사이클러뷰에 채팅 추가
    private void addChat(MessageData data) {
        runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else if (data.getType().equals("IMAGE")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_IMAGE));
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else if (data.getType().equals("ADD_SUCCESS")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
            }
            else if (data.getType().equals("ADD_REQUEST")) {
                if (data.getFrom().equals(username)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("상대방으로부터 친구 요청이 도착했습니다.");
                    builder.setMessage("수락하시겠습니까?");
                    builder.setPositiveButton("거절", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {return;}
                    });
                    builder.setNegativeButton("수락", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mSocket.emit("ADD_ACCEPT",username,opponent,roomNumber);
                        }
                    });
                    builder.show();
                }
            } else {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
                binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void wearegettingupdates(){
        runOnUiThread(()-> {
            Toast.makeText(this, "updated",Toast.LENGTH_SHORT).show();
        });
    }

    private void sendMessage() {
        mSocket.emit("newMessage", gson.toJson(new MessageData("MESSAGE",
                username,
                roomNumber,
                binding.contentEdit.getText().toString(),
                System.currentTimeMillis())));
        Log.d("MESSAGE", new MessageData("MESSAGE",
                username,
                roomNumber,
                binding.contentEdit.getText().toString(),
                System.currentTimeMillis()).toString());
        adapter.addItem(new ChatItem(username, binding.contentEdit.getText().toString(), toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE));
        binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        binding.contentEdit.setText("");
    }
    // 이미지 uri로부터 실제 파일 경로를 알아냄
    private String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();

        return result;
    }

    public void uploadImage(Uri imageUri, Context context) {
        File image = new File(getRealPathFromURI(imageUri, context));
        RequestBody requestBody = RequestBody.create(image, MediaType.parse("image/*"));

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestBody);

        retrofitClient.getApiService().uploadImage(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result.getResult() == 1) {
                    Log.d("PHOTO", "Upload success : " + result.getImageUri());
                    sendImage(result.getImageUri());

                } else {
                    Log.d("PHOTO", "Upload failed");
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("PHOTO", "Upload failed : " + t.getMessage());
            }
        });
    }

    private void sendImage(String imageUri) {
        mSocket.emit("newImage", gson.toJson(new MessageData("IMAGE",
                username,
                roomNumber,
                imageUri,
                System.currentTimeMillis())));


        Log.d("IMAGE", new MessageData("IMAGE",
                username,
                roomNumber,
                imageUri,
                System.currentTimeMillis()).toString());
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMiliis));
    }

    // 이미지를 갤러리에서 선택했을 때 이벤트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            uploadImage(selectedImageUri, getApplicationContext());
            adapter.addItem(new ChatItem(username, String.valueOf(selectedImageUri), toDate(System.currentTimeMillis()), ChatType.RIGHT_IMAGE));
            binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("left", gson.toJson(new RoomData(username, roomNumber)));
        mSocket.disconnect();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("채팅방을 나가시겠습니까? 상대방과 연결이 끊어집니다.");
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.setNegativeButton("나가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }



}

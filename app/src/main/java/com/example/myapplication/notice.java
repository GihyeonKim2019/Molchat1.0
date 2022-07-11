package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class notice extends AppCompatActivity {

    private final String TAG = "noticeLog";
    //주의 변경
    private final String URL =  "https://063a-192-249-18-215.jp.ngrok.io";

    private Retrofit retrofit;
    com.example.myapplication.ApiService service;

    private TextView notice_name, notice_age, notice_character;
    private EditText notice_chat;
    private ImageView imageView;
    private CheckBox checkBox;
    private Button send;


    private String Userid;
    private String Userid_friend;
    private boolean check = true;
    private Bitmap profile_image;
    private Bitmap notice_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Userid = ((GlobalId)getApplication()).getGlobalId();
        // 수정 필요
        Userid_friend = ((GlobalId)getApplication()).getGlobalId();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(com.example.myapplication.ApiService.class);

        notice_name = findViewById(R.id.notice_name0);
        notice_age = findViewById(R.id.notice_age0);
        notice_character = findViewById(R.id.notice_character0);
        notice_chat = findViewById(R.id.notice_chat);
        imageView = findViewById(R.id.imageView);
        checkBox = findViewById(R.id.checkBox);
        send = findViewById(R.id.send);

        Call<Post> profile = service.get_profile_data(Userid_friend);
        profile.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                String Name = response.body().getName();
                notice_name.setText(Name);
                /*
                String Age = response.body().getAge();
                notice_age.setText(Age);
                String Character = response.body().getCharacter();
                notice_character.setText(Character);
                 */
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 접속에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        DownloadTest(service);

        Call<List<JsonObject>> notice = service.noticedownload(Userid_friend);
        notice.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> result = response.body();
                List<Bitmap> bitmapList = new List<Bitmap>() {
                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean contains(@Nullable Object o) {
                        return false;
                    }

                    @NonNull
                    @Override
                    public Iterator<Bitmap> iterator() {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Object[] toArray() {
                        return new Object[0];
                    }

                    @NonNull
                    @Override
                    public <T> T[] toArray(@NonNull T[] ts) {
                        return null;
                    }

                    @Override
                    public boolean add(Bitmap bitmap) {
                        return false;
                    }

                    @Override
                    public boolean remove(@Nullable Object o) {
                        return false;
                    }

                    @Override
                    public boolean containsAll(@NonNull Collection<?> collection) {
                        return false;
                    }

                    @Override
                    public boolean addAll(@NonNull Collection<? extends Bitmap> collection) {
                        return false;
                    }

                    @Override
                    public boolean addAll(int i, @NonNull Collection<? extends Bitmap> collection) {
                        return false;
                    }

                    @Override
                    public boolean removeAll(@NonNull Collection<?> collection) {
                        return false;
                    }

                    @Override
                    public boolean retainAll(@NonNull Collection<?> collection) {
                        return false;
                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public Bitmap get(int i) {
                        return null;
                    }

                    @Override
                    public Bitmap set(int i, Bitmap bitmap) {
                        return null;
                    }

                    @Override
                    public void add(int i, Bitmap bitmap) {

                    }

                    @Override
                    public Bitmap remove(int i) {
                        return null;
                    }

                    @Override
                    public int indexOf(@Nullable Object o) {
                        return 0;
                    }

                    @Override
                    public int lastIndexOf(@Nullable Object o) {
                        return 0;
                    }

                    @NonNull
                    @Override
                    public ListIterator<Bitmap> listIterator() {
                        return null;
                    }

                    @NonNull
                    @Override
                    public ListIterator<Bitmap> listIterator(int i) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public List<Bitmap> subList(int i, int i1) {
                        return null;
                    }
                };

                int size = result.size();
                for(int i = 0; i < size; i++){
                    DownloadNoticeTest(service, result.get(i).get("Image").toString());
                    bitmapList.add(notice_image);
                }
            }
            @Override
            public void onFailure(Call<List<JsonObject>>call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 접속에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = !check;
            }
        });

        send.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = notice_chat.getText().toString();
                if(content.equals("")){

                }
                else{
                    if(check){
                        Call<ResponseBody> secret = service.give_noticeboard_secret_content(content, Userid);
                        secret.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){

                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                    else{
                        Call<ResponseBody> secret = service.give_noticeboard_content(content, Userid, Userid_friend);
                        secret.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){

                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        }) ;
    }

    private void DownloadTest(com.example.myapplication.ApiService service){
        Call<ResponseBody> call = service.get_profile_image(Userid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                Log.v(TAG, "다운로드 1");
                InputStream inputStream = response.body().byteStream();
                Log.v(TAG, "다운로드 2");
                profile_image = BitmapFactory.decodeStream(inputStream);
                Log.v(TAG, "다운로드 3");
                imageView.setImageBitmap(profile_image);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void DownloadNoticeTest(com.example.myapplication.ApiService service, String image){
        Call<ResponseBody> call = service.get_noticeboard_image(image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                InputStream inputStream = response.body().byteStream();
                notice_image = BitmapFactory.decodeStream(inputStream);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
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
    private File imagefile;
    private Bitmap profile_image;
    Uri imageUri;
    private boolean check = true;
    private String content = "";
    private String confirm = "";
    private Bitmap notice_image;

    private final String TAG = "noticeLog";
    private final String URL =  "https://063a-192-249-18-215.jp.ngrok.io";
    private Retrofit retrofit;
    com.example.myapplication.ApiService service;

    private TextView notice_name, notice_age, notice_character;
    private EditText notice_chat;
    private ImageView imageView;
    private CheckBox checkBox;
    private Button send;
    //private ListView lsitview;

    private int ID = 1;
    //ID 본인 아이디. ID2 친구/본인 아이디
    private int ID2 = 1;
    private int ID_notice;
    private String userid = "g1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

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

        Call<Post> profile = service.get_profile_data(ID);
        profile.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.v(TAG, "프로필 데이터를 받았습니다.");

                ID_notice = response.body().getID();

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
                Log.v(TAG, "프로필 접속에 실패하였습니다.");
                Toast.makeText(getApplicationContext(), "서버 접속에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        DownloadTest(service);


        Call<List<JsonObject>> notice = service.noticedownload(userid);
        notice.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                Log.v(TAG, "이하이");
                List<JsonObject> result = response.body();
                Log.v(TAG, "이하이1");
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
                Log.v(TAG, "이하이2");
                Log.v(TAG, response.body().get(0).get("Image").toString());

                int size = result.size();
                for(int i = 0; i < size; i++){
                    Log.v(TAG, "이하이"+i);
                    DownloadNoticeTest(service, result.get(i).get("Image").toString());
                    bitmapList.add(notice_image);
                }


            }
            @Override
            public void onFailure(Call<List<JsonObject>>call, Throwable t) {
                Log.v(TAG, "프로필 접속에 실패하였습니다.");
                Toast.makeText(getApplicationContext(), "서버 접속에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
//


        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
                check = !check;
            }
        }) ;

        send.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
                content = notice_chat.getText().toString();
                if(content.equals(confirm)){

                }
                else{
                    if(ID != ID_notice) {
                        Toast.makeText(getApplicationContext(), "스스로에게 게시글을 달 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //익영 친구의 user id에 대한 구별 필요
                        if(check){
                            Log.v(TAG, "오선무3");
                            Call<ResponseBody> secret = service.give_noticeboard_secret_content(content, userid);
                            secret.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){

                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                            Log.v(TAG, "오선무4");
                        }
                        //1번번 user id에 대한 구별 필요 뒤에 userid가 자기 앞에가 평가 대상
                        else{
                            Log.v(TAG, "오선무1");
                            Call<ResponseBody> secret = service.give_noticeboard_content(content, userid, userid);
                            secret.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){

                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                            Log.v(TAG, "오선무2");
                        }

                    }
                        //make list view
                }
            }
        }) ;

    }

    private void DownloadTest(com.example.myapplication.ApiService service){
        Log.v(TAG, "다운로드 0");
        Call<ResponseBody> call = service.get_profile_image(ID);
        Log.v(TAG, "다운로드");
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
        Log.v(TAG, "다운로드");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                Log.v(TAG, "다운로드 1");
                InputStream inputStream = response.body().byteStream();
                Log.v(TAG, "다운로드 2");
                notice_image = BitmapFactory.decodeStream(inputStream);
                Log.v(TAG, "다운로드 3");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}

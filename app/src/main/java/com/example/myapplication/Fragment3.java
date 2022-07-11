package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment3 extends Fragment {
    public static Fragment3 newInstance(int number) {
        Fragment3 fragment3 = new Fragment3();
        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        fragment3.setArguments(bundle);
        return fragment3;
    }


    private String Userid;


    //주의
    private final String URL =  "https://063a-192-249-18-215.jp.ngrok.io";

    private TextView name, age, character, mbti, introduce;
    private Button notice_button, evaluation_button;
    private ImageView image;

    private Retrofit retrofit;
    com.example.myapplication.ApiService service;

    private File imagefile;
    private Bitmap profile_image;
    Uri imageUri;
    private static final int REQUEST_CODE = 1;


    private Context mcontext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile, container, false);

        Userid = ((GlobalId)getActivity().getApplication()).getGlobalId();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(com.example.myapplication.ApiService.class);

        name = (TextView)view.findViewById(R.id.name);
        age = (TextView)view.findViewById(R.id.age);
        character = (TextView)view.findViewById(R.id.chacracter);
        mbti = (TextView)view.findViewById(R.id.mbti);
        introduce = (TextView)view.findViewById(R.id.indtroduce);

        image = (ImageView) view.findViewById(R.id.image);
        notice_button = (Button) view.findViewById(R.id.notice);
        evaluation_button = (Button) view.findViewById(R.id.evaluation);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        notice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), notice.class );
                startActivity( intent );
            }
        });

        evaluation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), evaluation.class );
                startActivity( intent );
            }
        });

        Call<Post> profile = service.get_profile_data(Userid);
        profile.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                String Name = response.body().getName();
                name.setText(Name);
                /*
                int ID1 = response.body().getID();

                String Age = response.body().getAge();
                name.setText(Age);
                String Character = response.body().getCharacter();
                name.setText(Character);
                String Mbti = response.body().getMbti();
                name.setText(Mbti);
                String Introduce = response.body().getIntroduce();
                name.setText(Introduce);
                 */
    }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "서버 접속에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        Call<ResponseBody> call = service.get_profile_image(Userid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                InputStream inputStream = response.body().byteStream();
                profile_image = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(profile_image);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return view;
    }
}
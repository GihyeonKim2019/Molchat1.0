package com.example.myapplication;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/retrofit/login0/")
    Call<com.example.myapplication.Post> giveid(@Query("Userid") String Userid);
    // Userid를 보내고 | 똑같은 Userid가 db에 있으면 Userid를 없으면 response 실패.

    @GET("/retrofit/login1/")
    Call<com.example.myapplication.Post> giveall(@Query("Name") String Name, @Query("Userid") String Userid, @Query("Password") String Password);
    // Name, Userid, Password를 보내고 | 이를 db에 추가한다.

    @GET("/retrofit/login2/")
    Call<com.example.myapplication.Post> givelogin(@Query("Userid") String Userid, @Query("Password") String Password);
    // Userid, Password를 보내고 | db에 둘다 있으면 row를 보내고 없으면 response 실패.

    @GET("/retrofit/login3/")
    Call<Post> giveid1(@Query("Userid") String Userid);
    // Userid를 보낸다, 응답 X

    @GET("/retrofit/profile0/")
    Call<Post> get_profile_data(@Query("ID") Integer ID);
    // ID를 보내고 | row를 불러온다.

    @Multipart
    @POST("/upload")
    Call<Post> give_profile_image(@Part MultipartBody.Part image, @Part("upload") RequestBody name, @Query("ID") Integer ID);

    @GET("/download")
    Call<ResponseBody> get_profile_image(@Query("ID") Integer ID);

    @GET("/Noticeupload")
    Call<Post> noticeupload(@Query("ID") Integer ID);

    @GET("/Noticedownload")
    Call<List<JsonObject>> noticedownload(@Query("Userid") String Userid);

    @GET("/Noticedownloadimage")
    Call<ResponseBody> get_noticeboard_image(@Query("Image") String image);

    @GET("/Noticeupload")
    Call<ResponseBody> give_noticeboard_content(@Query("Content") String Content, @Query("Userid") String Userid, @Query("Userid1") String Userid1);

    @GET("/Noticeuploadsecret")
    Call<ResponseBody> give_noticeboard_secret_content(@Query("Content") String Content, @Query("Userid") String Userid);

    @FormUrlEncoded
    @POST("/retrofit/post")
    Call<ResponseBody> postFunc(@Field("data") String data);


    @Multipart
    @POST("/upload1")
    Call<Result> uploadImage(@Part MultipartBody.Part image);
}

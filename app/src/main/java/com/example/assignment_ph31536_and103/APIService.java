package com.example.assignment_ph31536_and103;



import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {

    String DOMAIN = "http://10.0.2.2:3000/";

    @GET("/api/list")
    Call<List<StudentModel>> getStudents();

    @POST("/api/add_sv") // Đường dẫn và endpoint tùy thuộc vào API của bạn
    Call<StudentModel> addStudent(@Body StudentModel student);


    @Multipart
    @POST("/api/add_sv")
    Call<StudentModel> addStudentPicker(
            @Part("maSV") String maSV,
            @Part("tenSV") String tenSV,
            @Part("diemTB") double diemTB,
            @Part MultipartBody.Part avatar);


    @DELETE("api/delete/{id}")
    Call<StudentModel> deleteStudent(@Path("id") String idStudent);


    @PUT("api/update/{id}")
    Call<StudentModel> updateStudent(@Path("id") String idStudent,
                                 @Body StudentModel student);
}



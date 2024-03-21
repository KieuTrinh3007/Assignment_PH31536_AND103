package com.example.assignment_ph31536_and103;

import static com.google.firebase.firestore.model.Values.isDouble;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class home_sv extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;

    RecyclerView rcvSinhVien;
    List<StudentModel> listStudentModel;

    StudentAdapter adapter;

    ImageView imagePiker;

    FloatingActionButton floatAddSinhVien;

    EditText edtAvatar;



    private Uri mUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sv);

        rcvSinhVien = findViewById(R.id.rcvSinhVien);
        floatAddSinhVien = findViewById(R.id.floatAddSinhVien);


        listStudentModel = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvSinhVien.setLayoutManager(layoutManager);

        adapter = new StudentAdapter(this,listStudentModel);
        rcvSinhVien.setAdapter(adapter);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        APIService apiService = retrofit.create(APIService.class);
        Call<List<StudentModel>> call = apiService.getStudents();

        Log.d("123", "onCreate: ");

        call.enqueue(new Callback<List<StudentModel>>() {
            @Override
            public void onResponse(Call<List<StudentModel>> call, Response<List<StudentModel>> response) {
                Log.e("abc", "onResponse: " +response.isSuccessful());
                if (response.isSuccessful()) {
                    listStudentModel.clear();
                    listStudentModel.addAll(response.body()); // Thêm dữ liệu vào list hiện có
                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<List<StudentModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });

        floatAddSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(home_sv.this, new StudentModel(), 1);
            }
        });
    }

    public void showDialog(Context context, StudentModel sinhVien, Integer type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dl_themsv, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtMaSV = view.findViewById(R.id.edtMaSV);
        EditText edtNameSV = view.findViewById(R.id.edtNameSV);
        EditText edtDiemTB = view.findViewById(R.id.edtDiemTB);
        edtAvatar = view.findViewById(R.id.edtAvatar);
        imagePiker = view.findViewById(R.id.imgAvatarSV);
        Button btnChonAnh = view.findViewById(R.id.btnChonAnh);
        Button btnSave = view.findViewById(R.id.btnSave);

        if (type == 0) {
            edtMaSV.setText(sinhVien.getMaSV());
            edtNameSV.setText(sinhVien.getTenSV());
            edtDiemTB.setText(sinhVien.getDiemTB() + "");
            edtAvatar.setText(sinhVien.getAvatar());
            Glide.with(view).load(sinhVien.getAvatar()).into(imagePiker);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                String maSV = edtMaSV.getText().toString().trim();
                String tenSV = edtNameSV.getText().toString().trim();
                String diemTB = edtDiemTB.getText().toString();
                String avatar = edtAvatar.getText().toString().trim();



                if (maSV.isEmpty() || tenSV.isEmpty() || diemTB.isEmpty() || avatar.isEmpty()){
                    Toast.makeText(context, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (!isDouble(diemTB)) {
                    Toast.makeText(context, "Điểm trung bình phải là số", Toast.LENGTH_SHORT).show();
                } else {
                    Double point = Double.parseDouble(diemTB);
                    if (point <= 0 || point >= 10) {
                        Toast.makeText(context, "Điểm phải từ 0-10", Toast.LENGTH_SHORT).show();
                    } else {
                        StudentModel sv = new StudentModel(maSV, tenSV, point, avatar);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(APIService.DOMAIN)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();


                        APIService apiService = retrofit.create(APIService.class);
                        File file = new File(edtAvatar.getText().toString());

                        RequestBody requestFile =
                                RequestBody.create(MultipartBody.FORM, file);

// MultipartBody.Part is used to send also the actual file name
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

// add another part within the multipart request
                        RequestBody avatar1 =
                                RequestBody.create(MultipartBody.FORM, "avatar");
//                        Call<StudentModel> call = apiService.addStudentPicker(sv.getMaSV(),sv.getTenSV(),sv.getDiemTB(),body);
                        Call<StudentModel> call = apiService.addStudent(sv);

                        if (type == 0) {
                            call = apiService.updateStudent(sinhVien.get_id(), sv);
                        }

                        call.enqueue(new Callback<StudentModel>() {
                            @Override
                            public void onResponse(Call<StudentModel> call, Response<StudentModel> response) {
                                if (response.isSuccessful()) {
                                    String msg = "Add success";
                                    if (type == 0) {
                                        msg = "Update success";
                                    }
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                    loadData();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<StudentModel> call, Throwable t) {
                                String msg = "Add fail" + t.getMessage();
                                Log.d("245", "onFailure: " + t.getMessage());
                                if (type == 0) {
                                    msg = "update fail";
                                }
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            }
        });

        if (type == 1) {
            btnChonAnh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermission();
                }
            });
        }
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void loadData (){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        APIService apiService = retrofit.create(APIService.class);
        Call<List<StudentModel>> call = apiService.getStudents();

        call.enqueue(new Callback<List<StudentModel>>() {
            @Override
            public void onResponse(Call<List<StudentModel>> call, Response<List<StudentModel>> response) {
                if (response.isSuccessful()){
                    listStudentModel = response.body();
                    Log.d("123", "onResponse: " + listStudentModel.get(0).toString());
                    Log.d("345", "onResponse: " + listStudentModel.get(1).toString());
                    Log.d("678", "onResponse: " + listStudentModel.get(2).toString());
                    Log.d("891", "onResponse: " + listStudentModel.size());

//                    rcvSinhVien = findViewById(R.id.rcvSinhVien);


                    adapter = new StudentAdapter(home_sv.this, listStudentModel);
                    Log.d("iii", "onResponse: " + rcvSinhVien);
                    rcvSinhVien.setLayoutManager(new LinearLayoutManager(home_sv.this));
                   rcvSinhVien.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<StudentModel>> call, Throwable t) {
//                Toast.makeText(Home.this, "Call API Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(home_sv.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("\n" +
                                "Nếu bạn từ chối quyền, bạn không thể sử dụng dịch vụ này\n\nVui lòng bật quyền tại [Cài đặt] > [Quyền]")
                        .setPermissions(android.Manifest.permission.READ_MEDIA_IMAGES)
                        .check();
            } else {
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("\n" +
                                "Nếu bạn từ chối quyền, bạn không thể sử dụng dịch vụ này\n\nVui lòng bật quyền tại [Cài đặt] > [Quyền]")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        }
    }

    private ActivityResultLauncher<Intent> mActivityRequestLauncher =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK){
                        Intent data = o.getData();
                        if (data == null){
                            return;
                        }

                        Uri uri = data.getData();
                        mUri = uri;

                        edtAvatar.setText(uri.getPath());



                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            imagePiker.setImageBitmap(bitmap);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            });


    public void openImagePicker(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mActivityRequestLauncher.launch(intent);
    }

//    public String getRealPathFromURI (Uri contentUri) { String path = null; String[] proj = { MediaStore.MediaColumns.DATA };
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) { int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            path = cursor.getString(column_index); } cursor.close(); return path; }

}
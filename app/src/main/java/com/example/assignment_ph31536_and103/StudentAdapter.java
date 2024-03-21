package com.example.assignment_ph31536_and103;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<StudentModel> studentModelList;
    private Context context;

    home_sv mainActivity ;

    public StudentAdapter(Context context, List<StudentModel> studentModelList) {
        this.context = context;
        this.studentModelList = studentModelList;
        mainActivity = (home_sv) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_rcv_sinhvien,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (position >= 0 && position <= studentModelList.size()){
            StudentModel sv = studentModelList.get(position);

            holder.tvmaSV.setText("Mã SV : "+sv.getMaSV());
            holder.tvtenSV.setText("Họ tên : "+sv.getTenSV());
            holder.tvdiemTB.setText("Điểm TB : "+sv.getDiemTB());

            Glide.with(holder.itemView.getContext())
                    .load(sv.getAvatar())
                    .into(holder.imgAvatar);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idStudent = sv.get_id();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Cảnh báo");
                    builder.setMessage("Bạn có muốn xóa không?");
                    builder.setNegativeButton("No",null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(APIService.DOMAIN)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            APIService apiService = retrofit.create(APIService.class);
                            apiService.deleteStudent(idStudent).enqueue(new Callback<StudentModel>() {
                                @Override
                                public void onResponse(Call<StudentModel> call, Response<StudentModel> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                                        studentModelList.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<StudentModel> call, Throwable t) {
                                    Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    home_sv home = new home_sv();
//                    home.showDialog(context,studentModelList.get(position),0);
                    mainActivity.showDialog(context,studentModelList.get(position),0);
                }
            });


        }
    }

    @Override
    public int getItemCount() {

        if (studentModelList.size() > 0){
            return studentModelList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvtenSV, tvmaSV, tvdiemTB;
        ImageView imgAvatar;

        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvId);
            tvtenSV = itemView.findViewById(R.id.tvtenSV);
            tvmaSV = itemView.findViewById(R.id.tvmaSV);
            tvdiemTB = itemView.findViewById(R.id.tvdiemTB);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
    }


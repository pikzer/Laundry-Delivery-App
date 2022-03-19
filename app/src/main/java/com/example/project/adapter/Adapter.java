package com.example.project.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.OrderRecycle;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<OrderRecycle> orderRecycleList ;

    public Adapter(List<OrderRecycle> orderRecycleList) {
        this.orderRecycleList = orderRecycleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycle_item,parent,false);
        return  new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int resource = orderRecycleList.get(position).getStatusImg() ;
        String orderNo = orderRecycleList.get(position).getOrderNumTV() ;
        String serviceTv = orderRecycleList.get(position).getService_tv() ;
        String pickordropTv = orderRecycleList.get(position).getPickordropTv() ;
        String droporPickTime = orderRecycleList.get(position).getDroporPickTime() ;
        String statusNow_tv = orderRecycleList.get(position).getStatusNow_tv() ;
        holder.setData(resource,orderNo,serviceTv,pickordropTv,droporPickTime,statusNow_tv) ;
    }

    @Override
    public int getItemCount() {
        return   orderRecycleList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView ;
        public TextView orderNo,serviceTv,pickordropTv,droporPickTime,statusNow_tv ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.statusImg) ;
            orderNo = itemView.findViewById(R.id.orderNumTV) ;
            serviceTv = itemView.findViewById(R.id.service_tv) ;
            pickordropTv = itemView.findViewById(R.id.pickordropTv) ;
            droporPickTime =  itemView.findViewById(R.id.droporPickTime) ;
            statusNow_tv =  itemView.findViewById(R.id.statusNow_tv) ;
        }
        public void setData(int resource, String orderNo,String serviceTv,String pickordropTv, String droporPickTime, String statusNow_tv){
            imageView.setImageResource(resource);
            this.orderNo.setText(orderNo);
            this.serviceTv.setText(serviceTv);
            this.pickordropTv.setText(pickordropTv);
            this.droporPickTime.setText(droporPickTime);
            this.statusNow_tv.setText(statusNow_tv);
        }
    }
}

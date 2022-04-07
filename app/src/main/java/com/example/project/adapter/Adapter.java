package com.example.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.OrderRecycle;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<OrderRecycle> orderRecycleList ;
    private OnNoteListener onNoteListener ;

    public Adapter(List<OrderRecycle> orderRecycleList, OnNoteListener onNoteListener) {
        this.orderRecycleList = orderRecycleList;
        this.onNoteListener = onNoteListener ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycle_item,parent,false);
        return  new ViewHolder(view,onNoteListener) ;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView ;
        public TextView orderNo,serviceTv,pickordropTv,droporPickTime,statusNow_tv ;
        OnNoteListener onNoteListener ;
        public ViewHolder(@NonNull View itemView, OnNoteListener onClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.statusImg) ;
            orderNo = itemView.findViewById(R.id.orderNumTV) ;
            serviceTv = itemView.findViewById(R.id.service_tv) ;
            pickordropTv = itemView.findViewById(R.id.pickordropTv) ;
            droporPickTime =  itemView.findViewById(R.id.droporPickTime) ;
            statusNow_tv =  itemView.findViewById(R.id.statusNow_tv) ;
            this.onNoteListener = onClickListener;
            itemView.setOnClickListener(this);
        }
        public void setData(int resource, String orderNo,String serviceTv,String pickordropTv, String droporPickTime, String statusNow_tv){
            imageView.setImageResource(resource);
            this.orderNo.setText(orderNo);
            this.serviceTv.setText(serviceTv);
            this.pickordropTv.setText(pickordropTv);
            this.droporPickTime.setText(droporPickTime);
            this.statusNow_tv.setText(statusNow_tv);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener {
        void onNoteClick(int pos) ;
    }
}

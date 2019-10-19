package com.example.roadcomplaints.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.roadcomplaints.R;
import com.example.roadcomplaints.model.Complaints;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Complaints> complaintsList;

    public RecyclerViewAdapter(Context context, List<Complaints> complaintsList) {

        this.complaintsList = complaintsList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Complaints complaints = complaintsList.get(position);

        holder.tv_complaintId.setText(complaints.getComplaintId());

        holder.tv_description.setText(complaints.getDescription());

        holder.tv_status.setText(complaints.getStatus());

    }

    @Override
    public int getItemCount() {

        return complaintsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_complaintId,tv_description,tv_status;

        public ViewHolder(View itemView) {

            super(itemView);

            tv_status = (TextView) itemView.findViewById(R.id.tv_status);

            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_complaintId = (TextView) itemView.findViewById(R.id.tv_complaintId);
        }
    }
}

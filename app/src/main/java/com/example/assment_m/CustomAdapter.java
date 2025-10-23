package com.example.assment_m;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<ItemViewModel> mList;


    public CustomAdapter(List<ItemViewModel> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);

    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemViewModel itemViewModel = mList.get(position);

        holder.tvTitle.setText(String.format("%sC-%sC", String.valueOf(itemViewModel.getTempMax()), String.valueOf(itemViewModel.getTempMin())));
        holder.tvCom.setText(itemViewModel.getSpeed());
        holder.tvDay.setText( itemViewModel.getWeek());
        holder.tvFW.setText( itemViewModel.getFW());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        TextView tvFW = itemView.findViewById(R.id.tvFW);
        TextView tvTitle = itemView.findViewById(R.id.tvTitle);
        TextView tvCom = itemView.findViewById(R.id.tvCom);
        TextView tvDay = itemView.findViewById(R.id.tvDay);

    }


}
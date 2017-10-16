package org.zgc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zgc.R;

import java.util.List;

/**
 * Created by Nick on 2017/10/2
 */
public class RouteMacListAdapter extends RecyclerView.Adapter<RouteMacListAdapter.MyViewHolder> {
    private List<String> data;

    public RouteMacListAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_mac_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_mac.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mac;

        public MyViewHolder(View view) {
            super(view);
            tv_mac = (TextView) view.findViewById(R.id.tv_mac);
        }
    }
}

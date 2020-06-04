package com.example.delivery.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delivery.Model.Order;
import com.example.delivery.Model.OrderItem;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;

import org.joda.time.Interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    List<Order> orderList;
    Context context;

    public HistoryAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateNum,tvDateType,tvDate,tvTotdaCost;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvDateNum=itemView.findViewById(R.id.tvDateNum);
            tvDateType=itemView.findViewById(R.id.tvDateType);
            tvTotdaCost=itemView.findViewById(R.id.tvTotalCost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClicked((Order)itemView.getTag());
                }
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.history_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(orderList.get(position));
        holder.tvTotdaCost.setText(String.valueOf(orderList.get(position).getTotalPrice()));
        try {
            holder.tvDate.setText(new SimpleDateFormat("yyyy/MM/dd  HH:mm")
                    .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(orderList.get(position).getCreated_at())));
            setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderList.get(position).getCreated_at()),holder.tvDateType,holder.tvDateNum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderList==null?0:orderList.size();
    }

    private void setDate(Date date, TextView tvDateType, TextView tvDateNum){

        Interval interval=new Interval(date.getTime(),new Date().getTime());
        int years=interval.toPeriod().getYears();
        int months=interval.toPeriod().getMonths();
        int days=(interval.toPeriod().getWeeks()*7)+(interval.toPeriod().getDays());

        if(years!=0){
            if(years>1)
                tvDateType.setText(context.getString(R.string.years));

            else
                tvDateType.setText(context.getString(R.string.year));
            tvDateNum.setText(String.valueOf(years));
        }
        else if(months!=0){
            if (months>1)
                tvDateType.setText(context.getString(R.string.months));
            else
                tvDateType.setText(context.getString(R.string.month));
            tvDateNum.setText(String.valueOf(months));

        }else{
            if(days>1)
                tvDateType.setText(context.getString(R.string.days));
            else
                tvDateType.setText(context.getString(R.string.day));

            tvDateNum.setText(String.valueOf(days));
        }

    }

    private void itemClicked(Order order){

        View view=LayoutInflater.from(this.context).inflate(R.layout.cart_list,null);
        final RecyclerView rvCart=view.findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(this.context));
        LinearLayout totalCost=view.findViewById(R.id.top);
        final ProgressBar progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        totalCost.setVisibility(View.GONE);

        RetrofitClient.getApi().getOrderItems(order.getId()).enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if(response.isSuccessful()){
                    rvCart.setAdapter(new CartAdapter(response.body(),context,null));
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, context.getString(R.string.failed_to_get_items), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.string.failed_to_get_items), Toast.LENGTH_SHORT).show();

            }
        });

        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setView(view);
        dialog.setNegativeButton(context.getString(R.string.cancel),null);
        dialog.show();
    }



}

package com.bazara2z.distributorhelper.MyOrdersPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bazara2z.distributorhelper.BuildOrderPackage.BuildOrder;
import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.OrderSummaryPackage.OrderSummaryModel;
import com.bazara2z.distributorhelper.R;

import java.util.List;

/**
 * Created by Maddy on 08-03-2016.
 */
public class MyOrdersAdapter extends BaseAdapter {

    String LOG_TAG = "MyOrdersAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<OrderSummaryModel> listData;

    public MyOrdersAdapter(Context context, List<OrderSummaryModel> listData){

        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item_layout_my_order, null);
            holder = new ViewHolder();
            holder.retailerName = (TextView) convertView.findViewById(R.id.my_order_retailer_name);
            holder.productCount = (TextView) convertView.findViewById(R.id.my_order_product_count);
            holder.totalPrice = (TextView) convertView.findViewById(R.id.my_order_total_price);
            holder.editedPrice = (TextView) convertView.findViewById(R.id.my_order_edited_price);
            holder.editOrder = (Button) convertView.findViewById(R.id.my_order_edit_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderSummaryModel orderSummaryModel = this.listData.get(position);


        holder.retailerName.setText(orderSummaryModel.getRetailerName());
        holder.productCount.setText(String.valueOf(orderSummaryModel.getProductCount()));
        holder.totalPrice.setText(String.valueOf(orderSummaryModel.getTotalPrice()));
        holder.editedPrice.setText(String.valueOf(orderSummaryModel.getModifiedPrice()));

        if (orderSummaryModel.getIsOrderSynced() == 1){
            holder.editOrder.setText("View");
            holder.editOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int retailerId = orderSummaryModel.getRetailerId();
                    int orderId = orderSummaryModel.getOrderId();

                    Intent intent = new Intent(context, ViewOrderSummary.class);

                    intent.putExtra(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID, retailerId);
                    intent.putExtra(DistributorContract.OrdersEntry._ID, orderId);

                    context.startActivity(intent);
                    ((Activity) context).finish();

                }
            });
        }
        else {
            holder.editOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int retailerId = orderSummaryModel.getRetailerId();
                    int orderId = orderSummaryModel.getOrderId();
                    int newOrder = 0;

                    Intent intent = new Intent(context, BuildOrder.class);
                    intent.putExtra(DistributorContract.RetailersEntry.COLUMN_RETAILER_ID, retailerId);
                    intent.putExtra(DistributorContract.OrdersEntry._ID, orderId);
                    intent.putExtra("NewOrder", newOrder);
                    context.startActivity(intent);
                    ((Activity) context).finish();

                }
            });
        }

        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView retailerName;
        TextView productCount;
        TextView totalPrice;
        TextView editedPrice;
        Button editOrder;
    }
}

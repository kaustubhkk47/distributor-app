package com.bazara2z.distributorhelper.OrderSummaryPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bazara2z.distributorhelper.BuildOrderPackage.BuildOrderModel;
import com.bazara2z.distributorhelper.R;

import java.util.List;

/**
 * Created by Maddy on 07-03-2016.
 */
public class OrderSummaryAdapter extends BaseAdapter {

    String LOG_TAG = "OrderSummaryAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<BuildOrderModel> listData;

    public OrderSummaryAdapter(Context context, List<BuildOrderModel> listData){

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
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder holder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item_layout_order_summary, null);
            holder = new ViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.order_summary_product_name);
            holder.quantity = (TextView) convertView.findViewById(R.id.order_summary_total_quantity_listitem);
            holder.totalPrice = (TextView) convertView.findViewById(R.id.order_summary_total_price_listitem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BuildOrderModel buildOrderModel = this.listData.get(position);

        holder.productName.setText(buildOrderModel.getProductName());
        holder.quantity.setText(String.valueOf(buildOrderModel.getQuantity()));
        holder.totalPrice.setText(String.valueOf(buildOrderModel.getTotalPrice()));

        holder.id = position;
        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView productName;
        TextView quantity;
        TextView totalPrice;
    }

}

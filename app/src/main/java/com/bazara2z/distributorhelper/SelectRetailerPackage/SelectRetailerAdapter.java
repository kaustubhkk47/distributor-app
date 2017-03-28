package com.bazara2z.distributorhelper.SelectRetailerPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bazara2z.distributorhelper.R;

import java.util.List;

/**
 * Created by Maddy on 05-03-2016.
 */
public class SelectRetailerAdapter extends BaseAdapter {

    String LOG_TAG = "SelectRetailerAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<SelectRetailerModel> listData;

    public SelectRetailerAdapter(Context context, List<SelectRetailerModel> listData){
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
            convertView = layoutInflater.inflate(R.layout.list_item_layout_select_retailer, null);
            holder = new ViewHolder();
            holder.retailerName = (TextView) convertView.findViewById(R.id.select_retailer_name);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.select_retailer_phone_number);
            holder.addressLine1 = (TextView) convertView.findViewById(R.id.select_retailer_address_line1);
            holder.addressLine2 = (TextView) convertView.findViewById(R.id.select_retailer_address_line2);
            holder.landmark = (TextView) convertView.findViewById(R.id.select_retailer_landmark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectRetailerModel selectRetailerModel = this.listData.get(position);
        holder.retailerName.setText(selectRetailerModel.getRetailerName());
        holder.phoneNumber.setText(selectRetailerModel.getPhoneNumber());
        holder.addressLine1.setText(selectRetailerModel.getAddressLine1());
        if(selectRetailerModel.getAddressLine2().equals("")) {
            holder.addressLine2.setVisibility(View.GONE);
        }
        else {
            holder.addressLine2.setText(selectRetailerModel.getAddressLine2());
        }
        if(selectRetailerModel.getLandmark().equals("")) {
            holder.landmark.setVisibility(View.GONE);
        }
        else {
            holder.landmark.setText(selectRetailerModel.getLandmark());
        }

        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView retailerName;
        TextView phoneNumber;
        TextView addressLine1;
        TextView addressLine2;
        TextView landmark;
    }

}

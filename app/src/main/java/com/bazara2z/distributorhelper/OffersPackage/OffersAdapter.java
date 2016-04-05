package com.bazara2z.distributorhelper.OffersPackage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bazara2z.distributorhelper.R;

import java.util.List;

/**
 * Created by Maddy on 04-03-2016.
 */
public class OffersAdapter extends BaseAdapter{

    String LOG_TAG = "OffersAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<OffersModel>  listData;

    public OffersAdapter(Context context, List<OffersModel> listData){
        Log.w(LOG_TAG, "OffersAdapter created");
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
            convertView = layoutInflater.inflate(R.layout.offer_list_item_layout, null);
            holder = new ViewHolder();
            holder.offerDetails = (TextView) convertView.findViewById(R.id.offers_offer_details);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OffersModel offersModel = this.listData.get(position);
        holder.offerDetails.setText(offersModel.getOfferDetails());

        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView offerDetails;
    }
}

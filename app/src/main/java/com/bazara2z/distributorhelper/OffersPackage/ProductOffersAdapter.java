package com.bazara2z.distributorhelper.OffersPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.R;

import java.util.List;

/**
 * Created by Maddy on 04-03-2016.
 */
public class ProductOffersAdapter extends BaseAdapter{

    String LOG_TAG = "ProductOffersAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<ProductOffersModel>  listData;

    public ProductOffersAdapter(Context context, List<ProductOffersModel> listData){

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
            convertView = layoutInflater.inflate(R.layout.list_item_layout_offer, null);
            holder = new ViewHolder();
            holder.offerDetails = (TextView) convertView.findViewById(R.id.product_offers_offer_details);
            holder.productName = (TextView) convertView.findViewById(R.id.product_offers_product_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductOffersModel productOffersModel = this.listData.get(position);
        String offerDetailsString = productOffersModel.getOfferDetailsString();

        holder.offerDetails.setText(offerDetailsString);
        holder.productName.setText(productOffersModel.getProductName());

        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView productName;
        TextView offerDetails;
    }

}

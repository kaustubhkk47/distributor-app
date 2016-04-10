package com.bazara2z.distributorhelper.ProductsDisplayPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bazara2z.distributorhelper.OffersPackage.ProductOffersModel;
import com.bazara2z.distributorhelper.R;

import java.util.List;

/**
 * Created by Maddy on 07-04-2016.
 */
public class ProductOffersDialogAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;

    private List<ProductOffersModel>  listData;

    public ProductOffersDialogAdapter(Context context, List<ProductOffersModel> listData){
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
            convertView = layoutInflater.inflate(R.layout.list_item_offers_dialog_product_display, null);
            holder = new ViewHolder();
            holder.offerDetails = (TextView) convertView.findViewById(R.id.list_item_offers_dialog_product_display_offer_details);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductOffersModel productOffersModel = this.listData.get(position);
        String offerDetailsString = productOffersModel.getOfferDetailsString();

        holder.offerDetails.setText(offerDetailsString);

        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView offerDetails;
    }
}

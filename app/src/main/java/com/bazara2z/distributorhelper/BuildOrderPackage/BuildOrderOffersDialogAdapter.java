package com.bazara2z.distributorhelper.BuildOrderPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bazara2z.distributorhelper.OffersPackage.ProductOffersModel;
import com.bazara2z.distributorhelper.R;

import java.util.List;

/**
 * Created by Maddy on 07-04-2016.
 */
public class BuildOrderOffersDialogAdapter extends BaseAdapter {

    String LOG_TAG = "BuildOrderOffersDialogAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<ProductOffersModel> listData;

    public BuildOrderOffersDialogAdapter(Context context, List<ProductOffersModel> listData){
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
        ViewHolder holder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item_offers_dialog_build_order, null);
            holder = new ViewHolder();
            holder.offerDetails = (TextView) convertView.findViewById(R.id.list_item_offers_dialog_build_order_offer_details);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox = (CheckBox) convertView.findViewById(R.id.list_item_offers_dialog_build_order_offer_checkBox);

        final ProductOffersModel productOffersModel = this.listData.get(position);
        String offerDetailsString = productOffersModel.getOfferDetailsString();
        holder.checkBox.setChecked(productOffersModel.getOfferApplied() == 1);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productOffersModel.getOfferApplied() == 1){
                    listData.get(position).setOfferApplied(0);
                    notifyDataSetChanged();
                }
                else {
                    int size = listData.size();
                    for (int i = 0; i < size; i++){
                        listData.get(i).setOfferApplied(0);
                    }
                    listData.get(position).setOfferApplied(1);
                    notifyDataSetChanged();
                }

            }
        });



        holder.offerDetails.setText(offerDetailsString);

        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView offerDetails;
        CheckBox checkBox;
    }
}

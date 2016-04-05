package com.bazara2z.distributorhelper.ProductsDisplayPackage;

import android.content.Context;
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
public class ProductsDisplayAdapter extends BaseAdapter{

    String LOG_TAG = "ProductsDisplayAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<ProductsDisplayModel> listData;

    public ProductsDisplayAdapter(Context context, List<ProductsDisplayModel> listData){

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
            convertView = layoutInflater.inflate(R.layout.product_display_list_item_layout, null);
            holder = new ViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.product_display_product_name);
            holder.pricePerUnit = (TextView) convertView.findViewById(R.id.product_display_price_per_unit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        ProductsDisplayModel productsDisplayModel = this.listData.get(position);
        Double pricePerUnit = productsDisplayModel.getPricePerUnit();


        holder.productName.setText(productsDisplayModel.getProductName());
        holder.pricePerUnit.setText(String.valueOf(pricePerUnit));

        //Log.w(LOG_TAG, "Price per unit is " + pricePerUnit + " and is null " + (pricePerUnit != null) );

        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView productName;
        TextView pricePerUnit;
    }
}

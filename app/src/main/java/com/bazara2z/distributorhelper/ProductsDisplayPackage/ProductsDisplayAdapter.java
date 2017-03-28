package com.bazara2z.distributorhelper.ProductsDisplayPackage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
            convertView = layoutInflater.inflate(R.layout.list_item_layout_product_display, null);
            holder = new ViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.product_display_product_name);
            holder.pricePerUnit = (TextView) convertView.findViewById(R.id.product_display_price_per_unit);
            holder.offersLayout = convertView.findViewById(R.id.product_display_offers_layout);
            holder.offers = (Button) convertView.findViewById(R.id.product_display_offers_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final ProductsDisplayModel productsDisplayModel = this.listData.get(position);
        Double pricePerUnit = productsDisplayModel.getPricePerUnit();

        holder.productName.setText(productsDisplayModel.getProductName());
        holder.pricePerUnit.setText(String.valueOf(pricePerUnit));

        if (productsDisplayModel.getProductOffersCount() > 0){
            holder.offersLayout.setVisibility(View.VISIBLE);

            String offersText = "Offers(" + productsDisplayModel.getProductOffersCount() + ")";

            holder.offers.setText(offersText);

            holder.offers.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    LayoutInflater factory = LayoutInflater.from(context);
                    final View dialogBoxProductOffers = factory.inflate(
                            R.layout.dialog_box_product_offers, null);

                    final AlertDialog offersDialog = new AlertDialog.Builder(context).create();

                    offersDialog.setView(dialogBoxProductOffers);

                    ProductOffersDialogAdapter productOffersDialogAdapter = new ProductOffersDialogAdapter(context, productsDisplayModel.getProductOffers());

                    ListView listView = (ListView) dialogBoxProductOffers.findViewById(R.id.dialog_box_product_offers_list_view);

                    listView.setAdapter(productOffersDialogAdapter);

                    Button okButton = (Button) dialogBoxProductOffers.findViewById(R.id.dialog_box_product_offers_ok_button);

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //your business logic
                            offersDialog.dismiss();
                        }
                    });

                    offersDialog.show();
                }
            });
        }
        else {
            holder.offersLayout.setVisibility(View.GONE);
        }

        //Log.w(LOG_TAG, "Price per unit is " + pricePerUnit + " and is null " + (pricePerUnit != null) );

        return convertView;
    }

    static class ViewHolder{
        int id;
        TextView productName;
        TextView pricePerUnit;
        View offersLayout;
        Button offers;
    }
}

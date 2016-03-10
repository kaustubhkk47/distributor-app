package com.bazara2z.distributorhelper.BuildOrderPackage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bazara2z.distributorhelper.R;

import java.util.List;

/**
 * Created by Maddy on 06-03-2016.
 */
public class BuildOrderAdapter extends BaseAdapter {

    String LOG_TAG = "BuildOrderAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<BuildOrderModel> listData;

    public BuildOrderAdapter(Context context, List<BuildOrderModel> listData){

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
            convertView = layoutInflater.inflate(R.layout.build_order_list_item_layout, null);
            holder = new ViewHolder();
            holder.offerDetails = (TextView) convertView.findViewById(R.id.build_order_offer_details);
            holder.productName = (TextView) convertView.findViewById(R.id.build_order_product_name);
            holder.pricePerUnit = (TextView) convertView.findViewById(R.id.build_order_price_per_unit);
            holder.addButton = (Button) convertView.findViewById(R.id.build_order_add_button);
            holder.subtractButton = (Button) convertView.findViewById(R.id.build_order_subtract_button);
            holder.quantity = (Button) convertView.findViewById(R.id.build_order_quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        BuildOrderModel buildOrderModel = this.listData.get(position);
        Double pricePerUnit = buildOrderModel.getPricePerUnit();
        String offerDetails = buildOrderModel.getOfferDetails();

        holder.offerDetails.setText(offerDetails);
        holder.productName.setText(buildOrderModel.getProductName());
        holder.pricePerUnit.setText(String.valueOf(pricePerUnit));
        holder.quantity.setText(String.valueOf(buildOrderModel.getQuantity()));
        holder.id = position;


        if (offerDetails == null){
            holder.offerDetails.setVisibility(View.GONE);
        }
        else {
            holder.offerDetails.setVisibility(View.VISIBLE);
        }

        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptView = li.inflate(R.layout.build_order_quantity_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                final EditText userInput = (EditText) promptView
                        .findViewById(R.id.buildOrderDialogUserInput);


                Log.w(LOG_TAG, "Button clicked");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final String enteredQuantity = userInput.getText().toString();

                                        if (isNumberValid(enteredQuantity)) {
                                            int newQuantity = Integer.valueOf(enteredQuantity);
                                            listData.get(position).setQuantity(newQuantity);
                                            listData.get(position).setTotalPrice(newQuantity * listData.get(position).getPricePerUnit());
                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(context, "Please enter valid number", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                alertDialog.show();



            }
        });


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemIndex = v.getId();
                int initialQuantity = listData.get(position).getQuantity();
                int newQuantity = initialQuantity + 1;
                listData.get(position).setQuantity(newQuantity);
                listData.get(position).setTotalPrice(newQuantity*listData.get(position).getPricePerUnit());
                notifyDataSetChanged();
            }
        });

        holder.subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemIndex = v.getId();
                int initialQuantity = listData.get(position).getQuantity();
                int newQuantity = initialQuantity - 1;
                if (newQuantity >= 0) {
                    listData.get(position).setQuantity(newQuantity);
                    listData.get(position).setTotalPrice(newQuantity*listData.get(position).getPricePerUnit());
                }
                notifyDataSetChanged();
            }
        });


        return convertView;
    }

    private boolean isNumberValid(String number) {
        if (number.matches("[0-9]+")){
            return true;
        }
        return false;
    }



    static class ViewHolder{
        int id;
        TextView offerDetails;
        TextView productName;
        TextView pricePerUnit;
        Button quantity;
        Button addButton;
        Button subtractButton;
    }
}

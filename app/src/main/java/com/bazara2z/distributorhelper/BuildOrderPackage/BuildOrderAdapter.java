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

import com.bazara2z.distributorhelper.Data.DistributorContract;
import com.bazara2z.distributorhelper.OffersPackage.ProductOffersModel;
import com.bazara2z.distributorhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maddy on 10-04-2016.
 */
public class BuildOrderAdapter extends BaseAdapter {

    String LOG_TAG = "BuildOrderAdapter";

    private LayoutInflater layoutInflater;
    private Context context;

    private List<BuildOrderModel> listData;

    public BuildOrderAdapter() {

    }

    public BuildOrderAdapter(Context context, List<BuildOrderModel> listData) {
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout_build_order, null);
            holder = new ViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.build_order_product_name);
            holder.pricePerUnit = (TextView) convertView.findViewById(R.id.build_order_price_per_unit);
            holder.addButton = (Button) convertView.findViewById(R.id.build_order_add_button);
            holder.subtractButton = (Button) convertView.findViewById(R.id.build_order_subtract_button);
            holder.quantity = (Button) convertView.findViewById(R.id.build_order_quantity);
            holder.totalPrice = (TextView) convertView.findViewById(R.id.build_order_list_total_price_text_view);
            holder.editedPrice = (Button) convertView.findViewById(R.id.build_order_list_modified_price_button);
            holder.offersLayout = convertView.findViewById(R.id.build_order_offers_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.offers = (Button) convertView.findViewById(R.id.build_order_offers_button);
        holder.offerStatus = (TextView) convertView.findViewById(R.id.build_order_offers_applied_status);

        BuildOrderModel buildOrderModel = this.listData.get(position);
        Double pricePerUnit = buildOrderModel.getPricePerUnit();


        holder.productName.setText(buildOrderModel.getProductName());
        holder.pricePerUnit.setText(String.valueOf(pricePerUnit));
        holder.quantity.setText(String.valueOf(buildOrderModel.getQuantity()));
        holder.totalPrice.setText(String.format("%.1f", buildOrderModel.getTotalPrice()));
        holder.editedPrice.setText(String.format("%.1f", buildOrderModel.getEditedPrice()));
        holder.id = position;

        if (buildOrderModel.getProductOffersCount() >= 0) {
            holder.offersLayout.setVisibility(View.VISIBLE);
            String offersText = "Offers(" + buildOrderModel.getProductOffersCount() + ")";
            holder.offers.setText(offersText);
            holder.offers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater factory = LayoutInflater.from(context);
                    final View view = factory.inflate(R.layout.dialog_box_build_order_offers, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setView(view);
                    BuildOrderOffersDialogAdapter buildOrderOffersDialogAdapter =
                            new BuildOrderOffersDialogAdapter(context, listData.get(position).getProductOffers());

                    view.findViewById(R.id.dialog_box_build_order_offers_ok_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            refreshOffers(position, holder);
                        }
                    });

                    alertDialog.show();

                }
            });
        } else {
            holder.offersLayout.setVisibility(View.GONE);
            listData.get(position).setDiscountPercent(0);
        }


        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptView = li.inflate(R.layout.quantity_prompt_build_order, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                final EditText userInput = (EditText) promptView
                        .findViewById(R.id.buildOrderDialogUserInput);


                Log.w(LOG_TAG, "Quantity input Button clicked");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final String enteredQuantity = userInput.getText().toString();

                                        if (isNumberValid(enteredQuantity)) {
                                            int newQuantity = Integer.valueOf(enteredQuantity);
                                            refreshPrices(position, newQuantity);
                                            refreshOffers(position, holder);
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
                refreshPrices(position, newQuantity);
                refreshOffers(position, holder);
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
                    refreshPrices(position, newQuantity);
                    refreshOffers(position, holder);
                }
                notifyDataSetChanged();
            }
        });


        holder.editedPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptView = li.inflate(R.layout.quantity_prompt_build_order, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptView);

                TextView title = (TextView) promptView.findViewById(R.id.quantity_prompt_build_order_title);

                title.setText("Enter edited price :");

                final EditText userInput = (EditText) promptView
                        .findViewById(R.id.buildOrderDialogUserInput);


                Log.w(LOG_TAG, "Edited price Button clicked");

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final String enteredPrice = userInput.getText().toString();

                                        if (isNumberValid(enteredPrice)) {
                                            double newPrice = Double.valueOf(enteredPrice);
                                            listData.get(position).setEditedPrice(newPrice);
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

        refreshOffers(position, holder);

        return convertView;
    }


    private boolean isNumberValid(String number) {
        if (number.matches("[0-9]+")) {
            return true;
        }
        return false;
    }

    public void refreshOffers(int position, ViewHolder holder) {

        BuildOrderModel buildOrderModel = listData.get(position);
        ArrayList<ProductOffersModel> productOffersModels = buildOrderModel.getProductOffers();
        int size = productOffersModels.size();
        int offerApplied = 0;

        for (int i = 0; i < size; i++) {
            ProductOffersModel productOffersModel = productOffersModels.get(i);

            if (productOffersModel.getOfferApplied() == 1) {
                offerApplied = 1;
                switch (productOffersModel.getOfferType()) {
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_VOLUME_DISCOUNT: {
                        if (productOffersModel.getMinimumOrderQuantity() <= buildOrderModel.getQuantity()) {
                            listData.get(position).setDiscountPercent(productOffersModel.getDiscountPercent());
                            holder.offerStatus.setText(productOffersModel.offerAppliedText(buildOrderModel.getQuantity()));
                            holder.offerStatus.setVisibility(View.VISIBLE);
                            refreshPrices(position, buildOrderModel.getQuantity());
                        } else {
                            listData.get(position).getProductOffers().get(i).setOfferApplied(0);
                            holder.offerStatus.setVisibility(View.GONE);
                            listData.get(position).setDiscountPercent(0.0);
                            refreshPrices(position, buildOrderModel.getQuantity());
                            Toast.makeText(context, "Add at least " + productOffersModel.getMinimumOrderQuantity()
                                    + " units to apply this offer", Toast.LENGTH_SHORT).show();

                        }
                        notifyDataSetChanged();
                        break;

                    }
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_X_GET_Y_FREE: {
                        if (productOffersModel.getxCount() <= buildOrderModel.getQuantity()) {
                            holder.offerStatus.setVisibility(View.VISIBLE);
                            holder.offerStatus.setText(productOffersModel.offerAppliedText(buildOrderModel.getQuantity()));

                        } else {
                            listData.get(position).getProductOffers().get(i).setOfferApplied(0);
                            holder.offerStatus.setVisibility(View.GONE);
                            Toast.makeText(context, "Add at least " + productOffersModel.getxCount()
                                    + " units to apply this offer", Toast.LENGTH_SHORT).show();

                        }

                        notifyDataSetChanged();
                        break;

                    }
                    case DistributorContract.ProductOffersEntry.PRODUCT_OFFER_TYPE_BUY_A_GET_B_FREE: {
                        if (productOffersModel.getxCount() <= buildOrderModel.getQuantity()) {
                            holder.offerStatus.setVisibility(View.VISIBLE);
                            holder.offerStatus.setText(productOffersModel.offerAppliedText(buildOrderModel.getQuantity()));

                        } else {
                            listData.get(position).getProductOffers().get(i).setOfferApplied(0);
                            holder.offerStatus.setVisibility(View.GONE);
                            Toast.makeText(context, "Add at least " + productOffersModel.getxCount()
                                    + " units to apply this offer", Toast.LENGTH_SHORT).show();

                        }
                        notifyDataSetChanged();
                        break;

                    }
                }
            }

        }

        if (offerApplied == 0) {
            holder.offerStatus.setVisibility(View.GONE);
            listData.get(position).setDiscountPercent(0.0);
        }

    }

    public void refreshPrices(int position, int quantity) {
        listData.get(position).setQuantity(quantity);
        double discountPercent = (Double.valueOf(listData.get(position).getDiscountPercent()) / 100);
        double quantity1 = quantity;
        double totalPrice = listData.get(position).getPricePerUnit() * quantity1 * (1 - discountPercent);
        listData.get(position).setTotalPrice(totalPrice);
        listData.get(position).setEditedPrice(totalPrice);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        Button addButton;
        Button editedPrice;
        int id;
        TextView offerStatus;
        Button offers;
        View offersLayout;
        TextView pricePerUnit;
        TextView productName;
        Button quantity;
        Button subtractButton;
        TextView totalPrice;
    }

}

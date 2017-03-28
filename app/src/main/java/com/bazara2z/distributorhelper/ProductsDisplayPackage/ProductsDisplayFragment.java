package com.bazara2z.distributorhelper.ProductsDisplayPackage;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import com.bazara2z.distributorhelper.Data.DistributorContract.*;
import com.bazara2z.distributorhelper.OffersPackage.ProductOffersModel;

public class ProductsDisplayFragment extends ListFragment {

    private final String LOG_TAG = "ProductsDisplayFragment";

    private OnFragmentInteractionListener mListener;

    public static ProductsDisplayFragment newInstance() {
        ProductsDisplayFragment fragment = new ProductsDisplayFragment();
        return fragment;
    }


    public ProductsDisplayFragment() {
    }



    List<ProductsDisplayModel> productsDisplayModelList;
    ProductsDisplayAdapter productsDisplayAdapter;
    ListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productsDisplayModelList = getAllProducts();

        productsDisplayAdapter = new ProductsDisplayAdapter(getActivity(), productsDisplayModelList);

        this.setListAdapter(productsDisplayAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    public List<ProductsDisplayModel> getAllProducts(){
        ArrayList<ProductsDisplayModel> mProductDisplayModelList = new ArrayList<ProductsDisplayModel>();

        Context mContext = getActivity().getApplicationContext();

        String[] columns = {ProductsEntry.COLUMN_PRODUCT_ID ,ProductsEntry.COLUMN_PRODUCT_NAME, ProductsEntry.COLUMN_PRICE_PER_UNIT};

        Cursor cursor = mContext.getContentResolver().query(ProductsEntry.DISPLAY_URI, columns, null, null, null);

        ProductsDisplayModel productsDisplayModel;

        Log.w(LOG_TAG, "The number of products fetched is " + String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                productsDisplayModel = new ProductsDisplayModel();
                productsDisplayModel.setId(i);
                productsDisplayModel.setProductId(cursor.getInt(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_ID)));
                productsDisplayModel.setProductName(cursor.getString(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_NAME)));
                productsDisplayModel.setPricePerUnit(cursor.getDouble(cursor.getColumnIndex(ProductsEntry.COLUMN_PRICE_PER_UNIT)));

                //---------------------------------------------------------------------------------------------

                ArrayList<ProductOffersModel> mProductOffersModelList = new ArrayList<ProductOffersModel>();

                String[] columns1 = {ProductOffersEntry.COLUMN_OFFER_ID, ProductOffersEntry.COLUMN_OFFER_TYPE_NAME,
                        ProductOffersEntry.COLUMN_OFFER_TYPE,ProductOffersEntry.COLUMN_PRODUCT_ID,
                        ProductOffersEntry.COLUMN_MINIMUM_ORDER_QUANTITY,
                        ProductOffersEntry.COLUMN_DISCOUNT_PERCENT, ProductOffersEntry.COLUMN_X_COUNT,
                        ProductOffersEntry.COLUMN_Y_COUNT, ProductOffersEntry.COLUMN_Y_NAME};

                String selection = ProductOffersEntry.COLUMN_PRODUCT_ID + " = " +
                        cursor.getInt(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_ID));

                Cursor cursor1 = mContext.getContentResolver().query(ProductOffersEntry.CHECK_URI, columns1, selection, null, null);

                ProductOffersModel productOffersModel;

                if (cursor1.getCount() > 0){
                    for (int j = 0; j < cursor1.getCount(); j = j + 1) {
                        cursor1.moveToNext();

                        productOffersModel = new ProductOffersModel();
                        productOffersModel.setId(j);
                        productOffersModel.setOfferId(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_ID)));
                        productOffersModel.setOfferDetails(cursor1.getString(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_TYPE_NAME)));
                        productOffersModel.setOfferType(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_TYPE)));
                        productOffersModel.setProductId(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_PRODUCT_ID)));
                        productOffersModel.setMinimumOrderQuantity(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_MINIMUM_ORDER_QUANTITY)));
                        productOffersModel.setDiscountPercent(cursor1.getDouble(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_DISCOUNT_PERCENT)));
                        productOffersModel.setxCount(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_X_COUNT)));
                        productOffersModel.setyCount(cursor1.getInt(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_Y_COUNT)));
                        productOffersModel.setyName(cursor1.getString(cursor1.getColumnIndex(ProductOffersEntry.COLUMN_Y_NAME)));

                        mProductOffersModelList.add(productOffersModel);
                    }
                }

                cursor1.close();

                //---------------------------------------------------------------------------------------------

                productsDisplayModel.setProductOffers(mProductOffersModelList);

                mProductDisplayModelList.add(productsDisplayModel);
            }
        }

        cursor.close();

        //Log.w(LOG_TAG, "The size of the list is " + mProductDisplayModelList.size());

        return mProductDisplayModelList;
    }

}

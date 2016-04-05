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

        String[] columns = {ProductsEntry.COLUMN_PRODUCT_NAME, ProductsEntry.COLUMN_PRICE_PER_UNIT};

        Cursor cursor = mContext.getContentResolver().query(ProductsEntry.DISPLAY_URI, columns, null, null, null);

        ProductsDisplayModel productsDisplayModel;

        Log.w(LOG_TAG, "The number of items fetched is " + String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                productsDisplayModel = new ProductsDisplayModel();
                productsDisplayModel.setId(i);
                productsDisplayModel.setProductName(cursor.getString(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_NAME)));
                productsDisplayModel.setPricePerUnit(cursor.getDouble(cursor.getColumnIndex(ProductsEntry.COLUMN_PRICE_PER_UNIT)));


                //Log.w(LOG_TAG, "i currently is " + i);

                mProductDisplayModelList.add(productsDisplayModel);
            }
        }

        cursor.close();

        //Log.w(LOG_TAG, "The size of the list is " + mProductDisplayModelList.size());

        return mProductDisplayModelList;
    }

}

package com.bazara2z.distributorhelper.OffersPackage;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.bazara2z.distributorhelper.Data.DistributorContract.*;


import java.util.ArrayList;
import java.util.List;


public class OffersFragment extends ListFragment {

    private final String LOG_TAG = "OffersFragment";

    private OnFragmentInteractionListener mListener;

    public static OffersFragment newInstance() {
        OffersFragment fragment = new OffersFragment();
        return fragment;
    }


    public OffersFragment() {
    }

    List<ProductOffersModel> productOffersModelList;
    ProductOffersAdapter productOffersAdapter;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productOffersModelList = getAllProductOffers();

        productOffersAdapter = new ProductOffersAdapter(getActivity(), productOffersModelList);

        this.setListAdapter(productOffersAdapter);

    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, null);
        return view;
    }
    */

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
        void onFragmentInteraction(String id);
    }

    public List<ProductOffersModel> getAllProductOffers(){
        ArrayList<ProductOffersModel> mProductOffersModelList = new ArrayList<ProductOffersModel>();

        Context mContext = getActivity().getApplicationContext();

        String[] columns = {ProductOffersEntry.COLUMN_OFFER_ID, ProductOffersEntry.COLUMN_OFFER_TYPE_NAME,
                ProductOffersEntry.COLUMN_OFFER_TYPE,ProductOffersEntry.TABLE_NAME + "." + ProductOffersEntry.COLUMN_PRODUCT_ID,
                ProductsEntry.COLUMN_PRODUCT_NAME, ProductOffersEntry.COLUMN_MINIMUM_ORDER_QUANTITY,
                ProductOffersEntry.COLUMN_DISCOUNT_PERCENT, ProductOffersEntry.COLUMN_X_COUNT,
                ProductOffersEntry.COLUMN_Y_COUNT, ProductOffersEntry.COLUMN_Y_NAME};

        Cursor cursor = mContext.getContentResolver().query(ProductOffersEntry.VIEW_WITH_PRODUCTS_URI, columns, null, null, null);

        ProductOffersModel productOffersModel;

        //Log.w(LOG_TAG, "The number of items fetched is " + String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                productOffersModel = new ProductOffersModel();
                productOffersModel.setId(i);
                productOffersModel.setOfferId(cursor.getInt(cursor.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_ID)));
                productOffersModel.setOfferDetails(cursor.getString(cursor.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_TYPE_NAME)));
                productOffersModel.setOfferType(cursor.getInt(cursor.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_TYPE)));
                productOffersModel.setProductId(cursor.getInt(cursor.getColumnIndex(ProductOffersEntry.COLUMN_PRODUCT_ID)));
                productOffersModel.setProductName(cursor.getString(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_NAME)));
                productOffersModel.setMinimumOrderQuantity(cursor.getInt(cursor.getColumnIndex(ProductOffersEntry.COLUMN_MINIMUM_ORDER_QUANTITY)));
                productOffersModel.setDiscountPercent(cursor.getDouble(cursor.getColumnIndex(ProductOffersEntry.COLUMN_DISCOUNT_PERCENT)));
                productOffersModel.setxCount(cursor.getInt(cursor.getColumnIndex(ProductOffersEntry.COLUMN_X_COUNT)));
                productOffersModel.setyCount(cursor.getInt(cursor.getColumnIndex(ProductOffersEntry.COLUMN_Y_COUNT)));
                productOffersModel.setyName(cursor.getString(cursor.getColumnIndex(ProductOffersEntry.COLUMN_Y_NAME)));

                mProductOffersModelList.add(productOffersModel);
            }
        }

        cursor.close();

        //Log.w(LOG_TAG, "The size of the list is " + mProductOffersModelList.size());

        return mProductOffersModelList;
    }

}

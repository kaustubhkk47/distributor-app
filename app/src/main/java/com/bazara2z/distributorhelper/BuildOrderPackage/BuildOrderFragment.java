package com.bazara2z.distributorhelper.BuildOrderPackage;

import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;


public class BuildOrderFragment extends ListFragment {

    private final String LOG_TAG = "BuildOrderFragment";

    private OnFragmentInteractionListener mListener;

    public static BuildOrderFragment newInstance() {
        List<BuildOrderModel> buildOrderModelList = new ArrayList<BuildOrderModel>();
        BuildOrderFragment fragment = new BuildOrderFragment(buildOrderModelList);
        return fragment;
    }


    public BuildOrderFragment(List<BuildOrderModel> buildOrderModelList) {
        this.buildOrderModelList = buildOrderModelList;
    }

    public BuildOrderFragment(){

    }

    List<BuildOrderModel> buildOrderModelList;
    BuildOrderAdapter buildOrderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //buildOrderModelList = getAllProducts();

        buildOrderAdapter = new BuildOrderAdapter(getActivity(), buildOrderModelList);

        this.setListAdapter(buildOrderAdapter);

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



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    /*

    public List<BuildOrderModel> getAllProducts(){
        ArrayList<BuildOrderModel> mBuildOrderModelList = new ArrayList<BuildOrderModel>();

        Context mContext = getActivity().getApplicationContext();

        String[] columns = {ProductsEntry.COLUMN_PRODUCT_NAME, ProductsEntry.COLUMN_PRICE_PER_UNIT, ProductOffersEntry.COLUMN_OFFER_TYPE_NAME,
                ProductsEntry.TABLE_NAME + "." + ProductsEntry.COLUMN_PRODUCT_ID};

        Cursor cursor = mContext.getContentResolver().query(ProductsEntry.DISPLAY_URI, columns, null, null, null);

        BuildOrderModel buildOrderModel;

        Log.w(LOG_TAG, "The number of items fetched is " + String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                buildOrderModel = new BuildOrderModel();

                buildOrderModel.setId(i);
                buildOrderModel.setProductName(cursor.getString(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_NAME)));
                buildOrderModel.setOfferDetails(cursor.getString(cursor.getColumnIndex(ProductOffersEntry.COLUMN_OFFER_TYPE_NAME)));
                buildOrderModel.setPricePerUnit(cursor.getDouble(cursor.getColumnIndex(ProductsEntry.COLUMN_PRICE_PER_UNIT)));
                buildOrderModel.setQuantity(0);
                buildOrderModel.setProductId(cursor.getInt(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_ID)));

                mBuildOrderModelList.add(buildOrderModel);
            }
        }

        cursor.close();


        return mBuildOrderModelList;
    }

    */

}

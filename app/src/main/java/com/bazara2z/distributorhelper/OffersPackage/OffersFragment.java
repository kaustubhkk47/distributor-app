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

    List<OffersModel> offersModelList;
    OffersAdapter offersAdapter;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        offersModelList = getAllOffers();

        offersAdapter = new OffersAdapter(getActivity(), offersModelList);

        this.setListAdapter(offersAdapter);

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

    public List<OffersModel> getAllOffers(){
        ArrayList<OffersModel> mOffersModelList = new ArrayList<OffersModel>();

        Context mContext = getActivity().getApplicationContext();

        String[] columns = {OffersEntry.COLUMN_OFFER_DETAILS, ProductsEntry.COLUMN_PRODUCT_NAME};

        Cursor cursor = mContext.getContentResolver().query(OffersEntry.VIEW_WITH_PRODUCTS_URI, columns, null, null, null);

        OffersModel offersModel;

        //Log.w(LOG_TAG, "The number of items fetched is " + String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                offersModel = new OffersModel();
                offersModel.setId(i);
                offersModel.setProductName(cursor.getString(cursor.getColumnIndex(ProductsEntry.COLUMN_PRODUCT_NAME)));
                offersModel.setOfferDetails(cursor.getString(cursor.getColumnIndex(OffersEntry.COLUMN_OFFER_DETAILS)));

                //Log.w(LOG_TAG, "i currently is " + i);

                mOffersModelList.add(offersModel);
            }
        }

        cursor.close();

        //Log.w(LOG_TAG, "The size of the list is " + mOffersModelList.size());

        return mOffersModelList;
    }

}

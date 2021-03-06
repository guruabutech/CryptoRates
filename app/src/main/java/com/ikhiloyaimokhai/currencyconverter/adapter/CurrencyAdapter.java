package com.ikhiloyaimokhai.currencyconverter.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikhiloyaimokhai.currencyconverter.R;
import com.ikhiloyaimokhai.currencyconverter.data.CurrencyContract;
import com.ikhiloyaimokhai.currencyconverter.utils.Helper;

/**
 * Created by Ikhiloya on 10/11/2017.
 * An adapter class used to display cards on the recycler view
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    //init variables
    private Context context;
    private Cursor cursor;
    private SparseBooleanArray mSelectedItemsIds;
    final private ListItemClickListener mOnclickListener;

    /**
     * an interface to handle click events on a card
     */
    public interface ListItemClickListener {
        void onListItemClick(String cryptoName, String currencyName, double currencyValue);
    }

    public CurrencyAdapter(Context context, Cursor cursor, ListItemClickListener mOnclickListener) {
        this.context = context;
        this.cursor = cursor;
        this.mOnclickListener = mOnclickListener;
        mSelectedItemsIds = new SparseBooleanArray();//
    }

    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.convert_unit, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrencyAdapter.ViewHolder holder, int position) {
        // Figure out the index of each column
        int idColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.USER_ID);
        int cryptoNameColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME);
        int currencyNameColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME);
        int currencyValueColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE);
        int timeColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_TIMESTAMP);

        // get to the right location in the cursor
        cursor.moveToPosition(position);

        // Use that index to extract the String or Int value of the word
        // at the current row the cursor is on.
        int currentID = cursor.getInt(idColumnIndex);
        String crypto_name = cursor.getString(cryptoNameColumnIndex);
        String currency_name = cursor.getString(currencyNameColumnIndex);
        double currency_val = cursor.getDouble(currencyValueColumnIndex);
        String currentTime = cursor.getString(timeColumnIndex);

        //set views
        holder.cryptoImage.setImageResource(Helper.getCryptoIcon(crypto_name));
        holder.cryptoName.setText(Helper.getCryptoName(crypto_name));
        holder.currencyVal.setText(String.valueOf(currency_val));
        holder.currencyUnit.setText(currency_name.toUpperCase());
        holder.currentTime.setText(currentTime);
        // Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(currentID);


        /** Change background color of the selected items in list view  **/
        holder.itemView
                .setBackgroundColor(mSelectedItemsIds.get(currentID) ? 0x9934B5E4
                        : Color.TRANSPARENT);
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (cursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = cursor;
        this.cursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView cryptoImage;
        private TextView cryptoName;
        private TextView currencyVal;
        private TextView currencyUnit;
        private TextView currentTime;


        public ViewHolder(View itemView) {
            super(itemView);

            cryptoImage = (ImageView) itemView.findViewById(R.id.crypto_image);
            cryptoName = (TextView) itemView.findViewById(R.id.crypto_name);
            currencyVal = (TextView) itemView.findViewById(R.id.currency_val);
            currencyUnit = (TextView) itemView.findViewById(R.id.currency_unit);
            currentTime = (TextView) itemView.findViewById(R.id.current_time);

            //set onClick listener on this view @link{itemView}
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.USER_ID);
            int cryptoNameColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CRYPTO_NAME);
            int currencyNameColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_NAME);
            int currencyValueColumnIndex = cursor.getColumnIndex(CurrencyContract.CurrencyEntry.COLUMN_CURRENCY_VALUE);
            int adapterPosition = getAdapterPosition();

            //move the cursor to the current position
            cursor.moveToPosition(adapterPosition);

            int currentID = cursor.getInt(idColumnIndex);
            String crypto_name = cursor.getString(cryptoNameColumnIndex);
            String currency_name = cursor.getString(currencyNameColumnIndex);
            double currency_val = cursor.getDouble(currencyValueColumnIndex);

            mOnclickListener.onListItemClick(crypto_name, currency_name, currency_val);

        }
    }

    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}

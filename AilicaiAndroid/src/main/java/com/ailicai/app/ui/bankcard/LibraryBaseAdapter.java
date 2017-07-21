package com.ailicai.app.ui.bankcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * name: LibrayBaseAdapter <BR>
 * description: List adapter的基类 <BR>
 * create date: 2015-7-13
 *
 * @author: IWJW Zhou Xuan
 */
public abstract class LibraryBaseAdapter<T> extends BaseAdapter {

    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public LibraryBaseAdapter(List<T> data, Context context) {
        mData = new ArrayList<T>();
        if (data != null) {
            for(T t:data) {
                if(t != null) {
                    mData.add(t);
                }
            }
//            mData.addAll(data);
        }
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getItem(int position) {
        if (mData == null) {
            return null;
        }
        return mData.get(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh(List<T> data) {
        if (data == null)
            return;
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void load(List<T> data) {
        if (data == null)
            return;
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }
}

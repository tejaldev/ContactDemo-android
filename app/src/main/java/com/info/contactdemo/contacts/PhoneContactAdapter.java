package com.info.contactdemo.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.info.contactdemo.R;


import java.util.List;

/**
 * Created by tejalpar on 2/15/16.
 */
public class PhoneContactAdapter extends BaseAdapter {

    List<PhoneContact> contactList;

    Context mContext;
    ViewHolder mViewHolder;

    public PhoneContactAdapter(List<PhoneContact> contactList, Context context) {
        this.contactList = contactList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if(contactList == null){
            return 0;
        }
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        if(contactList != null) {
            return contactList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(contactList != null) {
            return position;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            //create new view by inflating as we don't have recycled view

            //1. Inflate layout for each contact list item
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contacts_list_item, null);
        }
        else {
            //we have recycled view so no need to inflate utilize this view just change the content
            view = convertView;
        }

        //2. Create a viewHolder to organize view
        mViewHolder = new ViewHolder();

        //3. Get contact list item from layout and store ref in viewHolder
        mViewHolder.nameTextView = (TextView) view.findViewById(R.id.contactName);
        mViewHolder.picImageView = (ImageView) view.findViewById(R.id.contactPic);
        mViewHolder.phoneTextView = (TextView) view.findViewById(R.id.contactNumber);
        mViewHolder.nameInitialTextView = (TextView) view.findViewById(R.id.contactInitial);

        //4. Get element at position from contactList and load that data in view holder
        PhoneContact phoneContact = contactList.get(position);
        mViewHolder.phoneTextView.setText(phoneContact.getPhoneNumber());
        mViewHolder.nameTextView.setText(phoneContact.getName());
        mViewHolder.nameInitialTextView.setText(phoneContact.getNameInitial());

        try {
            if(phoneContact.getPhotoThumbnail() != null) {
                //uncomment below lines if you want to show thiumbnail photo of contacts in the list
//                mViewHolder.nameInitialTextView.setText("");
//                mViewHolder.picImageView.setImageBitmap(phoneContact.getPhotoThumbnail());
            }
            else {
                mViewHolder.picImageView.setImageBitmap(null);
                mViewHolder.picImageView.setBackgroundColor(phoneContact.getDefaultPhotoBgColor());
            }
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        //5. set the specific contact data as tag of view
        view.setTag(phoneContact);
        return view;
    }

    static class ViewHolder {
        ImageView picImageView;
        TextView nameTextView;
        TextView phoneTextView;
        TextView nameInitialTextView;
    }
}

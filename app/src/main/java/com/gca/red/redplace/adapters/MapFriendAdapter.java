package com.gca.red.redplace.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gca.red.redplace.R;
import com.gca.red.redplace.objects.Friend;
import com.gca.red.redplace.objects.Me;
import com.gca.red.redplace.utils.ImageLoaderUtil;

/**
 * Created by red2 on 2017/2/20.
 */

public class MapFriendAdapter extends BaseAdapter{

    static class ViewHolder
    {
        public ImageView picture;
        public TextView name;
        public TextView distant;
    }

    private LayoutInflater layoutInflater;
    private Context context;

    public MapFriendAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Me.getInstance().getFriends().size();
    }

    @Override
    public Object getItem(int position) {
        return Me.getInstance().getFriends().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Friend friend = Me.getInstance().getFriends().get(position);
        if (convertView == null) {
            convertView =  layoutInflater.inflate(R.layout.item_map_friend, null);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.distant = (TextView)convertView.findViewById(R.id.distant);
            holder.picture = (ImageView)convertView.findViewById(R.id.picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(friend.getName());
        ImageLoaderUtil.display(context, holder.picture, friend.getPhotoUrl());
        return convertView;
    }
}

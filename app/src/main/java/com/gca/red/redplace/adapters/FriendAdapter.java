package com.gca.red.redplace.adapters;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gca.red.redplace.R;
import com.gca.red.redplace.fragments.FriendFragment;
import com.gca.red.redplace.objects.Friend;
import com.gca.red.redplace.objects.Me;
import com.gca.red.redplace.utils.ImageLoaderUtil;

/**
 * Created by red2 on 2017/2/19.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View view;
        TextView nameText;
        ImageView picture;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            nameText = (TextView) view.findViewById(R.id.name);
            picture = (ImageView) view.findViewById(R.id.picture);
        }
    }

    public FriendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        FriendAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Friend friend = Me.getInstance().getFriends().get(position);
            if (friend == null) return;
            holder.nameText.setText(friend.getName());
            ImageLoaderUtil.display(context, holder.picture, friend.getPhotoUrl());
        }
    }

    @Override
    public int getItemCount() {
        return Me.getInstance().getFriends().size();
    }


}

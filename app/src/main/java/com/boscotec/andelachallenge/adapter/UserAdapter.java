package com.boscotec.andelachallenge.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boscotec.andelachallenge.ProfileActivity;
import com.boscotec.andelachallenge.R;
import com.boscotec.andelachallenge.model.UserDetail;
import com.boscotec.andelachallenge.utility.CircleImageView;
import com.boscotec.andelachallenge.utility.CircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnbosco on 16-Jul-17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {

    private Context context;
    private List<UserDetail> items = new ArrayList();
    private List<UserDetail> filterItems = new ArrayList();
    private ItemFilter itemFilter = new ItemFilter();


    public UserAdapter(Context conxt, List<UserDetail> items)
    {
        this.context = conxt;
        this.items = items;
        this.filterItems = items;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);
        return  new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserDetail git = this.filterItems.get(position);
        holder.name.setText(git.getLogin());
        Glide.with(context).load(git.getAvatarUrl())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_person_white_36dp)
                .into(holder.image);
    }

    @Override
    public int getItemCount() { return this.filterItems.size();}

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter{
     private ItemFilter(){}

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            List<String> list = new ArrayList();
            for(int i=0; i<items.size(); i++)
                list.add(UserAdapter.this.items.get(i).getLogin());

            List<UserDetail> result_list = new ArrayList(list.size());
            for(int i=0; i<list.size(); i++){
                if(list.get(i).toLowerCase().contains(query))
                    result_list.add(UserAdapter.this.items.get(i));
            }

            results.values = result_list;
            results.count = result_list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            UserAdapter.this.filterItems = (List) results.values;
            UserAdapter.this.notifyDataSetChanged();
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView image;
        TextView name;

        public UserViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.avater);
            name = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            UserDetail user = filterItems.get(getLayoutPosition());

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("profile_pix", user.getAvatarUrl());
            intent.putExtra("username", user.getLogin());
            intent.putExtra("profile_url", user.getUrl());
            intent.putExtra("html_url", user.getHtmlUrl());
            context.startActivity(intent);
        }
    }

}

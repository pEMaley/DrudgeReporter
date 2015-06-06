
package com.androidbegin.jsouptutorial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Provides the adapter construction to properly display images, links and titles of news articles.
 */
public class listViewAdapter extends BaseAdapter{
    Context context;
    int resource;
    ArrayList<Item> data = null;

    public listViewAdapter(Context context, int resource, ArrayList<Item> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public Item getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position){
        return data.indexOf(getItem(position));
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ItemHolder holder;

       if (convertView == null) {
           LayoutInflater inflater = ((Activity) context).getLayoutInflater();
           convertView = inflater.inflate(R.layout.simple_list_item_1, parent, false);
           holder = new ItemHolder();
           holder.image = (ImageView) convertView.findViewById(R.id.image);
           holder.article_name = (TextView) convertView.findViewById(R.id.article_name);
           convertView.setTag(holder);
       } else {
           holder = (ItemHolder) convertView.getTag();
       }

       Item item = getItem(position);
       if(item.fontRed()) {
           holder.article_name.setTextColor(Color.rgb(255, 65, 54));
           holder.article_name.setText(item.getName());

       }else {
           holder.article_name.setTextColor(Color.rgb(17,17,17));
           holder.article_name.setText(item.getName());
       }
       Picasso.with(this.context).load(item.getImg()).into(holder.image);
        return convertView;
   }

    private static class ItemHolder
    {
        ImageView image;
        TextView article_name;
    }
}

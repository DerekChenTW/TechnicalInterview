package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainCartActivity extends AppCompatActivity {

    ListView cartListView;
    private String userName ;
    private List<String> itemList = new ArrayList<>();
    private List<String> sortList = new ArrayList<>();
    private HashMap<String, Integer> itemAmountMap = new HashMap<>();
    private List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cart);

        Resources res = getResources();
        items = Arrays.asList(res.getStringArray(R.array.items));

        if (getIntent().hasExtra("userName")){
            TextView cartUserNameTV = (TextView) findViewById(R.id.cartUserName);
            userName = getIntent().getExtras().getString("userName");
            cartUserNameTV.setText(userName);
        }

        if(getIntent().hasExtra("itemList")){
            itemList = getIntent().getExtras().getStringArrayList("itemList");
        }

        cartListView = (ListView) findViewById(R.id.cartListView);
        if (itemList != null) {
            for (String item : itemList){
                if(sortList.contains(item) && itemAmountMap.containsKey(item)){
                    int amount = itemAmountMap.get(item);
                    itemAmountMap.put(item, amount+1);
                }
                else{ sortList.add(item);
                    itemAmountMap.put(item,1); }
                }
            cartListView.setAdapter(new MainCartActivity.CartListAdapter(this, R.layout.user_cart_layout, sortList));
        }
    }

    private class CartListAdapter extends ArrayAdapter<String> {
        private int layout;
        private CartListAdapter (Context context, int resource, List<String> objects ){
            super(context,resource,objects);
            this.layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            MainCartActivity.ViewHolder mainViewHolder;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                MainCartActivity.ViewHolder viewHolder = new MainCartActivity.ViewHolder();
                viewHolder.itemNameTV = (TextView) convertView.findViewById(R.id.cartitemNameTV);
                viewHolder.itemIdTV = (TextView) convertView.findViewById(R.id.cartIdTV);
                viewHolder.itemAmountTV = (TextView) convertView.findViewById(R.id.cartAmountTV);

                viewHolder.itemNameTV.setText(sortList.get(position));
                viewHolder.itemIdTV.setText( String.valueOf(items.indexOf(sortList.get(position))+1));
                if(itemAmountMap != null ) {
                    viewHolder.itemAmountTV.setText(String.valueOf(itemAmountMap.get(sortList.get(position))));
                }
                convertView.setTag(viewHolder);
            }
            else {
                mainViewHolder = (MainCartActivity.ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    public class ViewHolder {
        TextView itemNameTV;
        TextView itemIdTV;
        TextView itemAmountTV;
    }
}

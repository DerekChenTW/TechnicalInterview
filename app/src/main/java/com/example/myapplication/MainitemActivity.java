package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainitemActivity extends AppCompatActivity {

    ListView itemListView;
    String[] items;
    private String userName;
    private HashMap<String, List<String>> userItemMap = new HashMap<>();
    private ItemListAdapter itemListAdapter;
    private Intent itemIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainitem);

        itemIntent = new Intent(MainitemActivity.this, MainActivity.class);

        Resources res = getResources();
        items = res.getStringArray(R.array.items);

        itemListView = (ListView) findViewById(R.id.itemListView);
        itemListAdapter =  new MainitemActivity.ItemListAdapter(this,R.layout.item_list_layout, items);
        itemListView.setAdapter(itemListAdapter);

        ImageButton itemAcativityBtn = (ImageButton) findViewById(R.id.userIB);

        if (getIntent().hasExtra("userName")){
            userName = getIntent().getExtras().getString("userName");
        }

        itemAcativityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userItemMap = itemListAdapter.getMap();
                itemIntent.putExtra("userItemMap", userItemMap);
                startActivity(itemIntent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        userItemMap = itemListAdapter.getMap();
        itemIntent.putExtra("userItemMap", userItemMap);
        startActivity(itemIntent);
        finish();
    }

    private class ItemListAdapter extends ArrayAdapter<String> {
        private int layout;
        private ItemListAdapter (Context context, int resource, String[] objects ){
            super(context,resource,objects);
            this.layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            MainitemActivity.ViewHolder mainViewHolder;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                MainitemActivity.ViewHolder viewHolder = new MainitemActivity.ViewHolder();
                viewHolder.additemIB = (ImageButton) convertView.findViewById(R.id.addIB);
                viewHolder.itemNameTV = (TextView) convertView.findViewById(R.id.itemNameTV);
                viewHolder.itemIdTV = (TextView) convertView.findViewById(R.id.itemIdTV);

                viewHolder.itemNameTV.setText(items[position]);
                viewHolder.itemIdTV.setText( String.valueOf(position + 1));

                viewHolder.additemIB.setOnClickListener(new View.OnClickListener() {
                    List<String> itemList = new ArrayList<>();
                    @Override
                    public void onClick(View v) {
                        Intent userIntent =  new Intent(getApplicationContext(), MainActivity.class);
                        userIntent.putExtra("addItem",items[position]);
                        if (userItemMap.containsKey(userName)){
                            userItemMap.get(userName).add(items[position]);
                        }
                        else {
                            itemList.add(items[position]);
                            userItemMap.put(userName, itemList);
                        }
                        Intent intent = new Intent(MainitemActivity.this, MainActivity.class);
                        intent.putExtra("userItemMap", userItemMap);
                    }
                });
                convertView.setTag(viewHolder);
            }
            else {
                mainViewHolder = (MainitemActivity.ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        public HashMap<String, List<String>> getMap(){
            return userItemMap;
        }
    }

    public class ViewHolder {
        ImageButton additemIB;
        TextView itemNameTV;
        TextView itemIdTV;
    }
}

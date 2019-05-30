package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView userListView;
    String[] users;
    String[] items;

    private  String NowUser;
    private HashMap<String, List<String>> userItemMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();

        users = res.getStringArray(R.array.users);
        items = res.getStringArray(R.array.items);

        userListView = (ListView) findViewById(R.id.userListView);
        userListView.setAdapter(new UserListAdapter(this,R.layout.user_layout, users));

        ImageButton itemAcativityBtn = (ImageButton) findViewById(R.id.itemIB);

        if (getIntent().hasExtra("addItem")){
            if(this.userItemMap.containsKey(NowUser)) {
                this.userItemMap.get(NowUser).add(getIntent().getExtras().getString("addItem"));
            }
            else {
                List<String> itemList = new ArrayList<>();
                itemList.add(getIntent().getExtras().getString("addItem"));
                userItemMap.put(NowUser, itemList);
            }
        }
        if(getIntent().hasExtra("userItemMap")) {
            userItemMap = (HashMap<String, List<String>>) getIntent().getExtras().getSerializable("userItemMap");
        }
        Intent intent = new Intent(MainActivity.this, MainCartActivity.class);
        intent.putExtra("userItemMap", userItemMap);

        itemAcativityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemIntent = new Intent(getApplicationContext(), MainitemActivity.class);
                startActivity(itemIntent);
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "hi",Toast.LENGTH_SHORT).show();
                Intent itemIntent = new Intent(MainActivity.this, MainitemActivity.class);
                itemIntent.putExtra("userName",users[position]);
                NowUser = users[position];
                startActivity(itemIntent);
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class UserListAdapter extends ArrayAdapter<String>{
        private int layout;
        private UserListAdapter (Context context, int resource, String[] objects ){
            super(context,resource,objects);
            this.layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.cartIB = (ImageButton) convertView.findViewById(R.id.cartIB);
                viewHolder.itemIB = (ImageButton) convertView.findViewById(R.id.additemIB);
                viewHolder.userNameTV = (TextView) convertView.findViewById(R.id.userNameTV);
                viewHolder.userIdTV = (TextView) convertView.findViewById(R.id.userIdTV);

                viewHolder.userNameTV.setText(users[position]);
                viewHolder.userIdTV.setText( String.valueOf(position + 1));

                viewHolder.itemIB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent itemIntent = new Intent(MainActivity.this, MainitemActivity.class);
                        itemIntent.putExtra("userName",users[position]);
                        NowUser = users[position];
                        startActivity(itemIntent);
                    }
                });

                viewHolder.cartIB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cartIntent = new Intent(MainActivity.this, MainCartActivity.class);
                        cartIntent.putExtra("userName",users[position]);
                        cartIntent.putExtra("userItemMap", userItemMap);
                        cartIntent.putExtra("itemList", (Serializable) userItemMap.get(users[position]));
                        startActivity(cartIntent);
                    }
                });

                convertView.setTag(viewHolder);

            }
            else {
                mainViewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    public class ViewHolder {
        ImageButton cartIB;
        ImageButton itemIB;
        TextView userNameTV;
        TextView userIdTV;
    }
}

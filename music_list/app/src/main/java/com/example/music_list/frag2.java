package com.example.music_list;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class frag2 extends Fragment {
    private View view2;
    public String[] name2 = {"-- Naruto --","-- Attack on titan --","-- Violet Evergarden --","-- 宮崎駿音樂 --",
            "-- 2001-2010華語串燒 --","-- 80后金曲 --","-- 仙劍串燒 --","-- 無職轉升op串燒 --"};
    public static int[] icons2 = {R.drawable.naruto,R.drawable.alenmikasa,R.drawable.violetevergarden,
            R.drawable.chishu,R.drawable.laststar,R.drawable.laststar,R.drawable.godsword,R.drawable.nojob};
    @Override
    public View onCreateView(final LayoutInflater inflater2, ViewGroup container, Bundle savedInstanceState){
        view2 = inflater2.inflate(R.layout.music_list2,null);
        ListView listView = view2.findViewById(R.id.lv2);
        frag2.MyBaseAdapter2 adapter = new frag2.MyBaseAdapter2();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(frag2.this.getContext(),MusicActivity2.class);
                intent.putExtra("name",name2[position]);
                intent.putExtra("position",String.valueOf(position));
                startActivity(intent);
            }
        });
        return view2;
    }
    class MyBaseAdapter2 extends BaseAdapter {
        @Override
        public int getCount(){return name2.length;}
        @Override
        public Object getItem(int i){return name2[i];}
        @Override
        public long getItemId(int i){return i;}

        @Override
        public View getView(int i,View convertView,ViewGroup parent){
            View view = View.inflate(frag2.this.getContext(),R.layout.item_layout2,null);
            TextView tv_name = view.findViewById(R.id.item_name2);
            ImageView iv = view.findViewById(R.id.iv2);
            tv_name.setText(name2[i]);
            iv.setImageResource(icons2[i]);
            return view;
        }
    }
}

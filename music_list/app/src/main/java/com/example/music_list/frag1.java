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

public class frag1 extends Fragment {
    private View view;

    public String[] name = {"Aimee - release my soul","True - will","幾田りら - lilas","日笠陽子 - この美しき残酷な世界",
    "Hikari - Punishing Gray Raven 決戰曲","Unknown - 她的夢","大原ゆい子 - 風と行く道","澤野弘之 - Counter Attack Mankind"
    ,"蔡健雅 - 紫","美波 - この街に晴れはこない","Aimer - 季路","Unknown - Last Stardust","amazarashi - 境界線"};
    public static int[] icons = {R.drawable.inori,R.drawable.violet,R.drawable.mirige,R.drawable.mikasa,R.drawable.lucia
            ,R.drawable.lefu,R.drawable.roxy,R.drawable.alenmikasa,R.drawable.wucoan,R.drawable.norainy,R.drawable.chinastyle
    ,R.drawable.laststar,R.drawable.mirige2};
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.music_list,null);
        ListView listView = view.findViewById(R.id.lv);
        MyBaseAdapter adapter = new MyBaseAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(frag1.this.getContext(),MusicActivity.class);
                intent.putExtra("name",name[position]);
                intent.putExtra("position",String.valueOf(position));
                startActivity(intent);
            }
        });
        return view;
    }
    class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount(){return name.length;}
        @Override
        public Object getItem(int i){return name[i];}
        @Override
        public long getItemId(int i){return i;}

        @Override
        public View getView(int i,View convertView,ViewGroup parent){
            View view = View.inflate(frag1.this.getContext(),R.layout.item_layout,null);
            TextView tv_name = view.findViewById(R.id.item_name);
            ImageView iv = view.findViewById(R.id.iv);
            tv_name.setText(name[i]);
            iv.setImageResource(icons[i]);
            return view;
        }
    }
}

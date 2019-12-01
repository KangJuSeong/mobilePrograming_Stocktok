package com.example.mobileprograming_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Item> mDataset = new ArrayList<>();
    private Context mContext;
    private String userID;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {
        public TextView i_name;
        public TextView i_size;
        public TextView i_date;
        public TextView i_remark;
        public MyViewHolder(View v) {
            super(v);
            i_name=v.findViewById(R.id.i_name);
            i_size=v.findViewById(R.id.i_size);
            i_date=v.findViewById(R.id.i_date);
            i_remark=v.findViewById(R.id.i_remark);
            v.setOnCreateContextMenuListener(this);
        }
        // 각각의 메뉴의 대한 내용을 표시해줌
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "상세정보");
            MenuItem BuyItem = menu.add(Menu.NONE, 1002, 2, "바로구매");
            MenuItem Delete = menu.add(Menu.NONE, 1003, 3, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            BuyItem.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }
        // 길게 눌렀을 때 나오는 메뉴에 따른 버튼을 클릭했을 때 실행됨
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:
                        String postion=Integer.toString(getAdapterPosition());
                        Intent intent=new Intent(mContext,ModifyWindow.class);
                        intent.putExtra("NAME",mDataset.get(getAdapterPosition()).NAME);
                        intent.putExtra("DATE",mDataset.get(getAdapterPosition()).DATE);
                        intent.putExtra("LINK",mDataset.get(getAdapterPosition()).LINK);
                        intent.putExtra("SIZE",mDataset.get(getAdapterPosition()).SIZE);
                        intent.putExtra("REMARK",mDataset.get(getAdapterPosition()).REMARK);
                        intent.putExtra("POSITION",postion);
                        intent.putExtra("userID",userID);
                        ((Activity) mContext).startActivityForResult(intent, 1);
                        break;
                    case 1002:
                        Intent uri = new Intent(Intent.ACTION_VIEW,Uri.parse("http://"+mDataset.get(getAdapterPosition()).LINK));
                        ((Activity) mContext).startActivities(new Intent[]{uri});
                    case 1003:
                        firebase db=new firebase();
                        db.dbDelete(userID);
                        mDataset.remove(getAdapterPosition());
                        for(int i=0;i<mDataset.size();i++){
                            String pos=Integer.toString(i);
                            Item item3 = new Item(mDataset.get(i).NAME,mDataset.get(i).DATE,mDataset.get(i).SIZE,mDataset.get(i).LINK,mDataset.get(i).REMARK);
                            db.dbWrite(item3,pos,userID);
                        }
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mDataset.size());
                        break;
                }
                return true;
            }
        };
    };

    // 데이터를 받아온다 배열로
    public MyAdapter(Context mContext,ArrayList<Item> myDataset,String userID) {
        mDataset=myDataset;
        this.mContext=mContext;
        this.userID=userID;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    // Item에 데이터를 표시해줌
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.i_date.setText(mDataset.get(position).DATE);
        holder.i_name.setText(mDataset.get(position).NAME);
        holder.i_size.setText(mDataset.get(position).SIZE);
        holder.i_remark.setText(mDataset.get(position).REMARK);
    }
    @Override
    public int getItemCount() {return mDataset.size();}

    public void addItem(Item item) {
        mDataset.add(item);
        notifyItemInserted(mDataset.size()-1); //갱신
    }

    public void changeItem(Item item,int index) {
        mDataset.set(index,item);
        notifyItemChanged(index);
    }
}

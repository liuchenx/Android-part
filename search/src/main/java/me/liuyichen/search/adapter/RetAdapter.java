package me.liuyichen.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.liuyichen.search.model.Item;

/**
 * Created by liuchen on 15/12/5.
 * and ...
 */
public class RetAdapter extends BaseAdapter {

    private final Context context;

    private List<Item> list = new ArrayList<>();

    private LayoutInflater inflater;
    public RetAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<Item> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {

        this.list.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        android.util.Log.e("111", "position: " + position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            viewHolder.text = (TextView)convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.text.setText(list.get(position).getTitle());

        return convertView;
    }

    static class ViewHolder {

        public TextView text;
    }
}

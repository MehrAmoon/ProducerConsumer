package sample.amoon.app.producerconsumerthreading;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by M.Amoon on 7/8/2019.
 * create a custon adapter for listview
 */
class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<CustomModel> itemModelList;


    public CustomAdapter(Context context, ArrayList<CustomModel> modelList) {
        this.context = context;
        this.itemModelList = modelList;
    }
    @Override
    public int getCount() {
        return itemModelList.size();
    }
    @Override
    public Object getItem(int position) {
        return itemModelList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_items, null);
            TextView Number = (TextView) convertView.findViewById(R.id.Number);
            CustomModel m = itemModelList.get(position);
            Number.setText(m.getNumber());
        }
        return convertView;
    }

}



package com.tools.payhelper.pay.ui.accountchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.tools.payhelper.R;

import java.util.List;

public class DetailListAdapter extends BaseAdapter {

    private Context context;
    private List<AccountChange.Data> billInfo;
    private LayoutInflater inflater;

    public DetailListAdapter(Context context, List<AccountChange.Data> billInfo)
    {
        this.context = context;
        this.billInfo = billInfo;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return billInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return billInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DetailItemHolder detailItemHolder = null;


        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.detail, parent, false);
            detailItemHolder = new DetailItemHolder(convertView);
            convertView.setTag(detailItemHolder);

        }else{
            detailItemHolder = (DetailItemHolder)convertView.getTag();
        }


        detailItemHolder.init(billInfo.get(position));

        return detailItemHolder.convertView;
    }
}

package com.tools.payhelper.pay.ui.buyrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.tools.payhelper.R;
import com.tools.payhelper.pay.ui.accountchange.AccountChange;

import java.util.List;

public class BuyRecordListAdapter extends BaseAdapter {

    private Context context;
    private List<AccountChange.Data> billInfo;
    private LayoutInflater inflater;

    public BuyRecordListAdapter(Context context, List<AccountChange.Data> billInfo)
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
        BuyRecordItemHolder detailItemHolder = null;


        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.detail, parent, false);
            detailItemHolder = new BuyRecordItemHolder(convertView);
            convertView.setTag(detailItemHolder);

        }else{
            detailItemHolder = (BuyRecordItemHolder)convertView.getTag();
        }


        detailItemHolder.init(billInfo.get(position));

        return detailItemHolder.convertView;
    }
}

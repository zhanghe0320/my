package com.onesmock.Util.productList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onesmock.R;

import java.util.List;

/**
 * Created by peter on 18/8/7.
 */

public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<ImageInfo> imageInfoList;

    public GridAdapter(Context context, List<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            view = View.inflate(context, R.layout.gridview_item, null);
            holder = new ViewHolder();
            holder.imageView = view.findViewById(R.id.img_appIcon);
            holder.textView = view.findViewById(R.id.tv_appName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ImageInfo imageInfo = imageInfoList.get(i);
        holder.textView.setText(imageInfo.getText());

        if (imageInfo.getBitmap() == null) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            holder.imageView.setImageBitmap(imageInfo.getBitmap());
        }
        return view;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}

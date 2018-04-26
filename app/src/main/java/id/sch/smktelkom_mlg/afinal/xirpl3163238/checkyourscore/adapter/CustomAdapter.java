package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

/**
 * Created by Sakata Yoga on 12/04/2018.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    int[] icons;
    LayoutInflater layoutInflater;

    public CustomAdapter(Context context, int[] icons) {
        this.context = context;
        this.icons = icons;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.custom_spinner_item, null);
        ImageView ivIcon = view.findViewById(R.id.ivSpinner);
        ivIcon.setImageResource(icons[i]);
        return view;
    }
}

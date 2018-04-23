package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sadaa on 4/23/2018.
 */

public class SliderAdapter extends PagerAdapter {

    public int[] slide_images = {
            R.drawable.eat_icon,
            R.drawable.gambar2,
            R.drawable.gambar3
    };
    public String[] slide_headings = {
            "HAI",
            "HALLO",
            "HI"
    };
    public String[] slide_descs = {
            "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmbcxgddudhfffosnsskxnddossnxmxdudnxjx",
            "hjklzxcvbnmbqwertyuiopasasdfgdfghjklzxcvhfffosnsskxnddossnxmxdudnbnmqwertyuiopcxgddudxjx",
            "cvbnmqwertyuiopasdfgasdfghjklzxgddudhfffosnsskxnhjklzxcvbnmbcxqwertyuiopddossnxmxdudnxjx"
    };
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout) object);
    }
}

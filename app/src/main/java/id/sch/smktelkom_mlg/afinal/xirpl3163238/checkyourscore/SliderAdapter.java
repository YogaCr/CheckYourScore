package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

    public int[] slideViewPagers = {
            R.color.Slide1,
            R.color.Slide2,
            R.color.Slide3
    };
    public int[] slide_images = {
            R.drawable.icon_logo,
            R.drawable.data,
            R.drawable.statistik
    };
    public String[] slide_headings = {
            "SELAMAT DATANG",
            "",
            ""
    };
    public String[] slide_descs = {
            "Check Your Score adalah aplikasi yang digunakan untuk memeriksa dan melihat perkembangan nilai di setiap mata pelajaran",
            "Melihat nilai yang telah diupdate oleh guru anda",
            "Dapat memantai perkembangan nilai melalui statistik"
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

        ViewPager vpSlideViewPager = view.findViewById(R.id.slideViewPager);
        ImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_desc);


//        vpSlideViewPager.setAdapter(slideViewPagers[position]);
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

package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * A simple {@link Fragment} subclass.
 */
public class Statistik extends Fragment {
    GraphView graphView;

    public Statistik() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistik, container, false);
        graphView = v.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 90),
                new DataPoint(1, 100),
                new DataPoint(2, 80),
                new DataPoint(3, 85),
                new DataPoint(4, 95)
        });
        graphView.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[]{
                "Himpunan", "Limit", "Gradien", "Bunga", "PLTV"
        });
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        return v;
    }

}

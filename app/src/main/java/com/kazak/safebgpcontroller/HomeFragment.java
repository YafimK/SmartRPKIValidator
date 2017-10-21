package com.kazak.safebgpcontroller;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements NetworkUpdatableView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    PieChart roaStatusPieChart;
    PieChart bgpStatusPieChart;
    BarChart roaIssuesBarChart;
    LineChart roaIssueTimelineChart;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        roaStatusPieChart = (PieChart) view.findViewById(R.id.roaPieChart);
        bgpStatusPieChart = (PieChart) view.findViewById(R.id.bgpPieChart);
        roaIssuesBarChart = (BarChart) view.findViewById(R.id.roaIssueBarChart);
        roaIssueTimelineChart = (LineChart) view.findViewById(R.id.roaIssuesTimelineLineChart);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.fetchData(this, "Home", "/getCurrentStatus");
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void updateViewOnResponse(JSONObject jsonResponse) {


        List<PieEntry> roa_status_enteries = new ArrayList<>();
        List<PieEntry> bgp_status_enteries = new ArrayList<>();
        List<BarEntry> roa_issues_enteries = new ArrayList<>();
        List<Entry> roa_issues_timeline_enteries = new ArrayList<>();
        try {
            JSONObject roaStatus = jsonResponse.getJSONObject("roa_status");
            JSONObject bgpStatus = jsonResponse.getJSONObject("bgp_status");
            JSONObject roaIssuesStatus = jsonResponse.getJSONObject("roa_issue_status");
            JSONObject roaIssuesTimeline = jsonResponse.getJSONObject("roa_issue_timeline");

            Iterator<String> roaStatusIter = roaStatus.keys();
            Iterator<String> bgpStatusIter = bgpStatus.keys();
            Iterator<String> roaIssuesIter = roaIssuesStatus.keys();
            Iterator<String> roaIssuesTimelineIter = roaIssuesTimeline.keys();


            while (roaStatusIter.hasNext()) {
                String currentCatagory = roaStatusIter.next();
                roa_status_enteries.add(new PieEntry(roaStatus.getInt(currentCatagory), currentCatagory));
            }
            PieDataSet roaStatusChartDataset = new PieDataSet(roa_status_enteries, "Roa issues"); // add entries to dataset
            roaStatusChartDataset.setColors(ColorTemplate.JOYFUL_COLORS);
            PieData roaStatusChartData = new PieData(roaStatusChartDataset);
            roaStatusPieChart.setCenterTextColor(ColorTemplate.rgb("000"));
            roaStatusPieChart.setEntryLabelColor(ColorTemplate.rgb("000"));
            roaStatusPieChart.setData(roaStatusChartData);
            roaStatusPieChart.invalidate(); // refresh

            while (bgpStatusIter.hasNext()) {
                String currentCatagory = bgpStatusIter.next();
                bgp_status_enteries.add(new PieEntry(bgpStatus.getInt(currentCatagory), currentCatagory));
            }
            PieDataSet bgpStatusChartDataset = new PieDataSet(bgp_status_enteries, "Bgp issues"); // add entries to dataset
            bgpStatusChartDataset.setColors(ColorTemplate.JOYFUL_COLORS);
            bgpStatusPieChart.setEntryLabelColor(ColorTemplate.rgb("000"));

            PieData bgpStatusChartData = new PieData(bgpStatusChartDataset);
            bgpStatusPieChart.setData(bgpStatusChartData);

            bgpStatusPieChart.invalidate(); // refresh

            final List<String> roaIssuesTypes = new ArrayList<>();
            for (int counter = 0; roaIssuesIter.hasNext(); counter++) {
                String currentCatagory = roaIssuesIter.next();
                roaIssuesTypes.add(currentCatagory);
                roa_issues_enteries.add(new BarEntry(counter, roaIssuesStatus.getInt(currentCatagory)));
            }

            BarDataSet roaIssuesChartDataset = new BarDataSet(roa_issues_enteries, "roa issues distribution"); // add entries to dataset
            BarData roaIssuesChartData = new BarData(roaIssuesChartDataset);
            roaIssuesChartDataset.setColors(ColorTemplate.JOYFUL_COLORS);
            roaIssuesBarChart.setData(roaIssuesChartData);
            IAxisValueFormatter formatter = new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return (roaIssuesTypes.get((int) value));
                }

                // we don't draw numbers, so no decimal digits needed
                public int getDecimalDigits() {  return 0; }
            };

            XAxis xAxis = roaIssuesBarChart.getXAxis();
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);
            xAxis.setTextColor(ColorTemplate.rgb("000"));
            roaIssuesBarChart.invalidate(); // refresh


            final List<String> roaIssuesTimelineLabels = new ArrayList<>();
            for (int counter = 0; roaIssuesTimelineIter.hasNext(); counter++) {
                String currentCatagory = roaIssuesTimelineIter.next();
                roaIssuesTimelineLabels.add(currentCatagory);
                roa_issues_timeline_enteries.add(new Entry(counter, roaIssuesTimeline.getInt(currentCatagory)));
            }
            LineDataSet roaIssuesTimelineChartDataSet = new LineDataSet(roa_issues_timeline_enteries, "roa issues distribution"); // add entries to dataset
            roaIssuesTimelineChartDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            LineData roaIssuesTimelineChartData = new LineData(roaIssuesTimelineChartDataSet);
            roaIssueTimelineChart.setData(roaIssuesTimelineChartData);

            IAxisValueFormatter roaIssueTimelineChartFormatter = new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return roaIssuesTimelineLabels.get((int) value);
                }

                // we don't draw numbers, so no decimal digits needed

                public int getDecimalDigits() {  return 0; }
            };

            XAxis roaIssueTimelineXAxis = roaIssueTimelineChart.getXAxis();
            roaIssueTimelineXAxis.setTextColor(ColorTemplate.rgb("000"));

            roaIssueTimelineXAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            roaIssueTimelineXAxis.setValueFormatter(roaIssueTimelineChartFormatter);

            roaIssueTimelineChart.invalidate(); // refresh

        } catch (Exception e) {
            Log.e("updateViewOnResponse", e.toString());
        }


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void fetchData(NetworkUpdatableView view, String requestSourceFragment, String requestPath);

        void cancelPendingRequests(String fragName);
    }
}

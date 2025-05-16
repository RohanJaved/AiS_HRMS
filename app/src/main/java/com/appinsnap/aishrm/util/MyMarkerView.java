package com.appinsnap.aishrm.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.appinsnap.aishrm.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


import java.text.DecimalFormat;

import kotlin.io.ConstantsKt;

@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView tvContent;
    private DecimalFormat mFormat;

    String[] listlabels;
    String[] listlabelsComobine;


    public MyMarkerView(Context context,String[] listlabels,int layoutResource) {
        super(context, layoutResource);
        mFormat = new DecimalFormat("###,###,###,###");
        this.listlabels = listlabels;
        tvContent = findViewById(R.id.tvContent);
    }
    public MyMarkerView(Context context,String[] listlabels,String[] listlabelsComobine,int layoutResource) {
        super(context, layoutResource);
        mFormat = new DecimalFormat("###,###,###,###");
        this.listlabels = listlabels;
        this.listlabelsComobine = listlabelsComobine;
        tvContent = findViewById(R.id.tvContent);
    }
    public void changeBackground(Integer color){
        tvContent.setBackgroundColor(color);
    }
    public void setTextMarker(String msg){
        tvContent.setText(msg);
    }
    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if(listlabelsComobine==null) {
            if (e instanceof CandleEntry) {

                CandleEntry ce = (CandleEntry) e;
                // tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));

                if (listlabels != null && highlight != null) {
                    String label = listlabels[highlight.getDataSetIndex()];
                    tvContent.setText(label + " " + mFormat.format(Double.parseDouble(String.valueOf(ce.getHigh()))));
                } else {
                    tvContent.setText(mFormat.format(Double.parseDouble(String.valueOf(ce.getHigh()))));
                }
            } else {
                if (listlabels != null && highlight != null) {
                    if (Constants.graphViewMaker != "") {
                        setTextMarker(Constants.graphViewMaker);
                    } else {
                        String label = listlabels[highlight.getDataSetIndex()];
                        //tvContent.setText(Utils.formatNumber(e.getY(), 0, true));
                        tvContent.setText(label + " " + mFormat.format(Double.parseDouble(String.valueOf(e.getY()))));
                    }
                } else {
                    tvContent.setText(mFormat.format(Double.parseDouble(String.valueOf(e.getY()))));
                }

            }
        }else {
            if (highlight != null) {
                if (highlight.getDataIndex()==0){
                    tvContent.setText(" "+mFormat.format(Double.parseDouble(String.valueOf(e.getY()))));
                }else{
                    tvContent.setText(" "+mFormat.format(Double.parseDouble(String.valueOf(e.getY()))));
                }
            } else {
                Log.d("EnterMarker","highlight else");
                tvContent.setText(mFormat.format(Double.parseDouble(String.valueOf(e.getY()))));
            }

        }

        super.refreshContent(e, highlight);
    }



    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}

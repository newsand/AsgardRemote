package valhalla.asgardremote;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static String m_URL;
    private static final String TAG = "MyActivity";
    ArrayList<Entry> m_chartEntries;
    ArrayList<String> m_chartLabels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_chartEntries = new ArrayList<>();
        m_chartLabels = new ArrayList<String>();

        setContentView(R.layout.activity_main);
        Button v_refreshButton = (Button) findViewById(R.id.btn);
        v_refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p_view) {
                //m_URL = "http://191.185.45.175:8080/webapi/temperature/getAll";
                //urlString = "http://192.168.0.21:8080/webapi/temperature/getAll";
                m_URL = "http://192.168.0.21:8080/webapi/temperature/getAll";
                Log.i(TAG,"heloo");

                new ProcessJSON().execute(m_URL);
            }
        });
        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        m_chartEntries.add(new Entry(0f, 0));
        m_chartEntries.add(new Entry(0f, 1));
        m_chartEntries.add(new Entry(0f, 2));
        m_chartEntries.add(new Entry(0f, 3));
        m_chartEntries.add(new Entry(0f, 4));
        m_chartEntries.add(new Entry(0f, 5));


        LineDataSet v_dataset = new LineDataSet(m_chartEntries, "# of Calls");
        m_chartLabels.add("1");
        m_chartLabels.add("2");
        m_chartLabels.add("3");
        m_chartLabels.add("4");
        m_chartLabels.add("5");
        m_chartLabels.add("6");

        LineData v_chartData = new LineData(m_chartLabels, v_dataset);
        v_dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        v_dataset.setDrawCubic(true);
        v_dataset.setDrawFilled(true);

        lineChart.setData(v_chartData);
        lineChart.animateY(1000);

    }

    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String v_dataStream = null;
            String v_URL = strings[0];

            HTTPDataHandler v_connector = new HTTPDataHandler();
            Log.i(TAG,"heloo2");
            v_dataStream = v_connector.GetHTTPData(v_URL);

            // Return the data from specified url
            return v_dataStream;
        }

        protected void onPostExecute(String p_rawData){

            //..........Process JSON DATA................
            if(p_rawData !=null){
                try{
                    JSONArray v_data = new JSONArray(p_rawData);


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                    ArrayList<Entry> entries = new ArrayList<>();
                    ArrayList<String> labels = new ArrayList<String>();

                    for (int i = 0; i < v_data.length(); i++) {
                        Date v_currentInstant;
                        JSONObject v_currentRegister = v_data.getJSONObject(i);
                        double v_temperature = v_currentRegister.getDouble("temperature");
                        String v_registerTime = v_currentRegister.getString("registerTime");
                        //int hi = v_currentRegister.getInt("hardwareId");
                        //int si = v_currentRegister.getInt("sensorId");
                        entries.add(new Entry((float)v_temperature, i));
                        try {
                            v_currentInstant = sdf.parse(v_registerTime);
                            labels.add(v_currentInstant.toString());
                            System.out.println("Date ->" + v_currentInstant);
                        } catch (Exception e) {
                            Log.i(TAG,e.getMessage());
                        }
                    }
                    LineChart lineChart = (LineChart) findViewById(R.id.chart);
                    LineDataSet dataset = new LineDataSet(entries, "Temperature");
                    LineData data = new LineData(labels, dataset);

                    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    dataset.setDrawCubic(true);
                    dataset.setDrawFilled(true);
                    lineChart.setData(data);
                    lineChart.animateY(1000);

                }catch(JSONException e){
                    e.printStackTrace();
                }
            } // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end
}

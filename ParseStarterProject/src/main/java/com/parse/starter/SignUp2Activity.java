package com.parse.starter;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

public class SignUp2Activity extends AppCompatActivity {

    public class SetTime implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

        private EditText editText;
        private Calendar myCalendar;
        private Context ctx;

        public SetTime(EditText editText, Context ctx){
            this.editText = editText;
            this.ctx = ctx;
            this.editText.setOnFocusChangeListener(this);
            this.myCalendar = Calendar.getInstance();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                new TimePickerDialog(this.ctx, this, hour, minute, false).show();
            }
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            this.editText.setText( hourOfDay + ":" + minute);
        }

    }

    CheckBox c1,c2,c3,c4;
    EditText t1, openTime, closeTime, co1, co2, co3, co4, cc1, cc2, cc3, cc4;
    SetTime open, close, o1, o2, o3, o4, ct1, ct2, ct3, ct4;

    LocationManager locationManager;
    LocationListener locationListener;
    ParseObject object;
    String Lat, Long;
    TextView lat1, long1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        c1 = (CheckBox)findViewById(R.id.checkBox1);
        c2 = (CheckBox)findViewById(R.id.checkBox2);
        c3 = (CheckBox)findViewById(R.id.checkBox3);
        c4 = (CheckBox)findViewById(R.id.checkBox4);
        lat1 = (TextView)findViewById(R.id.curLat);
        long1 = (TextView)findViewById(R.id.curLong);
        t1 = (EditText)findViewById(R.id.hospitalCode);
        openTime = (EditText)findViewById(R.id.openTime);
        openTime.setInputType(InputType.TYPE_NULL);
        closeTime = (EditText)findViewById(R.id.closeTime);
        closeTime.setInputType(InputType.TYPE_NULL);
        co1 = (EditText)findViewById(R.id.open1);
        co1.setInputType(InputType.TYPE_NULL);
        co2 = (EditText)findViewById(R.id.open2);
        co2.setInputType(InputType.TYPE_NULL);
        co3 = (EditText)findViewById(R.id.open3);
        co3.setInputType(InputType.TYPE_NULL);
        co4 = (EditText)findViewById(R.id.open4);
        co4.setInputType(InputType.TYPE_NULL);
        cc1 = (EditText)findViewById(R.id.close1);
        cc1.setInputType(InputType.TYPE_NULL);
        cc2 = (EditText)findViewById(R.id.close2);
        cc2.setInputType(InputType.TYPE_NULL);
        cc3 = (EditText)findViewById(R.id.close3);
        cc3.setInputType(InputType.TYPE_NULL);
        cc4 = (EditText)findViewById(R.id.close4);
        cc4.setInputType(InputType.TYPE_NULL);
        open = new SetTime(openTime,this);
        close = new SetTime(closeTime,this);
        o1 = new SetTime(co1,this);
        o2 = new SetTime(co2,this);
        o3 = new SetTime(co3,this);
        o4 = new SetTime(co4,this);
        ct1 = new SetTime(cc1,this);
        ct2 = new SetTime(cc2,this);
        ct3 = new SetTime(cc3,this);
        ct4 = new SetTime(cc4,this);

//        openTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//                TimePickerDialog timePickerDialog = new TimePickerDialog(SignUp2Activity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
//                        openTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
//                    }
//                }, 0, 0, false);
//                timePickerDialog.show();
//            }
//        });


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                Log.i("Location-Latitude", String.valueOf(location.getLatitude()));
                Log.i("Location-Longitude", String.valueOf(location.getLongitude()));
                Lat = String.valueOf(location.getLatitude());
                Long = String.valueOf(location.getLongitude());
                lat1.setText(Lat);
                long1.setText(Long);
                Toast.makeText(SignUp2Activity.this, "Location Updated", Toast.LENGTH_SHORT);
                locationManager.removeUpdates(this);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            }


        };

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.whereEqualTo("name", ParseUser.getCurrentUser().getUsername());
        query.setLimit(1);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null){
                    Log.i("Parse stuff", Integer.toString(list.size()));
                    if(list.size() > 0){
                        Log.i("Parse stuff", "Found user");
                        object = list.get(0);
                        t1.setText(object.getString("ID"));
                        openTime.setText(object.getString("open"));
                        closeTime.setText(object.getString("close"));
                        c1.setChecked(object.getBoolean("b1"));
                        c2.setChecked(object.getBoolean("b2"));
                        c3.setChecked(object.getBoolean("b3"));
                        c4.setChecked(object.getBoolean("b4"));
                        Lat = object.getString("Latitude");
                        Long = object.getString("Longitude");
                        lat1.setText(object.getString("Latitude"));
                        long1.setText(object.getString("Longitude"));
                    }
                    else{
                        Log.i("Parse stuff","New User");
                        object = new ParseObject("Hospital");
                        lat1.setText("Hello");
                        getLoc(findViewById(R.id.outer));
                    }
                }
            }
        });
    }

    public void getLoc(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // we have permission!
            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    protected boolean checkUnique(String id){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.whereEqualTo("ID",id);
        query.whereNotEqualTo("name", ParseUser.getCurrentUser().getUsername());
        List<ParseObject> objs;
        try{
            objs = query.find();
        }
        catch(com.parse.ParseException e1){
            Log.i("Parse error",e1.getMessage());
            return false;
        }
        if(objs.size() > 0){
            return false;
        }
        return true;
    }

    public void submitDetails(View view) {
        Boolean b1, b2, b3, b4;
        b1 = c1.isChecked();
        b2 = c2.isChecked();
        b3 = c3.isChecked();
        b4 = c4.isChecked();
        if(t1.getText().toString().matches("")){
            Toast.makeText(this, "Please Enter Hospital ID", Toast.LENGTH_SHORT).show();
        }
        else if(openTime.getText().toString().matches("")){
            Toast.makeText(this, "Please Enter Opening Time", Toast.LENGTH_SHORT).show();
        }
        else if(closeTime.getText().toString().matches("")){
            Toast.makeText(this, "Please Enter Closing Time", Toast.LENGTH_SHORT).show();
        }
        else{
            if(checkUnique(t1.getText().toString())){
                object.put("name", ParseUser.getCurrentUser().getUsername());
                object.put("ID",t1.getText().toString());
                object.put("open",openTime.getText().toString());
                object.put("close",closeTime.getText().toString());
                object.put("b1",b1);
                object.put("b2",b2);
                object.put("b3",b3);
                object.put("b4",b4);
                object.put("Latitude",Lat);
                object.put("Longitude",Long);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(SignUp2Activity.this, "Successful Update", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp2Activity.this, HospitalScreenActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(SignUp2Activity.this, "Error! Please Submit Again", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
            else{
                Toast.makeText(SignUp2Activity.this, "Hospital ID already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

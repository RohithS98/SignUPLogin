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

import java.util.List;

public class SignUp2Activity extends AppCompatActivity {

    CheckBox c1,c2,c3,c4;
    EditText t1, openTime, closeTime;
    //TextView mLocation;

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

        openTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                TimePickerDialog timePickerDialog = new TimePickerDialog(SignUp2Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        openTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        closeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                TimePickerDialog timePickerDialog = new TimePickerDialog(SignUp2Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        closeTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

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

    public void submitDetails(View view) {
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
            Boolean b1, b2, b3, b4;
            b1 = c1.isChecked();
            b2 = c2.isChecked();
            b3 = c3.isChecked();
            b4 = c4.isChecked();
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
    }
}

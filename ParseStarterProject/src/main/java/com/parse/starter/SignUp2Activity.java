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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.List;

public class SignUp2Activity extends AppCompatActivity {

    CheckBox c1,c2,c3,c4;
    EditText t1, openTime, closeTime;
    //TextView mLocation;

    LocationManager locationManager;
    LocationListener locationListener;

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
        t1 = (EditText)findViewById(R.id.hospitalCode);
        openTime = (EditText)findViewById(R.id.openTime);
        openTime.setInputType(InputType.TYPE_NULL);
        closeTime = (EditText)findViewById(R.id.closeTime);
        closeTime.setInputType(InputType.TYPE_NULL);
        // mLocation = (TextView)findViewById(R.id.location);

        openTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                ParseObject latlng=new ParseObject("Latlng");
                latlng.put("Latitude",String.valueOf(location.getLatitude()));
                latlng.put("Longitude",String.valueOf(location.getLongitude()));
                latlng.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.i("successfull","saved");
                        }
                        else{
                            Log.i("Failed",e.toString());
                        }
                    }
                });
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
    }

    public void getLoc(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // we have permission!
            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            //mLocation.setText("Location : "+String.valueOf(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).toString()));
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
            getLoc(view);
            Boolean b1, b2, b3, b4;
            b1 = c1.isChecked();
            b2 = c2.isChecked();
            b3 = c3.isChecked();
            b4 = c4.isChecked();
            String checked = b1.toString() + " " + b2.toString() + " " + b3.toString() + " " + b4.toString();
            Toast.makeText(SignUp2Activity.this, checked, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUp2Activity.this, HospitalScreenActivity.class);
            startActivity(intent);
        }
    }
}

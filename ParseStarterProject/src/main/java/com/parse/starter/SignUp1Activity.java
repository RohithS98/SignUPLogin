package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);
    }

    public void signUp(View view) {
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);

        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {
            Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();
        }
        else{
            ParseUser user = new ParseUser();

            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("Signup", "Successful");
                        Toast.makeText(SignUp1Activity.this, "Successful Sign Up", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp1Activity.this, SignUp2Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUp1Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(SignUp1Activity.this, MainActivity.class);
        startActivity(intent);
    }
}

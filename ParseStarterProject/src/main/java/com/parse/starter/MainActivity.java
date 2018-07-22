/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    EditText username;
    EditText password;
    TextView toggleTextView;
    LinearLayout linearLayout;
    ImageView logoImageView;
    boolean signUpMode = true;
    Button submitBtn;

    public void showUserList() {
        Intent intent = new Intent(MainActivity.this, UserListActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {

        if (username.getText().toString().matches("") || password.getText().toString().matches("")) {
            Toast.makeText(MainActivity.this, "Username/Password is required",
                    Toast.LENGTH_LONG).show();
        } else {

            if (signUpMode){
                // User Sign up
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Sign Up", "Success");
                            showUserList();
                        } else {
                            Log.i("Sign Up", "Failed" + e.toString());
                        }
                    }
                });
            } else {
                // User Log in
                String usernameTxt = username.getText().toString();
                String passwordTxt = password.getText().toString();
                ParseUser.logInInBackground(usernameTxt, passwordTxt, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // Hooray! The user is logged in.
                            Log.i("Login", "Success");
                            showUserList();
                        } else {
                            Log.i("Login", "Failed" + e.toString());
                        }
                    }
                });
            }
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set title to Activity
        setTitle("Instagram");

        username = (EditText) findViewById(R.id.username_edit);
        password = (EditText) findViewById(R.id.password_edit);
        submitBtn = (Button) findViewById(R.id.submit_btn);

        linearLayout = (LinearLayout) findViewById(R.id.layoutView);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);


        toggleTextView = (TextView) findViewById(R.id.toggle_function);
        toggleTextView.setOnClickListener(this);

        password.setOnKeyListener(this);
        linearLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            showUserList();
        }

      /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

      query.whereGreaterThan("score", 200);

      query.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {

              if (e == null && objects != null) {

                  for (ParseObject object : objects) {

                      object.put("score", object.getInt("score") + 50);
                      object.saveInBackground();

                  }

              }

          }
      });*/

//      ParseObject user = new ParseObject("user");
//
//      user.put("username", "trungtruc1094");
//      user.put("password", "Aa123456");
//      user.saveInBackground(new SaveCallback() {
//          @Override
//          public void done(ParseException e) {
//              if (e == null) {
//                  Log.i("Add User", "Success");
//              } else {
//                  Log.i("Add User", "Failed");
//              }
//          }
//      });
      /*

      ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

      query.whereEqualTo("username", "tommy");
      query.setLimit(1);

      query.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {

              if (e == null) {

                  Log.i("findInBackground", "Retrieved " + objects.size() + " objects");

                  if (objects.size() > 0) {

                      for (ParseObject object : objects) {

                          Log.i("findInBackgroundResult", Integer.toString(object.getInt("score")));

                      }

                  }

              }

          }
      });

      */

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void onClick(View view) {
        // Toggle between signup and login mode
        if (view.getId() == R.id.toggle_function) {
            if (signUpMode) {
                toggleTextView.setText("or, Sign up");
                submitBtn.setText("LOG IN");
                signUpMode = false;
            } else {
                toggleTextView.setText("or, Log in");
                submitBtn.setText("SIGN UP");
                signUpMode = true;
            }
        } else if (view.getId() == R.id.logoImageView || view.getId() == R.id.layoutView) {
            // Hide keyboard when clicking on background or logo
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        // Sign up or Login when clicking enter button on keyboard
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(view);
        }
        return false;
    }
}

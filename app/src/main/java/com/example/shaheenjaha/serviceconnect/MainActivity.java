package com.example.shaheenjaha.serviceconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button login_b;
    EditText reg_no,pswd;
    TextView new_user,frgt_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_b = findViewById(R.id.buttonLogin);
        reg_no = findViewById(R.id.editTextUsername);
        pswd = findViewById(R.id.editTextPassword);

        new_user = findViewById(R.id.textViewNewUser);
        frgt_pwd = findViewById(R.id.textViewForgot);
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, register.class);
                startActivity(i);
            }
        });
        login_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spwd = pswd.getText().toString();
                String rollno = reg_no.getText().toString();
                int leng = rollno.length();
                String validate = "^[0-9][0-9][A-Z a-z][0-9][0-9][A-Z a-z][0-9][0-9][A-Z a-z 0-9][0-9]$";
                Pattern pattern = Pattern.compile(validate);
                Matcher matcher = pattern.matcher(rollno);
                Boolean mat = matcher.matches();
                if (mat) {
                    new AsyncClass1().execute(rollno,spwd);
                } else if (leng < 10 || leng > 10) {
                    Toast.makeText(getApplicationContext(), "Enter valid Registration number", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter valid Registration number", Toast.LENGTH_LONG).show();
                }


            }
        });
        frgt_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ifp = new Intent(MainActivity.this, register.class);
                ifp.putExtra("button_text","RequestPassword");
                startActivity(ifp);
            }
        });
    }
        public class AsyncClass1 extends AsyncTask<String,String,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... arg) {
                String regNo = arg[0];
                String pass=arg[1];
                String data1="";
                int tmp1;
                try{
                    URL url=new URL ("https://aec.edu.in/service_connect/app/Login.php");
                    String urlParams="regNo="+regNo+"&pass="+pass;
                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream os=httpURLConnection.getOutputStream();
                    os.write(urlParams.getBytes());
                    os.flush();
                    os.close();
                    InputStream is=httpURLConnection.getInputStream();
                    while ((tmp1=is.read())!=-1){
                        data1+=(char)tmp1;
                    }
                    is.close();
                    httpURLConnection.disconnect();
                    return data1;
                }
                catch (MalformedURLException e){
                    e.printStackTrace();
                    return "Exception:"+e.getMessage();
                }
                catch (IOException e){
                    e.printStackTrace();
                    return "Exception:"+e.getMessage();
                }
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                try {
                    JSONObject user_data = new JSONObject(s);
                    String status = user_data.getString("status");
                    //Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
                    switch (status)
                    {
                        case "TRUE":
                            Toast.makeText(getApplicationContext(),"Successfully Logged In",Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("student_data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("s_name",user_data.getString("name"));
                            editor.putString("s_reg_no",user_data.getString("regNo"));
                            editor.putString("s_room_no",user_data.getString("roomNo"));
                            editor.putString("s_gender",user_data.getString("gender"));
                            editor.putString("s_mess",user_data.getString("mess"));
                            editor.putString("s_contact_no",user_data.getString("contactNo"));
                            editor.putString("s_email",user_data.getString("email"));
                            editor.apply();
                            startActivity(new Intent(MainActivity.this,register.class));
                            //Toast.makeText(MainActivity.this, sharedPreferences.getString("s_email","default"), Toast.LENGTH_SHORT).show();
                            break;
                        case "FALSE":
                            Toast.makeText(getApplicationContext(),"You are not a registered user",Toast.LENGTH_LONG).show();
                            break;
                        default:break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
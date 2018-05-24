package com.example.shaheenjaha.serviceconnect;

import android.os.AsyncTask;
import android.os.PatternMatcher;
import android.service.autofill.RegexValidator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //String forget = getIntent().getExtras().getString("button_text");
        final Button reg_b;
        final EditText reg_user;
        reg_b=(Button)findViewById(R.id.buttonregister);
        reg_user=(EditText)findViewById(R.id.editTextUser);
        //reg_b.setText(forget);
        reg_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sreg=reg_user.getText().toString();
                int length=sreg.length();
                String validate="^[0-9][0-9][A-Z a-z][0-9][0-9][A-Z a-z][0-9][0-9][A-Z a-z 0-9][0-9]$";
                Pattern pattern=Pattern.compile(validate);
                Matcher matcher=pattern.matcher(sreg);
                Boolean mat = matcher.matches();
                if(mat){
                  new AsyncClass().execute(sreg);}
                else if(length<10 || length>10){
                    Toast.makeText(getApplicationContext(), "Enter valid Registration number", Toast.LENGTH_LONG).show();
                }
                else{
                  Toast.makeText(getApplicationContext(), "Enter", Toast.LENGTH_LONG).show();
               }

            }
        });

    }
    public class AsyncClass extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg) {
            String regNo = arg[0];
            String data="";
            int tmp;
            try{
                URL url=new URL ("https://aec.edu.in/service_connect/app/RequestPassword.php");
                String urlParams="regNo="+regNo;
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is=httpURLConnection.getInputStream();
                while ((tmp=is.read())!=-1){
                    data+=(char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();
                    return data;
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
            if(s.equals("TRUE")){
                    Toast.makeText(getApplicationContext(), "USER BELONGS TO HOSTEL", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getApplicationContext(), "USER DOESN'T BELONG TO HOSTEL", Toast.LENGTH_LONG).show();
            }
        }
    }
}
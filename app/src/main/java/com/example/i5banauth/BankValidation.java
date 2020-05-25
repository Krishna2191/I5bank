package com.example.i5banauth;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class BankValidation extends Activity {
    private DatabaseHandler helper = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        bankOption();
        //finish();

    }

    public void bankOption() {
        final String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        Cursor cursor = helper.fetchData();
        ArrayList<String> columnArray1 = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            columnArray1.add(cursor.getString(1));
        }
        columnArray1.add("Close");
        final String[] banks = (String[]) columnArray1.toArray(new String[columnArray1.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick bank to validate");

        builder.setItems(banks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                System.out.println("Banks   "+banks[which]+" Phone Number  "+phoneNumber);
                if (banks[which]=="Close")
                {
                    finish();
                    System.exit(0);
                }
                else
                {
                    new RetrieveFeedTask(banks[which],phoneNumber).execute();
                  //  finish();

                   // processBank(banks[which],phoneNumber);
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void onBackgroundTaskDataObtained(String response) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("I5");
        builder.setMessage("BANK AUTH");

        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        else if(response.equals("VALID"))
        {
            builder.setPositiveButton("AUTH CALL PROCEED", null);
            System.out.println("Hi valid Phne");
        }
        else
        {
            builder.setNegativeButton("NOT AN AUTH CALL", null);
            System.out.println("Invalid Phne");
        }
        System.out.println(response);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

private class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
    private Exception exception;
    String bank;
    String phoneNum;
    RetrieveFeedTask(String bk,String phno)
    {
        bank=bk;
        phoneNum=phno;
    }
    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
       // responseView.setText("");
    }

    protected String doInBackground(Void... urls) {

        try {

            System.out.println("MobNumber     "+phoneNum);
            System.out.println("bank     "+bank);
            URL url = new URL("https://prod-03.southcentralus.logic.azure.com/workflows/c4f756b128f6453eaf1e021cd7fe5695/triggers/manual/paths/invoke/test_" + bank + "/" + phoneNum + "?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=XJf1tzzTpuUUMT7jVLGqaYCsgYr1AD2az0tjsvKqAEs");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                //System.out.println(stringBuilder);
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                JSONObject json = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = json.getJSONArray("value");
                if(jsonArray.length()==0)
                {
                    return "INVALID";
                }
                else if (jsonArray.getJSONObject(0).getString("MobNumber").equals(phoneNum) ) {
                    return "VALID";
                }
                else {
                    return "INVALID";
                }
            }
            finally {
                urlConnection.disconnect();
            }
        }
        catch(Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
            return "INVALID";
        }

    }

    protected void onPostExecute(String response) {


        BankValidation.this.onBackgroundTaskDataObtained(response);
        //progressBar.setVisibility(View.GONE);
        Log.i("INFO", response);
      //  responseView.setText(response);
        // TODO: check this.exception
        // TODO: do something with the feed

    }
}

}

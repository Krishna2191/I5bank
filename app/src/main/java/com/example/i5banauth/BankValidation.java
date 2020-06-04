package com.example.i5banauth;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

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
        //bankOption();
        showDialog();
        //finish();

    }
    public void showDialog() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        myDialog.setTitle("I5 Bank Auth");
        myDialog.setMessage("Do want to Authenticate Caller ?");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        bankOption();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        System.exit(0);
                        break;
                }

            }
        };
        myDialog.setPositiveButton("Yes", dialogClickListener);
        myDialog.setNegativeButton("No",dialogClickListener);
        myDialog.show();

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
        builder.setTitle("I5 Bank - Validate");

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
                else;
                {
                    String data[] = new String[2];
                    data[0] =  banks[which];
                    data[1] =  phoneNumber;

                    Context context = getApplicationContext();
                    new RetrieveFeedTask(context).execute(data);
                    //   System.exit(0);
                    // processBank(banks[which],phoneNumber);
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    class RetrieveFeedTask extends AsyncTask<String[], Void, String> {
        private Exception exception;

        private Context mContext;
        private int NOTIFICATION_ID = 1;
        private Notification mNotification;
        private NotificationManager mNotificationManager;

        String bank;
        String phoneNum;
        ProgressDialog progressDialog;
        RetrieveFeedTask(Context context)
        {
       /* this.mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
*/
        }
        protected void onPreExecute() {
          /*  System.out.println("ONPre execute");
            createNotification("Data download is in progress","");*/
        }

        protected String doInBackground(String[]... data) {

            bank = data[0][0].toString();
            phoneNum=data[0][1].toString();
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
                        System.out.println("Invalid");
                        return "INVALID";

                    }
                    else if (jsonArray.getJSONObject(0).getString("MobNumber").equals(phoneNum) ) {
                        System.out.println("Valid");
                        return "VALID";
                    }
                    else {
                        System.out.println("Invalid");
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
            System.out.println("Response welcome   "+response);
            progressDialog = ProgressDialog.show(BankValidation.this,
                    "I5 Bank Auth",
                    response);
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progressDialog.cancel();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);
            //     createNotification("Data download ended abnormally!",response);


        }
        private void createNotification(String contentTitle, String contentText) {
            System.out.println("contentTitle  "+ contentTitle);
            System.out.println("contentText  "+ contentText);
            //Build the notification using Notification.Builder
     /*   Notification.Builder builder = new Notification.Builder(mContext)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText);

        //Get current notification
      //  mNotification = builder.getNotification();
System.out.println("Notification");
        //Show the notification

        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        mNotificationManager.notify(001, builder.build());*/
        }
    }
}
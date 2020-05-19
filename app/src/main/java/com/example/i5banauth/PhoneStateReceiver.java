package com.example.i5banauth;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Arrays;

/**
 *
 */
class Global {

    public static  Boolean IS_RUNNING= false;
}
public class PhoneStateReceiver extends BroadcastReceiver {
   // public static int globalInt = 0;
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (Global.IS_RUNNING){
                //allready running!!! do nothing
                return;
            }
            Global.IS_RUNNING=Boolean.TRUE;
            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Toast.makeText(context, "Incoming Call State", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Ringing State Number is -" + incomingNumber, Toast.LENGTH_SHORT).show();
                if(contactExists(context,incomingNumber))
                {
                    Toast.makeText(context, "Valid Contact", Toast.LENGTH_SHORT).show();
                   // abortBroadcast();
                }
                else {
                Intent mIntent = new Intent(context,AttendCall.class) ;//Same as above two lines
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mIntent);
                }
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                Toast.makeText(context, "Call Received State", Toast.LENGTH_SHORT).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Toast.makeText(context, "Call Idle State", Toast.LENGTH_SHORT).show();
                Global.IS_RUNNING=false;
               // clearAbortBroadcast();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean contactExists(Context context, String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        System.out.println("Hi " +lookupUri);
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        //System.out.println(Arrays.toString(mPhoneNumberProjection));
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                System.out.println("Hi I am in !");
                return true;
            }
           else
            {
               System.out.println("Hi I am not in !");
         //       return true;
            }
        }
        finally {
            if (cur != null){
                cur.close();
            }
            return false;
        }
    }
}





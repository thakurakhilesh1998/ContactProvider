package com.example.myquizapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private final int REQUEST_READ_CONTACT_PERMISSION = 100;
    private final String TAG = "MainActivity";
    private Cursor mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
             {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT_PERMISSION);
            }
         else {
            new TermsLoader().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACT_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new TermsLoader().execute();
            }
        }
    }

    private class TermsLoader extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {

            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            mData = cursor;
            int id = mData.getColumnIndex("_ID");
            int name = mData.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
           // int phoneNumber=mData.getColumnIndex(ContactsContract.Contacts.EXTRA_ADDRESS_BOOK_INDEX_TITLES);
            while (mData.moveToNext()) {
                String word = cursor.getString(id);
                String display_name = cursor.getString(name);
           //     String phone=cursor.getString(phoneNumber);
                Log.i("contacts id", word);
                Log.i("display namee", display_name);
              //  Log.i("number",phone);
            }
        }
    }
}

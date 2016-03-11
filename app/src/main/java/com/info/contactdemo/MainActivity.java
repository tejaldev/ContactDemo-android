package com.info.contactdemo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.info.contactdemo.contacts.PhoneContact;
import com.info.contactdemo.contacts.PhoneContactAdapter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tejalpar on 2/15/16.
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<PhoneContact> phoneContacts = new ArrayList<>();
    private List<PhoneContact> temp;
    private ListView mContactList;
    private PhoneContactAdapter adapter;
    private ContentResolver resolver;
    private Cursor phonesCursor;

    // columns requested from the database
    private static final String[] PROJECTION1 = {
            ContactsContract.Contacts._ID, // _ID is always required
            ContactsContract.Contacts.DISPLAY_NAME, // that's what we want to display
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Phone._ID, // _ID is always required
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, // that's what we want to display
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Contactables.ACCOUNT_TYPE_AND_DATA_SET
    };

    private static final String SELECTION =
            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                    "'" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE + "'" +
                    " AND " + ContactsContract.CommonDataKinds.Contactables.ACCOUNT_TYPE_AND_DATA_SET + " = 'vnd.sec.contact.phone'" ;

    private static final String SORT_ORDER = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC ";

    private Uri mContactUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //1. initialize
        phoneContacts = new ArrayList<PhoneContact>();
        resolver = getContentResolver();

        //2. Query
        mContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        phonesCursor = resolver.query(mContactUri,
                PROJECTION,
                SELECTION,
                null,
                SORT_ORDER);

        //3. Load contacts in background
        mContactList = (ListView) findViewById(R.id.contactList);
        LoadContacts loadContact = new LoadContacts();
        loadContact.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadContacts extends AsyncTask<Void, Void, Void> {

        int lastPhotoBgColor;

        @Override
        protected Void doInBackground(Void... params) {
            if(phonesCursor != null) {
                while (phonesCursor.moveToNext()) {
                    int photoBgColor = -1;
                    Bitmap photoThumbnail = null;

                    String id = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String cName = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String photoUri = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                    String phoneNumber = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String acc = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.ACCOUNT_TYPE_AND_DATA_SET));

                    try {
                        if (photoUri != null) {
                            photoThumbnail = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(photoUri));
                        }
                        else {
                            photoBgColor = generatePhotoColor();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    PhoneContact contact = new PhoneContact();

                    //Name & number
                    contact.setId(id);
                    contact.setName(cName);
                    contact.setNameInitial(cName.substring(0, 1));
                    contact.setPhoneNumber(phoneNumber);

                    //photo
                    contact.setPhotoThumbnail(photoThumbnail);
                    contact.setDefaultPhotoBgColor(photoBgColor);

                    //add to list
                    phoneContacts.add(contact);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new PhoneContactAdapter(phoneContacts, MainActivity.this);
            mContactList.setAdapter(adapter);
        }

        //generate default photo bg color
        private int generatePhotoColor() {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            if(color == lastPhotoBgColor) {
                color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            }
            lastPhotoBgColor = color;
            return color;
        }
    }
}

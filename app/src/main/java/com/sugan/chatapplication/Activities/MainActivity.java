package com.sugan.chatapplication.Activities;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sugan.chatapplication.DataStorage.SQLiteDatabase.DBStorageAccess;
import com.sugan.chatapplication.MainActivity.Chats.ChatsItem;
import com.sugan.chatapplication.MainActivity.Chats.ChatsList;
import com.sugan.chatapplication.MainActivity.Contacts.ContactsItem;
import com.sugan.chatapplication.MainActivity.Contacts.ContactsList;
import com.sugan.chatapplication.MainActivity.Groups.GroupsItem;
import com.sugan.chatapplication.MainActivity.Groups.GroupsList;
import com.sugan.chatapplication.MainActivity.ViewPager.Adapter;
import com.sugan.chatapplication.R;
import com.sugan.chatapplication.util.ViewPagerSlidingTabs.SlidingTabLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    static final String LOG_TAG = "MainActivity";

    ViewPager viewPager;
    Adapter viewPagerAdapter;
    SlidingTabLayout slidingTabLayout;
    CharSequence[] tabsTitles = {"Groups", "Chats", "Contacts"};
    public static ArrayList<ChatsItem> chatsList;
    public static ArrayList<GroupsItem> groupsList;
    public static ArrayList<ContactsItem> contactsList;
    public static DBStorageAccess chatsDB, groupsDB, contactsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPagerAdapter = new Adapter(getApplicationContext(), getSupportFragmentManager(), tabsTitles);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getApplicationContext(), R.color.slidingTabSelectedItemColor));
        slidingTabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(pageChangeListener);

        chatsDB = new DBStorageAccess(getApplicationContext(), 1);
        groupsDB = new DBStorageAccess(getApplicationContext(), 0);
        contactsDB = new DBStorageAccess(getApplicationContext(), 2);

    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        //Save the necessary data here

        savedInstanceState.putParcelableArrayList("chats",chatsList);
        savedInstanceState.putParcelableArrayList("groups",groupsList);
        savedInstanceState.putParcelableArrayList("contacts",contactsList);

        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){

        //Fired only when the bundle is not null

        chatsList = savedInstanceState.getParcelableArrayList("chats");
        groupsList = savedInstanceState.getParcelableArrayList("groups");
        contactsList = savedInstanceState.getParcelableArrayList("contacts");

        super.onRestoreInstanceState(savedInstanceState );

    }

}

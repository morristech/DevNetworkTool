package app.deadmc.devnetworktool.activities;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;

import app.deadmc.devnetworktool.R;
import app.deadmc.devnetworktool.clients.BaseAbstractClient;
import app.deadmc.devnetworktool.constants.DevConsts;
import app.deadmc.devnetworktool.fragments.HistoryOfUrlsFragment;
import app.deadmc.devnetworktool.fragments.ParentFragment;
import app.deadmc.devnetworktool.fragments.SettingsFragment;
import app.deadmc.devnetworktool.fragments.ping.PingFragment;
import app.deadmc.devnetworktool.fragments.rest.RestFragment;
import app.deadmc.devnetworktool.services.ConnectionService;

public class MainActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ParentFragment currentFragment;
    private Fragment fragment;
    private ConnectionService connectionService;
    private Toolbar toolbar;
    private boolean exitClicked = false;

    private boolean serviceBound = false;


    public void stopService() {
        if (connectionService != null)
            connectionService.stopService();

        Intent intent = new Intent(this,
                ConnectionService.class);
        stopService(intent);
        //doUnbindService();
    }


    //-------------------Service Ended-----------------------------------------------------//


    public void setCurrentFragment(ParentFragment currentFragment) {
        if (currentFragment != null)
            Log.e("setCurrentFragment", "mainActivity2 " + currentFragment.toString());
        else
            Log.e("setCurrentFragment", "fragment is null");
        this.currentFragment = currentFragment;
    }

    public void setCurrentClient(BaseAbstractClient currentClient) {
        if (connectionService != null) {
            connectionService.setCurrentClient(currentClient);
        }
    }

    public ParentFragment getCurrentFragment() {
        return currentFragment;
    }


    public void setCustomTitle(int stringId) {
        toolbar.setTitle(stringId);
        Log.e("setTitleActivity",""+getString(stringId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        /*
        if (isServiceRunning(ConnectionService.class.getName())) {
            doBindService();
            return;
        }
        */


        if (savedInstanceState != null) {
            /*
            //Restore the fragment's instance
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            ipAddress = savedInstanceState.getString("ipAddress");
            port = savedInstanceState.getInt("port");
            typeOfConnection = savedInstanceState.getString("typeOfFragment");
            if (fragment == null) {
                runUrlFragment(DevConsts.REST);
            } else {
                openFragment(fragment, true);
            }
            */
        } else {
            runUrlFragment(DevConsts.REST);
        }



    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed", "started");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (currentFragment!= null) {
                if (currentFragment.toString().equals("FormattedJsonFragment")) {
                    super.onBackPressed();
                    return;
                }
            }

            if (currentFragment == null) {
                Log.e("onBackPressed", "currentFragment == null");
                super.onBackPressed();

                if (currentFragment == null)
                    this.finish();


            } else {
                Log.e("onBackPressed", "currentFragment != null");
                if (connectionService != null && connectionService.isRunning()) {
                    Log.e("connectionService", "connectionService.isRunning "+(connectionService.isRunning()));
                    showAlert(R.id.exit);
                } else {
                    stopService();
                    super.onBackPressed();
                    if (currentFragment == null)
                        this.finish();
                }
            }
            //currentFragment.initElements();

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        menu.findItem(R.id.action_settings).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.exit)
            exitClicked = true;
        showAlert(id);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(Fragment fragment, boolean addToBackStack) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (addToBackStack) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_id, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_id, fragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //doUnbindService();
    }

    private boolean isServiceRunning(String serviceName) {
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = (ActivityManager.RunningServiceInfo) i
                    .next();

            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                serviceRunning = true;
            }
        }
        return serviceRunning;
    }

    public void runUrlFragment(String type) {
        stopService();
        HistoryOfUrlsFragment historyOfUrlsFragment = new HistoryOfUrlsFragment();
        historyOfUrlsFragment.setTypeOfFragment(type);
        openFragment(historyOfUrlsFragment, true);
    }

    public void runFragment(String type, ParentFragment parentFragment) {
        parentFragment.setTypeOfFragment(type);
        openFragment(parentFragment, true);
    }


    public void runFragmentDependsOnClickedItem(int item) {
        switch (item) {
            case R.id.ping:
                runFragment(DevConsts.PING, new PingFragment());
                break;
            case R.id.rest:
                runFragment(DevConsts.REST, new RestFragment());
                break;
            case R.id.settings:
                runFragment(DevConsts.REST, new SettingsFragment());
                break;
            case R.id.exit:
                if (exitClicked)
                    finish();
                else
                    this.onBackPressed();
                break;
        }
    }

    public void showAlert(int item) {
        final int finalItem = item;
        if (connectionService == null || !connectionService.isRunning()) {
            runFragmentDependsOnClickedItem(finalItem);
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppTheme_Dialog_Alert);
        alertDialogBuilder.setMessage(R.string.alert_close_connection);
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopService();
                runFragmentDependsOnClickedItem(finalItem);
                //onBackPressed();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
    }


}

package org.androidtown.gigdingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String notiTitle = "";
    String ProTitle = "";
    String SchTitle = "";
    String MapTitle = "";
    String IntraTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sTitleStr = (String) getSupportActionBar().getTitle();

                if(sTitleStr.equals(notiTitle)) {               //공지사항
                    Snackbar.make(view, sTitleStr, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (sTitleStr.equals(ProTitle)) {        //프로젝트 / 팀원관리

                    Fragment fragemnt = new ProFragmentDetail();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_fragment_layout, fragemnt).addToBackStack(null).commit();

                } else if (sTitleStr.equals(SchTitle)) {        //일정관리
                    Snackbar.make(view, sTitleStr, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (sTitleStr.equals(MapTitle)) {        //지도
//                    Snackbar.make(view, sTitleStr, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Fragment fragment = new MapFragmentDetail();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_fragment_layout, fragment).addToBackStack(null).commit();
                } else if (sTitleStr.equals(IntraTitle)) {      //IntraNet
                    Snackbar.make(view, sTitleStr, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

    }

    private void init() {
        //타이틀 정보 SET
        notiTitle = getString(R.string.nav_noti);
        ProTitle = getString(R.string.nav_project);
        SchTitle = getString(R.string.nav_schedule);
        MapTitle = getString(R.string.nav_map);
        IntraTitle = getString(R.string.nav_intranet);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_noti:
                fragment = new NotiFragment();
                title = notiTitle;
                break;
            case R.id.nav_project:
                fragment = new ProFragment();
                title = ProTitle;
                break;
            case R.id.nav_schedule:
                fragment = new ScheduleFragment();
                title = SchTitle;
                break;
            case R.id.nav_map:
                fragment = new MapFragment();
                title = MapTitle;
                break;
            case R.id.nav_intranet:
                fragment = new IntranetFragment();
                title = IntraTitle;
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment).addToBackStack(null).commit();
        }

        setActionBarTitle(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title){
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }
}

package info.breezes.itebooks.app;

import android.app.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.SearchView;
import info.breezes.itebooks.app.download.DownloadManagerFragment;
import info.breezes.itebooks.app.library.LibraryFragment;
import info.breezes.itebooks.app.search.SearchResultFragment;
import info.breezes.itebooks.app.settings.SettingsActivity;


public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks,IFragmentHost {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private String searchQuery;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                showFragment(new StartFragment());
                mTitle=getString(R.string.title_section1);
                break;
            case 1:
                showFragment(new DownloadManagerFragment());
                mTitle=getString(R.string.title_section2);
                break;
            case 2:
                showFragment(new LibraryFragment());
                mTitle=getString(R.string.title_section3);
                break;

        }

    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }



    public void settingsClicked(View view){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void aboutClicked(View view){

    }

    public void restoreActionBar() {
        Log.d("MainActivity","restoreActionBar");
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(searchQuery);
        }
    }

    private void doMySearch(String searchQuery) {
        Log.d("MAS", searchQuery);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SearchManager.QUERY, searchQuery);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Main Activity", "1");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            SearchManager mSearMan = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(mSearMan.getSearchableInfo(getComponentName()));
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(String title) {
        mTitle=title;
        getActionBar().setTitle(mTitle);
    }
}

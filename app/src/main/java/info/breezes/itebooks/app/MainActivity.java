package info.breezes.itebooks.app;

import android.app.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import info.breezes.itebooks.app.main.SearchResultFragment;
import info.breezes.itebooks.app.model.NavigationDrawerItem;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setDrawerListAdapter(new MainNavigationDrawerAdapter(this, getLayoutInflater()));
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, NavigationDrawerItem item) {
        switch (item.getAction()) {
            case StartActivity:
                startActivity(new Intent(this, item.getActivity()));
                break;
            case ShowFragment:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, item.getFragment()).commit();
                mTitle = item.getTitle();
                break;
        }

    }

    public void restoreActionBar() {

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Main Activity","1");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(getString(R.string.hint_search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("Main Activity", "onQueryTextSubmit");
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = new SearchResultFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(SearchManager.QUERY, query);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    InputMethodManager inputMethodService = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodService.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d("Main Activity", "onQueryTextChange");
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


}

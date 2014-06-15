package info.breezes.itebooks.app.download;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import info.breezes.itebooks.app.R;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class DownloadManagerFragment extends Fragment implements ActionBar.TabListener {

    class DownloadFragmentPagerAdapter extends FragmentPagerAdapter {

        private Fragment downloadingFragment;
        private Fragment downloadedFragment;

        public DownloadFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            downloadedFragment = new DownloadedFragment();
            downloadingFragment = new DownloadingFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return downloadingFragment;
                case 1:
                    return downloadedFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, null);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupTabs(viewPager);
        setHasOptionsMenu(true);
        return view;
    }

    private void setupTabs(ViewPager viewPager) {
        viewPager.setAdapter(new DownloadFragmentPagerAdapter(getFragmentManager()));
        ActionBar actionBar = getActivity().getActionBar();
        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(R.string.downloading);
        tab.setTabListener(this);
        actionBar.addTab(tab);
        tab = actionBar.newTab();
        tab.setText(R.string.downloaded);
        tab.setTabListener(this);
        actionBar.addTab(tab);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        getActivity().getActionBar().removeAllTabs();
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("DownloadMangeFragment", "2");
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        return;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (viewPager != null) {
            viewPager.setCurrentItem(tab.getPosition());
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (viewPager != null) {
            viewPager.setCurrentItem(tab.getPosition());
        }
    }
}

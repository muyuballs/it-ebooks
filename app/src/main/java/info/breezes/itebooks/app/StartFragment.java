package info.breezes.itebooks.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.astuetz.PagerSlidingTabStrip;
import info.breezes.annotation.LayoutView;
import info.breezes.annotation.LayoutViewHelper;
import info.breezes.itebooks.app.R;
import info.breezes.itebooks.app.activity.BookDetailActivity;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class StartFragment extends Fragment {

    private IFragmentHost fragmentHost;
    @LayoutView(R.id.tabStrip)
    private PagerSlidingTabStrip tabStrip;
    @LayoutView(R.id.viewPager)
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, null);
        LayoutViewHelper.InitLayout(view, this);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return null;
            }

            @Override
            public int getCount() {
                return 0;
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        fragmentHost = (IFragmentHost) activity;
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        if(fragmentHost!=null){
            fragmentHost.setTitle(getActivity().getString(R.string.app_name));
        }
        super.onStart();
    }

    @Override
    public void onDetach() {
        fragmentHost=null;
        super.onDetach();
    }

}

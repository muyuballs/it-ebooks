package info.breezes.itebooks.app.model;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;

/**
 * Created by jianxingqiao on 14-6-14.
 */

public class NavigationDrawerItem {

    private Drawable icon;
    private String title;
    private Fragment fragment;
    private Class<? extends Activity> activity;
    private Action action;

    public NavigationDrawerItem(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
        this.action = Action.ShowFragment;
    }

    public NavigationDrawerItem(String title, Class<? extends Activity> activity) {
        this.title = title;
        this.activity = activity;
        this.action = Action.StartActivity;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public void setActivity(Class<Activity> activity) {
        this.activity = activity;
    }

    public Action getAction() {
        return action != Action.Default ? action : fragment != null ? Action.ShowFragment : Action.StartActivity;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}

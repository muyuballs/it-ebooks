package info.breezes.itebooks.app.model;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;

/**
 * Created by jianxingqiao on 14-6-14.
 */

public class NavigationDrawerItem {

    private int icon;
    private String title;
    private Action action;

    public NavigationDrawerItem(String title) {
        this(title,Action.ShowFragment);
    }

    public NavigationDrawerItem(String title,Action action) {
        this.title = title;
        this.action= action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}

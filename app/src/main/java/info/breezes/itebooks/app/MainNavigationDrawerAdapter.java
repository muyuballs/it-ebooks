package info.breezes.itebooks.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import info.breezes.itebooks.app.download.DownloadManagerFragment;
import info.breezes.itebooks.app.library.LibraryActivity;
import info.breezes.itebooks.app.settings.SettingsActivity;
import info.breezes.itebooks.app.main.StartFragment;
import info.breezes.itebooks.app.model.NavigationDrawerItem;

import java.util.ArrayList;

/**
 * Created by jianxingqiao on 14-6-15.
 */
public class MainNavigationDrawerAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<NavigationDrawerItem> items;

    public MainNavigationDrawerAdapter(Context context, LayoutInflater inflater) {
        this.layoutInflater = inflater;
        items = new ArrayList<NavigationDrawerItem>();
        items.add(new NavigationDrawerItem(context.getString(R.string.title_section1), new StartFragment()));
        items.add(new NavigationDrawerItem(context.getString(R.string.title_section2), new DownloadManagerFragment()));
        items.add(new NavigationDrawerItem(context.getString(R.string.title_section3), LibraryActivity.class));
        items.add(new NavigationDrawerItem(context.getString(R.string.title_section4), SettingsActivity.class));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public NavigationDrawerItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.navigation_drawer_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        NavigationDrawerItem item = getItem(position);
        if (item.getIcon() != null) {
            holder.imageView.setImageDrawable(item.getIcon());
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.textView.setText(item.getTitle());
        return convertView;
    }

    class Holder {
        ImageView imageView;
        TextView textView;
    }
}
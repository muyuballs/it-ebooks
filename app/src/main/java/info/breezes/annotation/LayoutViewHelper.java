package info.breezes.annotation;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by jianxingqiao on 14-6-22.
 */
public class LayoutViewHelper {
    public static void InitLayout(Activity activity){
        Field fields[]= activity.getClass().getDeclaredFields();
        for (Field field: fields){
            LayoutView layoutView=field.getAnnotation(LayoutView.class);
            if(layoutView!=null){
                try {
                    field.setAccessible(true);
                    field.set(activity,activity.findViewById(layoutView.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void InitLayout(View view,Object owner){
        Field fields[]= owner.getClass().getDeclaredFields();
        for (Field field: fields){
            LayoutView layoutView=field.getAnnotation(LayoutView.class);
            if(layoutView!=null){
                try {
                    field.setAccessible(true);
                    field.set(owner,view.findViewById(layoutView.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

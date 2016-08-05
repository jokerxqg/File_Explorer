package utils;

import android.app.Activity;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by joker on 2016/8/2/002.
 */
public class FinshActivity {


    private LinkedList<Activity> activityLinkedList = new LinkedList<Activity>();

    private FinshActivity() {
    }

    private static FinshActivity instance;

    public static FinshActivity getInstance() {
        if (null == instance) {
            instance = new FinshActivity();
        }
        return instance;
    }

    //向list中添加Activity
    public FinshActivity addActivity(Activity activity) {
        activityLinkedList.add(activity);
        return instance;
    }

    //结束特定的Activity(s)
    public FinshActivity finshActivities(Class<? extends Activity>... activityClasses) {
        for (Activity activity : activityLinkedList) {
            if (Arrays.asList(activityClasses).contains(activity.getClass())) {

                activity.finish();
            }
        }
        return instance;
    }

    //结束所有的Activities
    public FinshActivity finshAllActivities() {
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }
        return instance;
    }

}

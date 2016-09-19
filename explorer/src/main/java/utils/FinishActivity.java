package utils;

import android.app.Activity;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by joker on 2016/8/2/002.
 */
public class FinishActivity {


    private LinkedList<Activity> activityLinkedList = new LinkedList<Activity>();

    public FinishActivity() {
    }

    private static FinishActivity instance;

    public static FinishActivity getInstance() {
        if (null == instance) {
            instance = new FinishActivity();
        }
        return instance;
    }

    //向list中添加Activity
    public FinishActivity addActivity(Activity activity) {
        activityLinkedList.add(activity);
        return instance;
    }

    //结束特定的Activity(s)
    public FinishActivity finshActivities(Class<? extends Activity>... activityClasses) {
        for (Activity activity : activityLinkedList) {
            if (Arrays.asList(activityClasses).contains(activity.getClass())) {

                activity.finish();
            }
        }
        return instance;
    }

    //结束所有的Activities
    public FinishActivity finshAllActivities() {
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }
        return instance;
    }

}

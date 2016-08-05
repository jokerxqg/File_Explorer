package utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;

import com.joker.explorer.activity.AboutMeActivity;
import com.joker.explorer.activity.InternalStorageActivity;
import com.joker.explorer.activity.MainActivity;

/**
 * Created by joker on 2016/8/2/002.
 */
public class JumpAct {

    Activity activity = null;

    public JumpAct(Activity activity) {
        this.activity = activity;
    }

    public void jumpToSystemStorage() {
        Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
        activity.startActivity(intent);
    }

    public void jumpToInternalStorage() {
        Intent intent = new Intent(activity, InternalStorageActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void jumpToAboutMe() {
        Intent intent = new Intent(activity, AboutMeActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void jumpToHome() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }


}

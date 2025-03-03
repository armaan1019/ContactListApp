package edu.lakeland.mycontactlist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class Navbar {
    public static void initListButton(Activity activity) {
        ImageButton ibActivity = activity.findViewById(R.id.imageButtonList);
        setUpClickListenerEvent(ibActivity, activity, ContactListActivity.class);
    }

    public static void initMapButton(Activity activity) {
        ImageButton ibActivity = activity.findViewById(R.id.imageButtonMap);
        setUpClickListenerEvent(ibActivity, activity, ContactMapActivity.class);
    }

    public static void initSettingsButton(Activity activity) {
        ImageButton ibActivity = activity.findViewById(R.id.imageButtonSettings);
        setUpClickListenerEvent(ibActivity, activity, ContactSettingsActivity.class);
    }

    public static void setUpClickListenerEvent(ImageButton imageButton, Activity fromActivity, Class<?> targetClass) {
        imageButton.setEnabled(fromActivity.getClass() != targetClass); //Enable if fromActivity is not the same as target activity
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to another screen
                Intent intent = new Intent(fromActivity, targetClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                fromActivity.startActivity(intent);
            }
        });
    }
}
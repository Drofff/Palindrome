package com.drofff.palindrome.utils;

import com.drofff.palindrome.type.FirebaseNotification;
import org.json.simple.JSONObject;

import static com.drofff.palindrome.utils.JsonUtils.getJSONObjectFromFileByKey;

public class FirebaseNotificationUtils {

    private static final String FIREBASE_NOTIFICATIONS_FILE = "firebase_notifications.json";

    private static final String TWO_STEP_AUTH_NOTIFICATION_KEY = "two-step-auth";

    private FirebaseNotificationUtils() {}

    public static FirebaseNotification getTwoStepAuthNotification() {
        JSONObject notificationJson = getJSONObjectFromFileByKey(FIREBASE_NOTIFICATIONS_FILE, TWO_STEP_AUTH_NOTIFICATION_KEY);
        return FirebaseNotification.fromJSONObject(notificationJson);
    }

}

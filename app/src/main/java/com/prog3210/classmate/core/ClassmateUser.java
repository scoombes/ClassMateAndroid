/*
    ClassmateUser.java

    This class extends the ParseUser class but adds some helper functions.
    For all intents and purposes this IS a ParseUser, it even uses the '_User' parse class.

    Kyle Zimmerman, Justin Coschi, Sean Coombes
 */
package com.prog3210.classmate.core;

import android.content.Context;
import android.content.Intent;

import com.parse.ParseClassName;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.prog3210.classmate.user.UserLoginActivity;

import java.util.ArrayList;

@ParseClassName("_User")
public class ClassmateUser extends ParseUser {

    /***
     * Gets the First Name of the User from the ParseObject
     * @return Returns the first name of the user, or null if it has not been set
     */
    public String getFirstName() {
        return getString("firstName");
    }

    /***
     * Sets the first name of the user on parse
     * @param firstName The name to set the first name to
     */
    public void setFirstName(String firstName) {
        put("firstName", firstName);
    }

    /***
     * Gets the last name of the user on parse
     * @return The name to set the last name to
     */
    public String getLastName() {
        return getString("lastName");
    }

    /***
     * Sets the last name of the user on parse
     * @param firstName
     */
    public void setLastName(String firstName) {
        put("lastName", firstName);
    }

    /***
     * Logs the user out of the application and out of parse.
     * It then sends the user to the login activity.
     * @param context The context to use to start the login activity
     */
    public static void logOut(Context context) {
        //Leave all channels
        ParseInstallation.getCurrentInstallation().put("channels", new ArrayList<String>());
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //Logs the user out and sends them to the Login Activity
        ParseUser.logOut();
        Intent loginIntent = new Intent(context, UserLoginActivity.class);
        context.startActivity(loginIntent);
    }

    /***
     * Gets the current user as a ClassmateUser instead of a ParseUser.
     * @return The ClassmateUser of the currently logged in user. If no user is logged in, returns null.
     */
    public static ClassmateUser getCurrentUser() {
        return (ClassmateUser)ParseUser.getCurrentUser();
    }
}

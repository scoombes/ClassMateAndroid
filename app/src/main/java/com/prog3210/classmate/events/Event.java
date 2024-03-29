package com.prog3210.classmate.events;

import com.parse.CountCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.prog3210.classmate.core.EventType;
import com.prog3210.classmate.courses.Course;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ParseClassName("Event")
public class Event extends ParseObject {
    public Course getCourse() {
        return (Course) getParseObject("course");
    }
    public void setCourse(Course course) {
        put("course", course);
    }

    public ParseUser getCreator() {
        return getParseUser("creator");
    }
    public void setCreator(ParseUser creator) {
        put("creator", creator);
    }

    public String getDescription() {
        return getString("description");
    }
    public void setDescription(String description) {
        put("description", description);
    }

    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        put("name", name);
    }

    private SaveCallback createUpvoteCallback(final VoteCallback callback) {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(VoteCallback.UPVOTE, e);
            }
        };
    }

    private SaveCallback createNeutralCallback(final VoteCallback callback) {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(VoteCallback.NEUTRAL, e);
            }
        };
    }

    private SaveCallback createDownvoteCallback(final VoteCallback callback) {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(VoteCallback.DOWNVOTE, e);
            }
        };
    }

    public int getUpvotes() { return getInt("upvotes"); }
    public void hasUpvoted(CountCallback callback) {
        ParseRelation<ParseUser> upvoters = getRelation("upvoters");
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> upvoteQuery = upvoters.getQuery();
        upvoteQuery.whereEqualTo("objectId", currentUser.getObjectId());
        upvoteQuery.countInBackground(callback);
    }
    public void upvote(final VoteCallback callback) {
        final ParseRelation<ParseUser> upvoters = getRelation("upvoters");
        final ParseRelation<ParseUser> downvoters = getRelation("downvoters");

        hasUpvoted(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (count == 1) {
                    increment("upvotes", -1);
                    upvoters.remove(ParseUser.getCurrentUser());

                    saveInBackground(createNeutralCallback(callback));
                } else {
                    hasDownvoted(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {
                            if (count == 1) {
                                increment("downvotes", -1);
                                downvoters.remove(ParseUser.getCurrentUser());
                            }
                            increment("upvotes", 1);
                            upvoters.add(ParseUser.getCurrentUser());

                            saveInBackground(createUpvoteCallback(callback));
                        }
                    });
                }
            }
        });
    }

    public int getDownvotes() { return getInt("downvotes"); }
    public void hasDownvoted(CountCallback callback) {
        ParseRelation<ParseUser> downvoters = getRelation("downvoters");
        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> downvoteQuery = downvoters.getQuery();
        downvoteQuery.whereEqualTo("objectId", currentUser.getObjectId());
        downvoteQuery.countInBackground(callback);
    }
    public void downvote(final VoteCallback callback) {
        final ParseRelation<ParseUser> upvoters = getRelation("upvoters");
        final ParseRelation<ParseUser> downvoters = getRelation("downvoters");

        hasDownvoted(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (count == 1) {
                    increment("downvotes", -1);
                    downvoters.remove(ParseUser.getCurrentUser());

                    saveInBackground(createNeutralCallback(callback));
                } else {
                    hasUpvoted(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {
                            if (count == 1) {
                                increment("upvotes", -1);
                                upvoters.remove(ParseUser.getCurrentUser());
                            }
                            increment("downvotes", 1);
                            downvoters.add(ParseUser.getCurrentUser());

                            saveInBackground(createDownvoteCallback(callback));
                        }
                    });
                }
            }
        });
    }

    public double getFinalGradeWeight() {
        return getDouble("finalGradeWeight");
    }
    public void setFinalGradeWeight(double gradeWeight) {
        put("finalGradeWeight", gradeWeight);
    }

    public void setDate(Date date) {
        put("dueDate", date);
    }
    public Date getDate() {
        return getDate("dueDate");
    }
    public String getDateString() {
        DateFormat format = new SimpleDateFormat("EEE MMM d");

        return format.format(getDate());
    }

    public static ParseQuery<Event> getQuery() {
        return new ParseQuery<Event>(Event.class);
    }
    public EventType getEventType() {
        return (EventType)getParseObject("eventtype");
    }
    public void setEventType(EventType eventType) { put("eventtype", eventType); }
}
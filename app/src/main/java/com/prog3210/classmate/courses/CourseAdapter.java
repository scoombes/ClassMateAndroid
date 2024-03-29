package com.prog3210.classmate.courses;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.prog3210.classmate.R;

/**
 * Created by kzimmerman on 11/18/2015.
 */
public class CourseAdapter extends ParseQueryAdapter<Course> {

    boolean showDetails = true;

    private String searchTerm;

    public void setShowDetails(boolean showDetails) {
        this.showDetails = showDetails;
    }

    public enum FilterMode {
        All,
        Joined,
        Unjoined
    }

    boolean spinnerSupportEnabled = false;

    public CourseAdapter(Context context, FilterMode mode) {
        super(context, createQueryFactory(mode));
    }

    public void setSearchTerm(String query) {
        searchTerm = query;

        if (searchTerm != null) {
            searchTerm = searchTerm.toLowerCase();
        }
    }


    public void enableSpinnerSupport() {
        spinnerSupportEnabled = true;
    }

    @Override
    public int getViewTypeCount() {
        return spinnerSupportEnabled ? 1 : 2;
    }

    @Override
    public int getItemViewType(int position) {
        Course course = getItem(position);

        boolean matchesFilter = (searchTerm == null || searchTerm.length() == 0)
            || course.getName().toLowerCase().contains(searchTerm)
            || course.getCourseCode().toLowerCase().contains(searchTerm)
            || course.getTeacherName().toLowerCase().contains(searchTerm);

        if (matchesFilter) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public View getItemView(Course course, View view, ViewGroup parent) {

        if (view == null) {
            view = View.inflate(getContext(), R.layout.course_list_item, null);
        }

        super.getItemView(course, view, parent);

        CourseItemView courseItemView = (CourseItemView) view;
        courseItemView.updateValues(course, showDetails);

        return view;
    }

    @Override
    public View getNextPageView(View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.empty_view, null);
        }

        return v;
    }

    private static QueryFactory<Course> createQueryFactory(final FilterMode filterMode) {
        QueryFactory<Course> factory = new QueryFactory<Course>() {
            @Override
            public ParseQuery<Course> create() {
                ParseQuery<Course> query = Course.getQuery();
                query.include("semester");

                if (filterMode == FilterMode.Joined) {
                    query.whereEqualTo("members", ParseUser.getCurrentUser());
                } else if (filterMode == FilterMode.Unjoined) {
                    query.whereNotEqualTo("members", ParseUser.getCurrentUser());
                }

                query.orderByAscending("courseCode");

                return query;
            }
        };

        return factory;
    }
}

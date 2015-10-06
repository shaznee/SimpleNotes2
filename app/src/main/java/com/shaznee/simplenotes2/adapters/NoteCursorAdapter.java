package com.shaznee.simplenotes2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.shaznee.simplenotes2.R;
import com.shaznee.simplenotes2.database.DBOpenHelper;

/**
 * Created by MOHAMED SHAZNEE on 06-Oct-15.
 */
public class NoteCursorAdapter extends CursorAdapter {

    public NoteCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String noteText = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
        int pos = noteText.indexOf(10);
        if (pos != -1) {
            noteText = noteText.substring(0, pos) + "...";
        }
        TextView tv = (TextView) view.findViewById(R.id.tvNote);
        tv.setText(noteText);
    }
}

package com.shaznee.simplenotes2.activities;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.shaznee.simplenotes2.R;
import com.shaznee.simplenotes2.adapters.NoteCursorAdapter;
import com.shaznee.simplenotes2.database.DBOpenHelper;
import com.shaznee.simplenotes2.database.NotesProvider;

public class MainScreen extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainScreen.class.getSimpleName();
    private static final int EDITOR_REQUEST_CODE = 1001;

    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        cursorAdapter = new NoteCursorAdapter(this, null, 0);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(cursorAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_create_sample:
                insertSampleDate();
                break;
            case R.id.action_delete_all:
                deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertSampleDate() {
        insertNote("Simple note");
        insertNote("Multi-line\nnote");
        insertNote("Very long note with a lot of text that exceed the width of the screen");
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void deleteAll() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
                            getContentResolver().delete(NotesProvider.CONTENT_URI, null,null);
                            restartLoader();
                            Toast.makeText(MainScreen.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

    }


    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        Log.d(TAG, "Inserted Note " + noteUri.getLastPathSegment());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, NotesProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void newNoteButtonClicked(View view){
        Intent intent = new Intent(this, NoteEditor.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}

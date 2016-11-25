package com.thinkdifferent.noteapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.thinkdifferent.noteapp.R;
import com.thinkdifferent.noteapp.model.Note;
import com.thinkdifferent.noteapp.util.Utility;
import com.thinkdifferent.noteapp.view.activity.AddUpdateNoteActivity;

import java.util.List;
import java.util.Locale;

/**
 * adapter class to show notes in recyclerView.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewsHolder>
        implements View.OnClickListener {

    // object members.
    private Context context;
    private List<Note> notes;
    private TypedArray notesImages;

    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
        notesImages = context.getResources().obtainTypedArray(R.array.notes_image);
    }

    @Override
    public ViewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate view to show it in recycler view.
        View itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_note_item, parent, false);
        ViewsHolder viewsHolder = new ViewsHolder(itemView);
        viewsHolder.optionsImageView.setOnClickListener(this);
        viewsHolder.typeImageView.setOnClickListener(this);
        return viewsHolder;
    }

    @Override
    public void onBindViewHolder(ViewsHolder viewsHolder, int position) {
        Note note = notes.get(position);
        // set data to views.
        viewsHolder.noteLayout.setBackgroundColor(Color.parseColor(note.backgroundColor));
        viewsHolder.titleTexView.setText(note.title);
        viewsHolder.subjectTextView.setText(note.subject);
        viewsHolder.lastUpdateTextView.setText(String.format(Locale.US, context.getString(R.string.last_update), note.lastUpdate));
        viewsHolder.typeImageView.setImageResource(notesImages.getResourceId(note.type, 0));
        // set tag to views with item position.
        viewsHolder.optionsImageView.setTag(R.id.item_position, position);
        viewsHolder.typeImageView.setTag(R.id.item_position, position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public void onClick(View view) {
        int itemPosition = (int) view.getTag(R.id.item_position);
        switch (view.getId()) {
            case R.id.iv_options:
                // show popup menu to user.
                showPopupMenu(view, itemPosition);
                break;
            case R.id.iv_type_action:
                Note note = notes.get(itemPosition);
                switch (note.type) {
                    case 0:
                        // open link in browser.
                        Utility.openLink(view.getContext(), note.subject);
                        break;
                    case 1:
                        // send mail
                        Utility.sendMail(view.getContext(), note.subject);
                        break;
                    case 2:
                        // make a call.
                        Utility.makeCall(view.getContext(), note.subject);
                        break;
                }
                break;

        }
    }

    /**
     * show options popup menu to user.
     *
     * @param view         view that user clicked.
     * @param itemPosition clicked item position.
     */
    private void showPopupMenu(final View view, final int itemPosition) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.options_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_update) {
                    // update note.
                    Note note = notes.get(itemPosition);
                    ((AppCompatActivity) context).startActivityForResult(new Intent(view.getContext(), AddUpdateNoteActivity.class)
                            .putExtra("update_note", true).putExtra("note", note), 100);
                } else {
                    // delete note.
                    notes.remove(itemPosition);
                    notifyItemRangeRemoved(itemPosition, notes.size());
                }
                return true;
            }
        });

        popup.show(); //showing popup menu
    }


    /**
     * holder class view note view item in recyclerView.
     */
    class ViewsHolder extends RecyclerView.ViewHolder {
        //GUI References.
        TableLayout noteLayout;
        TextView titleTexView, subjectTextView, lastUpdateTextView;
        ImageView optionsImageView, typeImageView;

        ViewsHolder(View itemView) {
            super(itemView);
            noteLayout = (TableLayout) itemView.findViewById(R.id.tl_note_info);
            titleTexView = (TextView) itemView.findViewById(R.id.tv_note_title);
            subjectTextView = (TextView) itemView.findViewById(R.id.tv_note_subject);
            lastUpdateTextView = (TextView) itemView.findViewById(R.id.tv_last_update);
            optionsImageView = (ImageView) itemView.findViewById(R.id.iv_options);
            typeImageView = (ImageView) itemView.findViewById(R.id.iv_type_action);
        }
    }
}


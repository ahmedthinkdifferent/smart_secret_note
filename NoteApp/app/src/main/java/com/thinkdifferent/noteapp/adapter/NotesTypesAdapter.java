package com.thinkdifferent.noteapp.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinkdifferent.noteapp.R;

/**
 * class to show notes types in spinner.
 */

public class NotesTypesAdapter extends BaseAdapter {

    //Objects.
    private Context context;
    private TypedArray notesImages;
    private String[] notesTypes;

    /**
     * constructor.
     *
     * @param context caller context.
     */
    public NotesTypesAdapter(Context context) {
        this.context = context;
        notesImages = context.getResources().obtainTypedArray(R.array.notes_image);
        notesTypes = context.getResources().getStringArray(R.array.notes_types);
    }

    @Override
    public int getCount() {
        return notesImages.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewsHolder viewsHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_note_type, viewGroup, false);
            viewsHolder = new ViewsHolder(view);
            view.setTag(viewsHolder);
        } else {
            viewsHolder = (ViewsHolder) view.getTag();
        }
        // set data to views.
        viewsHolder.noteTypeImageView.setImageResource(notesImages.getResourceId(position, 0));
        viewsHolder.noteTypeTextView.setText(notesTypes[position]);
        return view;
    }


    /**
     * views holder class.
     */
    private class ViewsHolder {
        ImageView noteTypeImageView;
        TextView noteTypeTextView;

        ViewsHolder(View itemView) {
            noteTypeImageView = (ImageView) itemView.findViewById(R.id.iv_note_type);
            noteTypeTextView = (TextView) itemView.findViewById(R.id.tv_note_type);
        }
    }
}

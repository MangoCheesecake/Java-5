package com.example.kahermans.phonebookdemo;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SentenceAdapter2 extends ArrayAdapter<Sentence> {
    private Context mContext;
    private List<Sentence> sentenceList = new ArrayList<Sentence>();

    public SentenceAdapter2( Context context, ArrayList<Sentence> list)
    {
        super( context, 0, list);
        mContext = context;
        sentenceList = list;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        // Associates the list with the XML Layout file "contact_view"
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.contact_view,parent,false);

        // Individually handles each Contact in the contactList
        Sentence currentSentence = sentenceList.get(position);

        // Extracts the name of the Contact
        TextView sentence = (TextView) listItem.findViewById(R.id.textView_Sentence);
        sentence.setText(currentSentence.getSentence());

        return listItem;
    }
}
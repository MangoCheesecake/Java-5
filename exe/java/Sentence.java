package com.example.kahermans.phonebookdemo;

public class Sentence
{
    private String sentence;
    private String author;
    private String uid;

    public Sentence()
    {
        sentence  ="NA";
        author = "NA";
    }

    public Sentence( String sentence, String author, String uid )
    {
        this.sentence = sentence;
        this.author = author;
        this.uid = uid;
    }

    public String getSentence()
    {
        return sentence;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getUid()
    {
        return uid;
    }
    public String toString()
    {
        return sentence + ": " + author;
    }
}

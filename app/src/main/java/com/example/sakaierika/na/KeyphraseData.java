package com.example.sakaierika.na;

/**
 * Created by sakaierika on 2015/06/18.
 */

public class KeyphraseData {
    private CharSequence mKeyphrase;
    private CharSequence mScore;
    private CharSequence[] mRelativeTitle = new CharSequence[4];
    private CharSequence[] mRelativeLink = new CharSequence[4];


    public CharSequence getKeyphrase() {
        return mKeyphrase;
    }

    public void setKeyphrase(CharSequence keyphrase) {
        mKeyphrase = keyphrase;
    }

    public CharSequence getmScore() {
        return mScore;
    }

    public void setmScore(CharSequence score) {
        mScore = score;
    }

    public CharSequence[] getmRelativeTitle() {
        return mRelativeTitle;
    }

    public void setmRelativeTitle(CharSequence[] relativeTitle) {
        mRelativeTitle = relativeTitle;
    }

    public CharSequence[] getmRelativeLink() {
        return mRelativeLink;
    }

    public void setmRelativeLink(CharSequence[] relativelink) {
        mRelativeLink = relativelink;
    }



}


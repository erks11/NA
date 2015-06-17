package com.example.sakaierika.na;

/**
 * Created by sakaierika on 2015/06/14.
 */
public class Item {
    private CharSequence mTitle;
    private CharSequence mDescription;
    private CharSequence mDate;
    private CharSequence mLink;
    private CharSequence mImgURL;

    public Item(){
        mTitle = "";
        mDescription="";
        mDate="";
        mLink="";
        mImgURL="";
    }

    public CharSequence getDescription(){
        return mDescription;
    }

    public void setDescription(CharSequence description){
        mDescription = description;
    }

    public CharSequence getTitle(){
        return mTitle;
    }

    public void setTitle(CharSequence title){
        mTitle = title;
    }

    public CharSequence getDate() { return mDate; }

    public void setDate(CharSequence date) {mDate = date; }

    public CharSequence getLink() { return mLink; }

    public void setLink(CharSequence link) {mLink = link; }

    public CharSequence getImgURL() { return mImgURL; }

    public void setImgURL(CharSequence imgURL) {mImgURL = imgURL; }

}

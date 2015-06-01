package com.androidbegin.jsouptutorial;

/*
 *Item class for the listview to display images and texts
 */

public class Item {

    private String imageLink;
    private String articleTitle;
    private String link;

    public Item(){
        super();
    }

    public Item(String title, String link, String img) {
        super();
        this.articleTitle = title;
        this.link = link;
        this.imageLink = img;
    }

    public Item(String title, String link) {
        super();
        this.articleTitle = title;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return articleTitle;
    }

    public void setName(String title) {
        this.articleTitle = title;
    }

    @Override
    public String toString() {
        return this.articleTitle;
    }

    public void setImage(String img){
        this.imageLink = img;
    }

    public String getImg(){
        return imageLink;
    }
}

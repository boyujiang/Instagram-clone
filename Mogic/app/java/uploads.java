package com.cas.jiamin.mogic.Utility;

/**
 * uploads class
 *
 * The class represents a single post uploaded by user, which contains the
 * content(description of the post),
 * likes(the number of like of the post),
 * username(the username of the user who create the post),
 * url(the download url of the image),
 * uid(the user id of the user who create the post),
 * time(the time when the post was uploaded).
 */
public class uploads {
    private String contents;
    private int likes;
    private String username;
    private String url;
    private String uid;
    private String time;
    //private List<String> comments;

    /**
     * The basic constructor takes no arguments, establish a empty object
     */
    public  uploads() {

    }

    /**
     * The constructor takes six arguments, establish a object with the given
     * arguments.
     *
     * @param contents The description of the post.
     * @param like The number of the likes of the post.
     * @param uname The username of the user who create the post.
     * @param url The download url of the image.
     * @param uid The user id of the user who create the post.
     * @param time The time when the post was uploaded.
     */
    public uploads(String contents, int like, String uname, String url, String uid, String time){
        this.contents = contents;
        this.likes = likes;
        this.url = url;
        this.username = uname;
        this.time = time;
        this.uid = uid;
        //this.comments = comments;
    }

    //public void setContent(String con){this.contents = contents;}

    /**
     * A simple method takes no argument and return the contents of the uploads object
     *
     * @return The contents of the uploads object
     */
    public  String getContents() {return this.contents; }

    /**
     * A simple method takes no argument and return the username of the uploads object
     *
     * @return The username of the uploads object
     */
    public String getUsername(){ return this.username; }

    /**
     * A simple method takes no argument and return the url of the uploads object
     *
     * @return The url of the uploads object
     */
    public String getUrl(){return this.url; }

    /**
     * A simple method takes no argument and return the likes number of the uploads object
     *
     * @return The likes number of the uploads object
     */
    public int getLikes(){return this.likes; }

    /*public List<String> getComments(){
        return this.comments;
    }*/
}

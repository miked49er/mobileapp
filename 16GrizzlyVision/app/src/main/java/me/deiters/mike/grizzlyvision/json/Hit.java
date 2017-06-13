package me.deiters.mike.grizzlyvision.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hit {

    @SerializedName("previewHeight")
    @Expose
    private long previewHeight;
    @SerializedName("likes")
    @Expose
    private long likes;
    @SerializedName("favorites")
    @Expose
    private long favorites;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("webformatHeight")
    @Expose
    private long webformatHeight;
    @SerializedName("views")
    @Expose
    private long views;
    @SerializedName("webformatWidth")
    @Expose
    private long webformatWidth;
    @SerializedName("previewWidth")
    @Expose
    private long previewWidth;
    @SerializedName("comments")
    @Expose
    private long comments;
    @SerializedName("downloads")
    @Expose
    private long downloads;
    @SerializedName("pageURL")
    @Expose
    private String pageURL;
    @SerializedName("previewURL")
    @Expose
    private String previewURL;
    @SerializedName("webformatURL")
    @Expose
    private String webformatURL;
    @SerializedName("imageWidth")
    @Expose
    private long imageWidth;
    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("userImageURL")
    @Expose
    private String userImageURL;
    @SerializedName("imageHeight")
    @Expose
    private long imageHeight;

    public long getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(long previewHeight) {
        this.previewHeight = previewHeight;
    }

    public Hit withPreviewHeight(long previewHeight) {
        this.previewHeight = previewHeight;
        return this;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public Hit withLikes(long likes) {
        this.likes = likes;
        return this;
    }

    public long getFavorites() {
        return favorites;
    }

    public void setFavorites(long favorites) {
        this.favorites = favorites;
    }

    public Hit withFavorites(long favorites) {
        this.favorites = favorites;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Hit withTags(String tags) {
        this.tags = tags;
        return this;
    }

    public long getWebformatHeight() {
        return webformatHeight;
    }

    public void setWebformatHeight(long webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public Hit withWebformatHeight(long webformatHeight) {
        this.webformatHeight = webformatHeight;
        return this;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public Hit withViews(long views) {
        this.views = views;
        return this;
    }

    public long getWebformatWidth() {
        return webformatWidth;
    }

    public void setWebformatWidth(long webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public Hit withWebformatWidth(long webformatWidth) {
        this.webformatWidth = webformatWidth;
        return this;
    }

    public long getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(long previewWidth) {
        this.previewWidth = previewWidth;
    }

    public Hit withPreviewWidth(long previewWidth) {
        this.previewWidth = previewWidth;
        return this;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public Hit withComments(long comments) {
        this.comments = comments;
        return this;
    }

    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    public Hit withDownloads(long downloads) {
        this.downloads = downloads;
        return this;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public Hit withPageURL(String pageURL) {
        this.pageURL = pageURL;
        return this;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public Hit withPreviewURL(String previewURL) {
        this.previewURL = previewURL;
        return this;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public Hit withWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
        return this;
    }

    public long getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(long imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Hit withImageWidth(long imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Hit withUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Hit withUser(String user) {
        this.user = user;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Hit withType(String type) {
        this.type = type;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Hit withId(long id) {
        this.id = id;
        return this;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public Hit withUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
        return this;
    }

    public long getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(long imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Hit withImageHeight(long imageHeight) {
        this.imageHeight = imageHeight;
        return this;
    }

}

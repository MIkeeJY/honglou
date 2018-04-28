package com.hlsp.video.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoListItem implements Parcelable {

    /**
     * video_name : 精灵梦叶罗丽：思思和王默不对劲，没想到是失去了叶罗丽的记忆
     * video_duration : 44
     * video_coverURL : http://p0.qhimg.com/video/568_320_70/t01da853c42d79dcb4c.webp
     * video_definition :
     * video_author_name : katvvv
     * video_label_level1 : 游戏
     * video_extData : eyJzdHJhdGVneSI6IjIuNi41MS43MS5iOGFuMDRyMWxxbmouOS40MjQuMTMudmdjbG50Li4xLjM5NXF5YWVpeWtvbSIsImNoYW5uZWxfaWQiOiIyNCJ9
     * video_pubtime : 1512057600
     * video_author_avatarURL : http://p0.qhimg.com/t01223d44a1cc1eb500.jpg
     * video_size : 2790400
     * video_show_mode : 2
     * video_author_id : 1902252097
     * video_count_play : 88000
     * video_source : 360
     * video_id : PnB04yQb12o3
     * video_count_digg : 61
     * video_count_collect : 511
     * video_count_share : 1021
     * video_playURL : 1
     * video_statue : 正常
     * video_label_challenge1 : 镜头不动也能玩运镜?!
     * video_count_comment : 251
     * video_desc : 镜头不动也能玩运镜！哈哈哈哈啊哈哈哈哈一脸懵逼的biubiu
     */

    private String video_name;
    private int video_duration;
    private String video_coverURL;
    private String video_definition;
    private String video_author_name;
    private String video_label_level1;
    private String video_extData;
    private String video_pubtime;
    private String video_author_avatarURL;
    private String video_size;
    private String video_show_mode;
    private String video_author_id;
    private long video_count_play;
    private String video_source;
    private String video_id;
    private long video_count_digg;
    private long video_count_collect;
    private long video_count_share;
    private String video_playURL;
    private String video_statue;
    private String video_label_challenge1;
    private long video_count_comment;
    private String video_desc;

    int seleted;


    public String getVideo_name() {
        return video_name;
    }

    public int getVideo_duration() {
        return video_duration;
    }

    public String getVideo_coverURL() {
        return video_coverURL;
    }

    public String getVideo_definition() {
        return video_definition;
    }

    public String getVideo_author_name() {
        return video_author_name;
    }

    public String getVideo_label_level1() {
        return video_label_level1;
    }

    public String getVideo_extData() {
        return video_extData;
    }

    public String getVideo_pubtime() {
        return video_pubtime;
    }

    public String getVideo_author_avatarURL() {
        return video_author_avatarURL;
    }

    public String getVideo_size() {
        return video_size;
    }

    public String getVideo_show_mode() {
        return video_show_mode;
    }

    public String getVideo_author_id() {
        return video_author_id;
    }

    public long getVideo_count_play() {
        return video_count_play;
    }

    public String getVideo_source() {
        return video_source;
    }

    public String getVideo_id() {
        return video_id;
    }

    public long getVideo_count_digg() {
        return video_count_digg;
    }

    public long getVideo_count_collect() {
        return video_count_collect;
    }

    public long getVideo_count_share() {
        return video_count_share;
    }

    public String getVideo_playURL() {
        return video_playURL;
    }

    public String getVideo_statue() {
        return video_statue;
    }

    public String getVideo_label_challenge1() {
        return video_label_challenge1;
    }

    public long getVideo_count_comment() {
        return video_count_comment;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.video_name);
        dest.writeInt(this.video_duration);
        dest.writeString(this.video_coverURL);
        dest.writeString(this.video_definition);
        dest.writeString(this.video_author_name);
        dest.writeString(this.video_label_level1);
        dest.writeString(this.video_extData);
        dest.writeString(this.video_pubtime);
        dest.writeString(this.video_author_avatarURL);
        dest.writeString(this.video_size);
        dest.writeString(this.video_show_mode);
        dest.writeString(this.video_author_id);
        dest.writeLong(this.video_count_play);
        dest.writeString(this.video_source);
        dest.writeString(this.video_id);
        dest.writeLong(this.video_count_digg);
        dest.writeLong(this.video_count_collect);
        dest.writeLong(this.video_count_share);
        dest.writeString(this.video_playURL);
        dest.writeString(this.video_statue);
        dest.writeString(this.video_label_challenge1);
        dest.writeLong(this.video_count_comment);
        dest.writeString(this.video_desc);
    }

    public VideoListItem() {}

    protected VideoListItem(Parcel in) {
        this.video_name = in.readString();
        this.video_duration = in.readInt();
        this.video_coverURL = in.readString();
        this.video_definition = in.readString();
        this.video_author_name = in.readString();
        this.video_label_level1 = in.readString();
        this.video_extData = in.readString();
        this.video_pubtime = in.readString();
        this.video_author_avatarURL = in.readString();
        this.video_size = in.readString();
        this.video_show_mode = in.readString();
        this.video_author_id = in.readString();
        this.video_count_play = in.readLong();
        this.video_source = in.readString();
        this.video_id = in.readString();
        this.video_count_digg = in.readLong();
        this.video_count_collect = in.readLong();
        this.video_count_share = in.readLong();
        this.video_playURL = in.readString();
        this.video_statue = in.readString();
        this.video_label_challenge1 = in.readString();
        this.video_count_comment = in.readLong();
        this.video_desc = in.readString();
    }

    public static final Parcelable.Creator<VideoListItem> CREATOR = new Parcelable.Creator<VideoListItem>() {
        @Override
        public VideoListItem createFromParcel(Parcel source) {
            return new VideoListItem(source);
        }

        @Override
        public VideoListItem[] newArray(int size) {
            return new VideoListItem[size];
        }
    };


    public int getSeleted() {
        return seleted;
    }


    public void setSeleted(int seleted) {
        this.seleted = seleted;
    }


    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public void setVideo_duration(int video_duration) {
        this.video_duration = video_duration;
    }

    public void setVideo_coverURL(String video_coverURL) {
        this.video_coverURL = video_coverURL;
    }

    public void setVideo_definition(String video_definition) {
        this.video_definition = video_definition;
    }

    public void setVideo_author_name(String video_author_name) {
        this.video_author_name = video_author_name;
    }

    public void setVideo_label_level1(String video_label_level1) {
        this.video_label_level1 = video_label_level1;
    }

    public void setVideo_extData(String video_extData) {
        this.video_extData = video_extData;
    }

    public void setVideo_pubtime(String video_pubtime) {
        this.video_pubtime = video_pubtime;
    }

    public void setVideo_author_avatarURL(String video_author_avatarURL) {
        this.video_author_avatarURL = video_author_avatarURL;
    }

    public void setVideo_size(String video_size) {
        this.video_size = video_size;
    }

    public void setVideo_show_mode(String video_show_mode) {
        this.video_show_mode = video_show_mode;
    }

    public void setVideo_author_id(String video_author_id) {
        this.video_author_id = video_author_id;
    }

    public void setVideo_count_play(long video_count_play) {
        this.video_count_play = video_count_play;
    }

    public void setVideo_source(String video_source) {
        this.video_source = video_source;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setVideo_count_digg(long video_count_digg) {
        this.video_count_digg = video_count_digg;
    }

    public void setVideo_count_collect(long video_count_collect) {
        this.video_count_collect = video_count_collect;
    }

    public void setVideo_count_share(long video_count_share) {
        this.video_count_share = video_count_share;
    }

    public void setVideo_playURL(String video_playURL) {
        this.video_playURL = video_playURL;
    }

    public void setVideo_statue(String video_statue) {
        this.video_statue = video_statue;
    }

    public void setVideo_label_challenge1(String video_label_challenge1) {
        this.video_label_challenge1 = video_label_challenge1;
    }

    public void setVideo_count_comment(long video_count_comment) {
        this.video_count_comment = video_count_comment;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }
}

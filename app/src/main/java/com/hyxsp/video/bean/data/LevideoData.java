package com.hyxsp.video.bean.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.hyxsp.video.utils.Utils;


public class LevideoData implements Parcelable {

	public String title;//视频标题
	public String authorImgUrl;//作者头像地址
	public String authorName;//作者名称
	public String authorSignature;//作者签名
	public int authorSex;//作者性别
	public String coverImgUrl;//视频封面图片地址
	public String videoPlayUrl;//视频播放地址
	public String videoDownloadUrl;//视频下载地址
	public int videoWidth;//视频宽度
	public int videoHeight;//视频高度
	public int playCount;//播放次数
	public int likeCount;//点赞次数
	public long createTime;//创建时间

	//抖音专用
	public String musicImgUrl;//音乐图片
	public String musicName;//音乐名称
	public String musicAuthorName;//音乐作者

	//火山专用
	public String videoDuration;//视频时长(秒拍也用到)
	public String authorCity;//作者所在城市
	public String authorAge;//作者年龄

	//为了更好的效率问题提前格式化内容字段
	public String formatTimeStr = "";
	public String filterTitleStr = "";
	public String filterUserNameStr = "";
	public String formatPlayCountStr = "";
	public String formatLikeCountStr = "";
	public String filterMusicNameStr = "";

	//视频的Base64值
	public int type = 0;//视频类型：1 抖音 2 火山 3 快手 4 秒拍

	public static final String TABLE_NAME = "local_levideo_data";

	public interface Columns extends BaseColumns {
		public final static String IMG_URL = "img_url";
		public final static String TITLE = "title";
		public final static String VIEW_COUNT = "view_count";
		public final static String LOCAL_PATH = "local_path";
		public final static String VIDEO_TYPE = "video_type";
		public final static String SAVE_TIME = "save_time";
		public final static String DURATION_TIME = "duration_time";
	}

	public ContentValues toContentValues() {
		ContentValues c = new ContentValues();
		c.put(Columns.IMG_URL, coverImgUrl);
		c.put(Columns.TITLE, title);
		c.put(Columns.VIEW_COUNT, playCount);
		c.put(Columns.LOCAL_PATH, videoPlayUrl);
		c.put(Columns.VIDEO_TYPE, type);
		c.put(Columns.SAVE_TIME, createTime);
		c.put(Columns.DURATION_TIME, videoDuration);
		return c;
	}
	
	public LevideoData(){}
	
	public LevideoData fromCursor(Cursor cursor) {
		coverImgUrl = cursor.getString(cursor.getColumnIndex(Columns.IMG_URL));
		title = cursor.getString(cursor.getColumnIndex(Columns.TITLE));
		playCount = cursor.getInt(cursor.getColumnIndex(Columns.VIEW_COUNT));
		formatPlayCountStr = Utils.formatNumber(playCount);
		videoPlayUrl = cursor.getString(cursor.getColumnIndex(Columns.LOCAL_PATH));
		type = cursor.getInt(cursor.getColumnIndex(Columns.VIDEO_TYPE));
		createTime = cursor.getLong(cursor.getColumnIndex(Columns.SAVE_TIME));
		videoDuration = cursor.getString(cursor.getColumnIndex(Columns.DURATION_TIME));
		return this;
	}

	public static void onCreateTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + "("
				+ Columns.IMG_URL + " varchar(150),"
				+ Columns.TITLE + " varchar(150),"
				+ Columns.DURATION_TIME + " varchar(100),"
				+ Columns.VIEW_COUNT + " INTEGER,"
				+ Columns.VIDEO_TYPE + " INTEGER,"
				+ Columns.SAVE_TIME + " INTEGER,"
				+ Columns.LOCAL_PATH + " varchar(150)"
				+ ");");
	}
	
	public LevideoData(Parcel source) {
		title = source.readString();
		videoPlayUrl = source.readString();
		videoDownloadUrl = source.readString();
		coverImgUrl = source.readString();
		playCount = source.readInt();
		type = source.readInt();
		createTime = source.readLong();
		videoDuration = source.readString();
		formatPlayCountStr = source.readString();
		musicAuthorName = source.readString();
		musicImgUrl = source.readString();
		musicName = source.readString();
		authorImgUrl = source.readString();
		authorName = source.readString();
		filterUserNameStr = source.readString();
		filterMusicNameStr = source.readString();
		formatLikeCountStr = source.readString();
		authorAge = source.readString();
		authorSignature = source.readString();
		formatTimeStr = source.readString();
    }

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<LevideoData> CREATOR = new Creator<LevideoData>() {
		@Override
		public LevideoData[] newArray(int size) {
			return new LevideoData[size];
		}
		@Override
		public LevideoData createFromParcel(Parcel source) {
			return new LevideoData(source);
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(videoPlayUrl);
		dest.writeString(videoDownloadUrl);
		dest.writeString(coverImgUrl);
		dest.writeInt(playCount);
		dest.writeInt(type);
		dest.writeLong(createTime);
		dest.writeString(videoDuration);
		dest.writeString(formatPlayCountStr);
		dest.writeString(musicAuthorName);
		dest.writeString(musicImgUrl);
		dest.writeString(musicName);
		dest.writeString(authorImgUrl);
		dest.writeString(authorName);
		dest.writeString(filterUserNameStr);
		dest.writeString(filterMusicNameStr);
		dest.writeString(formatLikeCountStr);
		dest.writeString(authorAge);
		dest.writeString(authorSignature);
		dest.writeString(formatTimeStr);
	}


	public String getTitle() {
		return title;
	}

	public String getAuthorImgUrl() {
		return authorImgUrl;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getAuthorSignature() {
		return authorSignature;
	}

	public int getAuthorSex() {
		return authorSex;
	}

	public String getCoverImgUrl() {
		return coverImgUrl;
	}

	public String getVideoPlayUrl() {
		return videoPlayUrl;
	}

	public String getVideoDownloadUrl() {
		return videoDownloadUrl;
	}

	public int getVideoWidth() {
		return videoWidth;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public int getPlayCount() {
		return playCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public long getCreateTime() {
		return createTime;
	}

	public String getMusicImgUrl() {
		return musicImgUrl;
	}

	public String getMusicName() {
		return musicName;
	}

	public String getMusicAuthorName() {
		return musicAuthorName;
	}

	public String getVideoDuration() {
		return videoDuration;
	}

	public String getAuthorCity() {
		return authorCity;
	}

	public String getAuthorAge() {
		return authorAge;
	}

	public String getFormatTimeStr() {
		return formatTimeStr;
	}

	public String getFilterTitleStr() {
		return filterTitleStr;
	}

	public String getFilterUserNameStr() {
		return filterUserNameStr;
	}

	public String getFormatPlayCountStr() {
		return formatPlayCountStr;
	}

	public String getFormatLikeCountStr() {
		return formatLikeCountStr;
	}

	public String getFilterMusicNameStr() {
		return filterMusicNameStr;
	}

	public int getType() {
		return type;
	}
}

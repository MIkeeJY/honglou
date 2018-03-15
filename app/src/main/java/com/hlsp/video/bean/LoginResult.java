package com.hlsp.video.bean;

/**
 * Created by hackest on 2017/6/26.
 */

public class LoginResult {

    /**
     * bopenids : []
     * uid : 243355
     * nim_token : 1731eb129536df4d5125d68db719c643
     * kuocode : 348163
     * refresh_at : 0
     * emoji :
     * moment_black : []
     * hooked_openids : []
     * email : 1572747192@qq.com
     * valid_until : 1499743452
     * openid : 17926f3dccf63ec2eaf4e2c9c897f9cb
     * bmail :
     * kuoimg : []
     * nickname : 1572747192
     * haspwd : 1
     * slogan :
     * arrow_num : 4
     * update_at : 0
     * relation_tree : []
     * token : ec7da7005a1e11e79a56525400eee2cd
     * avatar :
     * exp : 0
     * kuoaudio :
     * zone_auth : 1
     */

    private int uid;
    private String nim_token;
    private int kuocode;
    private int refresh_at;
    private String emoji;
    private String email;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNim_token() {
        return nim_token;
    }

    public void setNim_token(String nim_token) {
        this.nim_token = nim_token;
    }

    public int getKuocode() {
        return kuocode;
    }

    public void setKuocode(int kuocode) {
        this.kuocode = kuocode;
    }

    public int getRefresh_at() {
        return refresh_at;
    }

    public void setRefresh_at(int refresh_at) {
        this.refresh_at = refresh_at;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

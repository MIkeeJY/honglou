package com.hlsp.video.bean;

import java.util.List;

/**
 * Created by hackest on 2018/4/27.
 */

public class AuthorInfoResponse {

    /**
     * aurthor_img : [{"img_url":"http://image.pearvideo.com/node/18-10027897-logo.jpg"}]
     * name : 文娱小队长
     * author_id : pearvideo_11549097
     */

    private String name;
    private String author_id;
    private List<AurthorImgEntity> aurthor_img;

    public String getName() {
        return name;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public List<AurthorImgEntity> getAurthor_img() {
        return aurthor_img;
    }

    public static class AurthorImgEntity {
        /**
         * img_url : http://image.pearvideo.com/node/18-10027897-logo.jpg
         */

        private String img_url;

        public String getImg_url() {
            return img_url;
        }
    }
}

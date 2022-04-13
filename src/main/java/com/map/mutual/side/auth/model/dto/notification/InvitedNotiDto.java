package com.map.mutual.side.auth.model.dto.notification;

import com.map.mutual.side.auth.model.dto.notification.extend.notificationDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

/**
 * Class       : InvitationNotiDto
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-04-13] - 조 준희 - Class Create
 */
public class InvitedNotiDto extends notificationDto {

    @QueryProjection
    public InvitedNotiDto( String userId, String userProfileUrl, String worldName, String userSuid, String worldUserCode ) {
        super("A"); // A 타입 알림.

        payload = new Payload(userId,userProfileUrl,worldName);
        data = new Data(userSuid,worldUserCode);
    }

    @Getter
    private static class Payload{
        private String userId;
        private String userProfileUrl;
        private String worldName;

        public Payload(String userId, String userProfileUrl, String worldName) {
            this.userId = userId;
            this.userProfileUrl = userProfileUrl;
            this.worldName = worldName;
        }
    }

    @Getter
    private static class Data{
        private String userSuid;
        private String worldUserCode;

        public Data(String userSuid, String worldUserCode) {
            this.userSuid = userSuid;
            this.worldUserCode = worldUserCode;
        }
    }

}
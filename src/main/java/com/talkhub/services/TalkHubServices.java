package com.talkhub.services;

import com.talkhub.services.talkhub.auth.TalkHubAuthService;
import com.talkhub.services.talkhub.auth.ITalkHubAuthService;
import com.talkhub.services.talkhub.bookmark.ITalkHubBookmarkService;
import com.talkhub.services.talkhub.bookmark.TalkHubBookmarkService;
import com.talkhub.services.talkhub.category.ITalkHubCategoryService;
import com.talkhub.services.talkhub.category.TalkHubCategoryService;
import com.talkhub.services.talkhub.follow.ITalkHubFollowService;
import com.talkhub.services.talkhub.follow.TalkHubFollowService;
import com.talkhub.services.talkhub.notification.ITalkHubNotificationService;
import com.talkhub.services.talkhub.notification.TalkHubNotificationService;
import com.talkhub.services.talkhub.post.ITalkHubPostService;
import com.talkhub.services.talkhub.post.TalkHubPostService;
import com.talkhub.services.talkhub.profile.ITalkHubProfileService;
import com.talkhub.services.talkhub.profile.TalkHubProfileService;
import com.talkhub.services.talkhub.tag.ITalkHubTagService;
import com.talkhub.services.talkhub.tag.TalkHubTagService;
import com.talkhub.services.talkhub.topic.ITalkHubTopicService;
import com.talkhub.services.talkhub.topic.TalkHubTopicService;

public class TalkHubServices {
  public static ITalkHubAuthService talkHubAuthService = new TalkHubAuthService();
    public static ITalkHubProfileService talkHubProfileService = new TalkHubProfileService();
    public static ITalkHubCategoryService talkHubCategoryService = new TalkHubCategoryService();
    public static ITalkHubTopicService talkHubTopicService = new TalkHubTopicService();
    public static ITalkHubPostService talkHubPostService = new TalkHubPostService();
    public static ITalkHubTagService talkHubTagService = new TalkHubTagService();
    public static ITalkHubFollowService talkHubFollowService = new TalkHubFollowService();
    public static ITalkHubBookmarkService talkHubBookmarkService = new TalkHubBookmarkService();
    public static ITalkHubNotificationService talkHubNotificationService = new TalkHubNotificationService();
}

package com.justdoit.pics.global;

/**
 * Created by mengwen on 2015/10/25.
 */
public class Constant {
    // 网页有关
    public final static String HOME_URL = "http://demo.gzqichang.com:8001/"; // 首页
    public final static String REGIST_URL_SUFFIX = "api/user/regist/"; // 注册
    public final static String LOGIN_URL_SUFFIX = "api/user/login/"; // 登录
    public final static String USER_INFO_URL_SUFFIX = "api/user/userinfo/"; // 用户信息
    public final static String LOGOUT_URL_SUFFIX = "api/user/logout/"; // 登出
    public final static String FOLLOWING_URL_SUFFIX = "api/relation/following/"; // 关注列表
    public final static String FOLLOWER_URL_SUFFIX = "api/relation/followers/"; // 粉丝列表    
    public final static String USER_TOPIC_LIST = "api/topic/list/"; //用户话题列表
    public final static String TOPIC = "api/topic/"; //查看删除修改话题
    public final static String COMMENT_LIST = "api/topic/comment/list/"; //查看话题列表
    public final static String TOKEN_NAME = "csrftoken"; // token名称
    public final static String FORM_TOKEN_NAME = "csrfmiddlewaretoken"; // 表单token名称

    // preference文件
    public final static String USER_INFO_PREFS = "UserInfoPrefs"; // 用户信息preference文件名
    public final static String COOKIES_PREFS = "CookiesPrefs"; // cookie保存的preference文件名
    public static final String USER_ID_NAME = "userid"; // 用户id的名称
    public static final String USERNAME_NAME = "username"; // 用户名的名称

    // activity
    public final static String ACTION_KEY = "goActivity"; // 选择要跳转的activity的key标识
}

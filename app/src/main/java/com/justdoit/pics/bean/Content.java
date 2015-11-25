package com.justdoit.pics.bean;

import java.util.List;

/**
 * Created by ljz on 2015/10/30.
 */
public class Content {


    /**
     * pk : 1
     * title : Thinking In React Step 2
     * article : 123124123
     * article_status : 1
     * author : {"id":2,"password":"pbkdf2_sha256$20000$R4v6aQDxt4Xf$tKf1ndXSbMpe9hIYZ2wk5F7BxUQdnwiyUgwpHlyW4MY=","last_login":"2015-11-24T13:18:13Z","is_superuser":false,"username":"ljzzzzz","first_name":"","last_name":"","email":"otobai@aldkfn.conqe","is_staff":false,"is_active":true,"date_joined":"2015-11-24T13:17:15Z","nickname":"","avatar":null,"sex":"0","email_verified":"0","phone_verified":"0","country":"","province":"","city":"","birthday":null,"grade":0,"followers_count":0,"topic_count":0,"collection_count":0,"groups":[],"user_permissions":[]}
     * label_category : []
     * cover_image : http://demo.gzqichang.com:8001/media/a0d7d0f2b211931378094ad865380cd790238da1_QTwaoZ1.jpg
     * create_time : 2015-11-24T13:18:28Z
     * update_time : 2015-11-24T13:16:17Z
     * comment_count : 2
     * star_count : 0
     * collect_count : 0
     */

    private int pk;
    private String title;
    private String article;
    private String article_status;
    /**
     * id : 2
     * password : pbkdf2_sha256$20000$R4v6aQDxt4Xf$tKf1ndXSbMpe9hIYZ2wk5F7BxUQdnwiyUgwpHlyW4MY=
     * last_login : 2015-11-24T13:18:13Z
     * is_superuser : false
     * username : ljzzzzz
     * first_name :
     * last_name :
     * email : otobai@aldkfn.conqe
     * is_staff : false
     * is_active : true
     * date_joined : 2015-11-24T13:17:15Z
     * nickname :
     * avatar : null
     * sex : 0
     * email_verified : 0
     * phone_verified : 0
     * country :
     * province :
     * city :
     * birthday : null
     * grade : 0
     * followers_count : 0
     * topic_count : 0
     * collection_count : 0
     * groups : []
     * user_permissions : []
     */

    private AuthorEntity author;
    private String cover_image;
    private String create_time;
    private String update_time;
    private int comment_count;
    private int star_count;
    private int collect_count;
    private List<?> label_category;

    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setArticle_status(String article_status) {
        this.article_status = article_status;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public void setStar_count(int star_count) {
        this.star_count = star_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public void setLabel_category(List<?> label_category) {
        this.label_category = label_category;
    }

    public int getPk() {
        return pk;
    }

    public String getTitle() {
        return title;
    }

    public String getArticle() {
        return article;
    }

    public String getArticle_status() {
        return article_status;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public int getComment_count() {
        return comment_count;
    }

    public int getStar_count() {
        return star_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public List<?> getLabel_category() {
        return label_category;
    }

    public static class AuthorEntity {
        private int id;
        private String password;
        private String last_login;
        private boolean is_superuser;
        private String username;
        private String first_name;
        private String last_name;
        private String email;
        private boolean is_staff;
        private boolean is_active;
        private String date_joined;
        private String nickname;
        private Object avatar;
        private String sex;
        private String email_verified;
        private String phone_verified;
        private String country;
        private String province;
        private String city;
        private Object birthday;
        private int grade;
        private int followers_count;
        private int topic_count;
        private int collection_count;
        private List<?> groups;
        private List<?> user_permissions;

        public void setId(int id) {
            this.id = id;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setLast_login(String last_login) {
            this.last_login = last_login;
        }

        public void setIs_superuser(boolean is_superuser) {
            this.is_superuser = is_superuser;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setIs_staff(boolean is_staff) {
            this.is_staff = is_staff;
        }

        public void setIs_active(boolean is_active) {
            this.is_active = is_active;
        }

        public void setDate_joined(String date_joined) {
            this.date_joined = date_joined;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(Object avatar) {
            this.avatar = avatar;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setEmail_verified(String email_verified) {
            this.email_verified = email_verified;
        }

        public void setPhone_verified(String phone_verified) {
            this.phone_verified = phone_verified;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public void setFollowers_count(int followers_count) {
            this.followers_count = followers_count;
        }

        public void setTopic_count(int topic_count) {
            this.topic_count = topic_count;
        }

        public void setCollection_count(int collection_count) {
            this.collection_count = collection_count;
        }

        public void setGroups(List<?> groups) {
            this.groups = groups;
        }

        public void setUser_permissions(List<?> user_permissions) {
            this.user_permissions = user_permissions;
        }

        public int getId() {
            return id;
        }

        public String getPassword() {
            return password;
        }

        public String getLast_login() {
            return last_login;
        }

        public boolean isIs_superuser() {
            return is_superuser;
        }

        public String getUsername() {
            return username;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getEmail() {
            return email;
        }

        public boolean isIs_staff() {
            return is_staff;
        }

        public boolean isIs_active() {
            return is_active;
        }

        public String getDate_joined() {
            return date_joined;
        }

        public String getNickname() {
            return nickname;
        }

        public Object getAvatar() {
            return avatar;
        }

        public String getSex() {
            return sex;
        }

        public String getEmail_verified() {
            return email_verified;
        }

        public String getPhone_verified() {
            return phone_verified;
        }

        public String getCountry() {
            return country;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public Object getBirthday() {
            return birthday;
        }

        public int getGrade() {
            return grade;
        }

        public int getFollowers_count() {
            return followers_count;
        }

        public int getTopic_count() {
            return topic_count;
        }

        public int getCollection_count() {
            return collection_count;
        }

        public List<?> getGroups() {
            return groups;
        }

        public List<?> getUser_permissions() {
            return user_permissions;
        }
    }
}

package com.justdoit.pics.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mengwen on 2015/11/26.
 */
public class UserRelationListInfo implements Serializable{

    /**
     * count : 1
     * next : null
     * previous : null
     * results : [{"relation_user":{"id":2,"password":"pbkdf2_sha256$20000$R4v6aQDxt4Xf$tKf1ndXSbMpe9hIYZ2wk5F7BxUQdnwiyUgwpHlyW4MY=","last_login":"2015-11-26T09:54:57Z","is_superuser":false,"username":"ljzzzzz","first_name":"","last_name":"","email":"otobai@aldkfn.conqe","is_staff":false,"is_active":true,"date_joined":"2015-11-24T13:17:15Z","nickname":"","avatar":"http://demo.gzqichang.com:8001/media/20151126093947.png","sex":"0","email_verified":"0","phone_verified":"0","country":"","province":"","city":"","birthday":null,"grade":0,"followers_count":2,"following_count":0,"topic_count":0,"collection_count":0,"groups":[],"user_permissions":[]}}]
     */

    private int count;
    private Object next;
    private Object previous;
    /**
     * relation_user : {"id":2,"password":"pbkdf2_sha256$20000$R4v6aQDxt4Xf$tKf1ndXSbMpe9hIYZ2wk5F7BxUQdnwiyUgwpHlyW4MY=","last_login":"2015-11-26T09:54:57Z","is_superuser":false,"username":"ljzzzzz","first_name":"","last_name":"","email":"otobai@aldkfn.conqe","is_staff":false,"is_active":true,"date_joined":"2015-11-24T13:17:15Z","nickname":"","avatar":"http://demo.gzqichang.com:8001/media/20151126093947.png","sex":"0","email_verified":"0","phone_verified":"0","country":"","province":"","city":"","birthday":null,"grade":0,"followers_count":2,"following_count":0,"topic_count":0,"collection_count":0,"groups":[],"user_permissions":[]}
     */

    private List<ResultsEntity> results;

    public void setCount(int count) {
        this.count = count;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public Object getNext() {
        return next;
    }

    public Object getPrevious() {
        return previous;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public static class ResultsEntity {
        /**
         * id : 2
         * password : pbkdf2_sha256$20000$R4v6aQDxt4Xf$tKf1ndXSbMpe9hIYZ2wk5F7BxUQdnwiyUgwpHlyW4MY=
         * last_login : 2015-11-26T09:54:57Z
         * is_superuser : false
         * username : ljzzzzz
         * first_name :
         * last_name :
         * email : otobai@aldkfn.conqe
         * is_staff : false
         * is_active : true
         * date_joined : 2015-11-24T13:17:15Z
         * nickname :
         * avatar : http://demo.gzqichang.com:8001/media/20151126093947.png
         * sex : 0
         * email_verified : 0
         * phone_verified : 0
         * country :
         * province :
         * city :
         * birthday : null
         * grade : 0
         * followers_count : 2
         * following_count : 0
         * topic_count : 0
         * collection_count : 0
         * groups : []
         * user_permissions : []
         */

        private RelationUserEntity relation_user;

        public void setRelation_user(RelationUserEntity relation_user) {
            this.relation_user = relation_user;
        }

        public RelationUserEntity getRelation_user() {
            return relation_user;
        }

        public static class RelationUserEntity {
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
            private String avatar;
            private String sex;
            private String email_verified;
            private String phone_verified;
            private String country;
            private String province;
            private String city;
            private Object birthday;
            private int grade;
            private int followers_count;
            private int following_count;
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

            public void setAvatar(String avatar) {
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

            public void setFollowing_count(int following_count) {
                this.following_count = following_count;
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

            public String getAvatar() {
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

            public int getFollowing_count() {
                return following_count;
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
}

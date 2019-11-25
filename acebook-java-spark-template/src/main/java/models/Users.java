package models;

import lombok.Data;

    @Data
    public class Users{
        private String username;
        private String full_name;
        private String password;

        public Users(String username, String full_name, String password) {
            this.username = username;
            this.full_name = full_name;
            this.password = password;
        }

    }


package models;

import lombok.Data;

import java.util.UUID;

    @Data
    public class Users{
        private static UUID user_id;
//        private UUID user_id;
        private String username;
        private String full_name;
        private String password;

        public Users(UUID user_id, String username, String full_name, String password) {
            this.user_id = user_id;
            this.username = username;
            this.full_name = full_name;
            this.password = password;
        }

        public static UUID return_user_id(){
            return user_id;
        }
        public boolean check_password(String check_password){
            boolean pass = false;
            if(password == check_password){
                pass = true;
            }
            return pass;
        }


    }


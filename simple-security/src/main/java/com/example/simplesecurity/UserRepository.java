package com.example.simplesecurity;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<String, User> userMap;

    /*
    아래의 encodedPassword는 각 알고리즘으로 인코딩된 password

    var scryptPasswordEncoder = SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8();
    scryptPasswordEncoder.encode("scrypt-password-1");
    scryptPasswordEncoder.encode("scrypt-password-2");
    scryptPasswordEncoder.encode("scrypt-password-3");

    var sha256PasswordEncoder = new MessageDigestPasswordEncoder("SHA-256");
    sha256PasswordEncoder.encode("sha-256-password-1");
    sha256PasswordEncoder.encode("sha-256-password-2");
    sha256PasswordEncoder.encode("sha-256-password-3");

    var bcryptPasswordEncoder = new BCryptPasswordEncoder();
    bcryptPasswordEncoder.encode("bcrypt-password-1");
    bcryptPasswordEncoder.encode("bcrypt-password-2");
    bcryptPasswordEncoder.encode("bcrypt-password-3");

    var argon2PasswordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    argon2PasswordEncoder.encode("argon2-password-1");
    argon2PasswordEncoder.encode("argon2-password-2");
    argon2PasswordEncoder.encode("argon2-password-3");
     */
    public UserRepository() {
        this.userMap = new HashMap<>();
        userMap.put("scrypt-user1", new User("scrypt-user1", "{scrypt}$100801$AI7MTvo+Bi953IDwR6ffKA==$RV6VeMB6seHKP4wJmwnu5ZuK1lQQ1hSehhgJicbkq10="));  // scrypt-password-1
        userMap.put("scrypt-user2", new User("scrypt-user2", "{scrypt}$100801$5IkIOhpHIF1giQ1h2ArAvA==$2psGHZMFHsgKhgYSs2XMtZ5Ick8Z4DPkFp6TFxYc5xg="));  // scrypt-password-2
        userMap.put("scrypt-user3", new User("scrypt-user3", "{scrypt}$100801$rS6bnMfvtq+Zenzi03pyJA==$Q0f2a5LD5W6nautyHFT/x2ftTT+opzIRn9j1kUZ1/b4="));  // scrypt-password-3

        userMap.put("sha256-user1", new User("sha256-user1", "{sha256}{FVwCcnODgLlTMKKHxWOFNCfiVoizUmMQGY5TMBfNgdo=}6122fd7b910d1468f50d70ad3c240450793e752dc3da5e3bfbc10cfd8edfcdab"));  // sha-256-password-1
        userMap.put("sha256-user2", new User("sha256-user2", "{sha256}{/dKcJzTzJo34mH8vaGOKGJiBBTYpKrK70yWEF/S0Rgs=}01ab075c059b603a6028ad9366589a79c2d6874b5176d6c3f7f8909588bc6fb9"));  // sha-256-password-2
        userMap.put("sha256-user3", new User("sha256-user3", "{aPykxPurwrcDocDbTl/rhm5ImzjjIIkK+oaEKINufzQ=}d35414cf2f218f74dced774b9fabbf3d06c85cb276a2f0b0253335cfc6684ea6"));  // sha-256-password-3

        userMap.put("bcrypt-user1", new User("bcrypt-user1", "{bcrypt}$2a$10$905//ZsakCVOXpw2Z/cVQeQQu9rSQi3VDlEebKIhraxzle.dbsqcK"));  // bcrypt-password-1
        userMap.put("bcrypt-user2", new User("bcrypt-user2", "{bcrypt}$2a$10$Qn9M.6JvOIBc/IAAfzKX2eq4takGmNS13OacU0oSVqPPcwAJ9SrJW"));  // bcrypt-password-2
        userMap.put("bcrypt-user3", new User("bcrypt-user3", "{bcrypt}$2a$10$gSMBYpqL.jhu8f8HPkUKLeORegHuxFJvhsAm7oFBpj6CwTfLoY3/u"));  // bcrypt-password-3

        userMap.put("argon2-user1", new User("argon2-user1", "{argon2}$argon2id$v=19$m=16384,t=2,p=1$8OOiHb49WBwHvt4H+RA58g$8yPn/zejS6Yc/Droo++t9UHGFPVRAUziDfjeSdxb+dI"));  // argon2-password-1
        userMap.put("argon2-user2", new User("argon2-user2", "{argon2}$argon2id$v=19$m=16384,t=2,p=1$ujGS4bjhT+BbmSL+yXtioA$h/nmBQaaBQdsjwME7d7VTJQt1y6XnjMxY/yQK5yK/2o"));  // argon2-password-2
        userMap.put("argon2-user3", new User("argon2-user3", "{argon2}$argon2id$v=19$m=16384,t=2,p=1$bLunl/vm04681OJe1PCdMw$Mblo6JO2H/zWFIUF9yvgJG2z2kabomwTcyJs2eBDxOw"));  // argon2-password-3
    }

    public User findByUsername(String username) {
        return userMap.get(username);
    }

    public void save(User user) {
        userMap.put(user.username(), user);
    }
}

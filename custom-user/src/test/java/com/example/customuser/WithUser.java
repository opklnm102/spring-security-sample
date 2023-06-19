package com.example.customuser;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithUserDetails("user@example.com")
@Retention(RetentionPolicy.RUNTIME)
public @interface WithUser {
}

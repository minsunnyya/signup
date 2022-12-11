package com.example.demo.fixture;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

public class TestInfoFixture {
    public static @NotNull TestInfo get() {
        TestInfo info = new TestInfo();
        info.setPostId(1);
        info.setUserId(1);
        info.setUserName("user");
        info.setPassword("password");
        info.setTitle("title");
        info.setBody("body");
        return info;
    }

    @Data
    public static class TestInfo {
        private Integer postId;
        private Integer userId;
        private String userName;
        private String password;
        private String title;
        private String body;
    }
}

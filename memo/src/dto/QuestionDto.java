package com.primenumber.stackoverflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private String title;
        private String content;
        private List<String> questionTags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private Long id;
        private String title;
        private String content;
        private List<String> questionTags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long questionId;
        private String title;
        private String content;
        private LocalDateTime modifiedAt;

        private Long memberId;
        private String displayName;

        private List<TagDto.Response> tags;
        private List<AnswerDto.Response> answers;
    }
}

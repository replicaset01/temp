package com.primenumber.stackoverflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AnswerDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long answerId;
        private String content;
        private long memberId;
        private String displayName;
        private long questionId;
        private LocalDateTime modifiedAt;
    }
}

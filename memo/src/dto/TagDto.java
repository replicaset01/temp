package com.primenumber.stackoverflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class TagDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long tagId;
        private String tagName;
    }
}

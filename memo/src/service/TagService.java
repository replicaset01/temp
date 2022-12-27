package com.primenumber.stackoverflow.service;

import com.primenumber.stackoverflow.entity.Tag;
import com.primenumber.stackoverflow.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository tagRepository;

    public Tag createTag(Tag tag) {
        verifiedTag(String.valueOf(tag));
        return tagRepository.save(tag);
    }

    public Tag updateTag(Tag tag) {
        Long findTag = tag.getId();

        Optional.ofNullable(tag.getName())
                .ifPresent(tag::setName);
        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public Tag findTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("태그가 없습니다 - tagName: " + tagName));
    }

    public void deleteTag(long tagId) {
        tagRepository.deleteById(tagId);
    }

    private Tag verifiedTag(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("태그가 없습니다. - name: " + name));
    }
}

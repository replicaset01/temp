package com.primenumber.stackoverflow.service;

import com.primenumber.stackoverflow.entity.Question;
import com.primenumber.stackoverflow.entity.QuestionTag;
import com.primenumber.stackoverflow.entity.Tag;
import com.primenumber.stackoverflow.repository.QuestionTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuestionTagService {

    private final QuestionTagRepository questionTagRepository;

    public QuestionTag createQuestionTag(QuestionTag questionTag) {
        return questionTagRepository.save(questionTag);
    }

    public QuestionTag findQuestionTag(Question question, String tagName) {
        return questionTagRepository.findByQuestionAndTag(question, tagName)
                .orElseThrow(() -> new EntityNotFoundException("질문태그가 없습니다 - questionName, tagName: " + question + tagName));
    }
}

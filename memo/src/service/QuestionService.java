package com.primenumber.stackoverflow.service;

import com.primenumber.stackoverflow.dto.QuestionDto;
import com.primenumber.stackoverflow.dto.security.MemberPrincipal;
import com.primenumber.stackoverflow.entity.Question;
import com.primenumber.stackoverflow.entity.QuestionTag;
import com.primenumber.stackoverflow.entity.Tag;
import com.primenumber.stackoverflow.exception.BusinessLogicException;
import com.primenumber.stackoverflow.exception.ExceptionCode;
import com.primenumber.stackoverflow.mapper.QuestionMapper;
import com.primenumber.stackoverflow.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper mapper;
    private final MemberService memberService;
    private final TagService tagService;
    private final QuestionTagService questionTagService;


    public Question createQuestion(QuestionDto.Post requestBody, MemberPrincipal memberPrincipal) {
        // Request Body에서 List<String> questionTags 가져오기
        List<String> questionTags = requestBody.getQuestionTags();

        // Mapper를 이용해 Dto -> Entity 변환
        Question newQuestion = mapper.postToEntity(requestBody, memberPrincipal.toEntity());

        // 위에서 가져온 questionTags 순회
        for (String inputTag : questionTags) {

            // Request Body로 받은 questionTags를 Tag에 동일한 String이 있는지 확인
            Optional<Tag> findTag = Optional.ofNullable(tagService.findTag(inputTag));

            // 태그에 questionTag와 동일한 String이 없으면
            if (!findTag.isPresent()) {
                // 새 태그 객체 생성 후 Tag 저장
                findTag = Optional.of(Tag.of(inputTag));
                tagService.createTag(findTag.get());
            }

            // 질문태그(질문, 태그) 생성 후 저장
            QuestionTag newQuestionTag = QuestionTag.of(newQuestion, findTag.get());
            questionTagService.createQuestionTag(newQuestionTag);

            // Entity에 QuestionTag 저장
            newQuestion.addQuestionTag(newQuestionTag);
        }
        return questionRepository.save(newQuestion);
    }


    // 찾은 questionId 에서 MemberPrincipal 의 memberId랑 맞는지 확인
    public Question updateQuestion(Question question) {

        Question findQuestion = verifiedQuestion(question.getId());
        MemberPrincipal memberPrincipal = MemberPrincipal.from(memberService.searchMember(question.getMember().getId()));

        Optional.ofNullable(question.getTitle())
                .ifPresent(findQuestion::setTitle);
        Optional.ofNullable(question.getContent())
                .ifPresent(findQuestion::setContent);
        Optional.ofNullable(question.getQuestionTags())
                .ifPresent(findQuestion::setQuestionTags);

        return questionRepository.save(findQuestion);
    }

    @Transactional(readOnly = true)
    public Question findQuestion(Long questionId) {

        return questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("질문이 없습니다 - questionId: " + questionId));

    }

    @Transactional(readOnly = true)
    public Page<Question> findQuestions(int page, int size) {

        return questionRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public void deleteQuestion(Long id) {

        questionRepository.deleteById(id);
    }

    private Question verifiedQuestion(long questionId) {

        return questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("질문이 없습니다 - questionId: " + questionId));
    }
}

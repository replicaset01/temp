package com.primenumber.stackoverflow.controller;

import com.primenumber.stackoverflow.dto.QuestionDto;
import com.primenumber.stackoverflow.dto.security.MemberPrincipal;
import com.primenumber.stackoverflow.entity.Answer;
import com.primenumber.stackoverflow.entity.Question;
import com.primenumber.stackoverflow.mapper.QuestionMapper;
import com.primenumber.stackoverflow.response.BaseResponse;
import com.primenumber.stackoverflow.response.PagingResponse;
import com.primenumber.stackoverflow.service.QuestionService;
import com.primenumber.stackoverflow.util.Constant;
import com.primenumber.stackoverflow.util.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Validated
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionMapper mapper;


    @PostMapping
    public ResponseEntity postQuestion(
            @Valid @RequestBody QuestionDto.Post post,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        Question question = questionService.createQuestion(post, memberPrincipal);

        URI location = UriCreator.createUri(Constant.QUESTION_DEFAULT_URL, question.getId());
        return ResponseEntity.created(location).build();

    }

    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") @Positive long questionId,
                                        @Valid @RequestBody QuestionDto.Patch patch) {

        Question question = questionService.updateQuestion(mapper.patchToEntity(patch));

        return new ResponseEntity<>(new BaseResponse<>(mapper.entityToResponse(question)), HttpStatus.OK);
    }

    @GetMapping("/{question-id}/{member-id}")
    public ResponseEntity getQuestion(@PathVariable("question-id") @Positive long questionId,
                                      @PathVariable("member-id") @Positive long memberId) {

        Question question = questionService.findQuestion(questionId);
        if (question == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Answer> answer = question.getAnswers();
        question.setAnswers(answer);

        return new ResponseEntity(new BaseResponse<>(mapper.entityToResponse(question)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getQuestions(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "15") int size) {

        Page<Question> pageQuestions = questionService.findQuestions(page -1, size);
        List<Question> questions = pageQuestions.getContent();
        return new ResponseEntity<>(new PagingResponse<>(mapper.entityToResponses(questions), pageQuestions), HttpStatus.OK);
    }

    @DeleteMapping("/{question-id}")
    public ResponseEntity deleteQuestion(@PathVariable("question-id") @Positive long questionId) {

        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
package com.primenumber.stackoverflow.mapper;

import com.primenumber.stackoverflow.dto.QuestionDto;
import com.primenumber.stackoverflow.entity.Member;
import com.primenumber.stackoverflow.entity.Question;
import com.primenumber.stackoverflow.entity.util.BasicStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface QuestionMapper {

    @Mapping(target = "questionTags", ignore = true)
    default Question postToEntity(QuestionDto.Post post, Member member) {
        Question question = Question.of(post.getTitle(), post.getContent(), member);
        return question;
    }

    default Question patchToEntity(QuestionDto.Patch patch) {
        Question updateQuestion = Question.of(null,null,null);
        updateQuestion.setStatus(BasicStatus.ACTIVE);
        updateQuestion.setAnswers(null);
        return  updateQuestion;
    }

    default QuestionDto.Response entityToResponse(Question question) {
    }


    List<QuestionDto.Response> entityToResponses(List<Question> questions);
}

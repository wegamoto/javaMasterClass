package com.wewe.eduexam.service;

import com.wewe.eduexam.dto.ChoiceDTO;
import com.wewe.eduexam.model.Choice;
import com.wewe.eduexam.repository.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChoiceService {

    private final ChoiceRepository choiceRepository;

    @Autowired
    public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    public List<Choice> getByQuestionId(Long questionId) {
        return choiceRepository.findByQuestionId(questionId);
    }

    public List<ChoiceDTO> getChoicesByQuestionId(Long questionId) {
        return choiceRepository.findByQuestionId(questionId)
                .stream()
                .map(c -> new ChoiceDTO(c.getId(), c.getContent(), c.getIsCorrect(), c.getQuestion().getContent()))
                .collect(Collectors.toList());
    }

}


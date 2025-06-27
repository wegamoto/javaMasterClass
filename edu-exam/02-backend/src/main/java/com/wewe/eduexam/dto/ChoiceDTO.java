// com.wewe.eduexam.dto.ChoiceDTO.java
package com.wewe.eduexam.dto;

import lombok.Getter;

public class ChoiceDTO {
    @Getter
    private Long id;
    @Getter
    private String content;
    private boolean isCorrect;
    private String questionText;

    public ChoiceDTO(Long id, String content, boolean isCorrect, String questionText) {
        this.id = id;
        this.content = content;
        this.isCorrect = isCorrect;
        this.questionText = questionText;
    }

    public boolean correct() {
        return isCorrect;
    }

}

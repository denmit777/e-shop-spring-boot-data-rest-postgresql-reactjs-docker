package com.training.eshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackDto {

    private static final String WRONG_RATE = "Wrong product rate";
    private static final String RATE_FIELD_IS_EMPTY = "Rate field shouldn't be empty";

    private String user;

    @Pattern(regexp = "terrible|bad|medium|good|great", message = WRONG_RATE)
    @NotEmpty(message = RATE_FIELD_IS_EMPTY)
    private String rate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String text;
}



package com.training.eshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {

    private static final String COMMENT_IS_EMPTY = "Comment shouldn't be empty";
    private static final String WRONG_SIZE_OF_COMMENT = "Comment shouldn't be more than 300 symbols";
    private static final String WRONG_COMMENT = "Comment should contain latin letters or figures";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String user;

    @NotBlank(message = COMMENT_IS_EMPTY)
    @Pattern(regexp = "^[A-za-z0-9]*$", message = WRONG_COMMENT)
    @Size(max = 300, message = WRONG_SIZE_OF_COMMENT)
    private String text;
}

package net.njit.ms.cs.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDto {

    private String code;

    public CodeDto(String code) {
        this.code = code;
    }

    public CodeDto() {}

}
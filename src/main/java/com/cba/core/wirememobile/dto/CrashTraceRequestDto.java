package com.cba.core.wirememobile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CrashTraceRequestDto implements Serializable {


    @NotBlank(message = "{validation.trace.time.empty}")
    private String time;
    @NotBlank(message = "{validation.trace.trace.empty}")
    private String trace;

    private static final long serialVersionUID = 1L;

}

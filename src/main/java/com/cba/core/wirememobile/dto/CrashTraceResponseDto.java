package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CrashTraceResponseDto implements Serializable {


    private int id;
    private String time;
    private String trace;

    private static final long serialVersionUID = 1L;

}

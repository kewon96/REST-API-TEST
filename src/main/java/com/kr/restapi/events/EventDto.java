package com.kr.restapi.events;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {
    
    // 입력받을 항목
    private String name;                                  // 이벤트 이름
    private String description;                           // 설명
    private LocalDateTime beginEnrollmentDateTime;        // 등록 시작일시
    private LocalDateTime closeEnrollmentDateTime;        // 등록 종료일시
    private LocalDateTime beginEventDateTime;             // 이벤트 시작일시
    private LocalDateTime endEventDateTime;               // 이벤트 종료일시
    private String location;                              // (optional) 위치(이게 없으면 온라인 모임)
    private int basePrice;                                // (optional) 등록비
    private int maxPrice;                                 // (optional) 등록비
    private int limitOfEnrollment;                        // 제한 인원

}

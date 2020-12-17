package com.kr.restapi.events;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 1. Builder Annotation으로 인해 Compile될 때 builder()와 각종 getter/setter, constructor 등이 생성된다.
 * 2. Equals와 HashCode를 구현할 때 모든 필드를 사용한다. Entity간의 연관관계가 있을 때 상호참조하는 경우가 되면 Stackoverflow가 발생함
 *   1. 다른 몇가지 필드를 추가해도 되지만 연관관계에 해당되는 필드가 있다면 상호참조로 인해 구현체에서 Stackoverflow(서로간의 메소드를 계속 호출)가 나타남
 *   2. SQL상에서 Primary Key를 생각하면 이해하기 쉽다.
 * 3. 밑의 많은 Annotation을 줄이기 위해 "@Data"라는 Annotation이 존재한다.
 *      하지만 사용하지 않는 이유는 사용 시 EqualsAndHashCode를 모든 필드를 호출하기 때문에 사용하지 않는다.
 */
//
//
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Event {
    private Integer id;

    private boolean offline; // online, offline 여부
    private boolean free; // 이벤트 유/무료 여부
    private EventStatus eventStatus = EventStatus.DRAFT;
    private String name;                                  // 이벤트 이름
    private String description;                           // 설명
    private LocalDateTime beginEnrollmentDateTime;        // 등록 시작일시
    private LocalDateTime closeEnrollmentDateTime;        // 등록 종료일시
    private LocalDateTime beginEventDateTime;             // 이벤트 시작일시
    private LocalDateTime endEventDateTime;               // 이벤트 종료일시
    private String location;                              // (optional) 위치(이게 없으면 온라인 모임)
    private int basePrice;                                // (optional)
    private int maxPrice;                                 // (optional)
    private int limitOfEnrollment;


}
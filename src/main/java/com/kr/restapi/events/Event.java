package com.kr.restapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <pre>
 * 1. Builder Annotation으로 인해 Compile될 때 builder()와 각종 getter/setter, constructor 등이 생성된다.
 * 2. Equals와 HashCode를 구현할 때 모든 필드를 사용한다. Entity간의 연관관계가 있을 때 상호참조하는 경우가 되면 Stackoverflow가 발생함
 *     1. 다른 몇가지 필드를 추가해도 되지만 연관관계에 해당되는 필드가 있다면 상호참조로 인해 구현체에서 Stackoverflow(서로간의 메소드를 계속 호출)가 나타남
 *     2. SQL상에서 Primary Key를 생각하면 이해하기 쉽다.
 * 3. 밑의 많은 Annotation을 줄이기 위해 "@Data"라는 Annotation이 존재한다.
 *     하지만 사용하지 않는 이유는 사용 시 EqualsAndHashCode를 모든 필드를 호출하기 때문에 사용하지 않는다.
 * 5. Event를 Entity로 선언하기 위해 @Entity를 달아주고 id가 식별자로 하기위해 @Id, @GeneratedValue를 달아준다.
 * 6. eventStatus의 이름값 그대로 저장하기 위해 @Enumerated(EnumType.STRING)을 붙인다.
 *     순서값을 저장하기 위해선 "STRING"을 "ORDINAL"로 변경
 * </pre>
 */
@Builder @AllArgsConstructor @NoArgsConstructor @ToString
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

    private boolean offline; // online, offline 여부
    private boolean free; // 이벤트 유/무료 여부
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
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

    /** free, offlie update */
    public void update() {
        // Update free : basePrice와 maxPrice값이 0이라면 무료
        if(this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        }

        // Update offline : 장소값이 있으면 offline 형태
        if(this.location == null || this.location.isBlank()) { // java 8: this.location.trim().isEmpty()
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}

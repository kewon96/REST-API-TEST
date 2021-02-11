package com.kr.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.restapi.common.TestDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <pre>
 * 1. junit5기준, junit4는 @RunWith(SpringRunner.class)를 사용함
 * 2. @WebMvcTest : 웹과 관련된 bean들이 모두 등록
 * 3. 테스트할 때 MockMvc를 주입받아서 사용
 *     1. MockMvc: servlet을 상대로 가짜 요청(이때 가짜값들을 보냄)을 DispatcherServlet한테 보내고 그 응답을 확인하게 함
 *     2. Web과 관련된 것만 등록 -> Slicing test라 하기도 함
 *     3. 단위테스트라하기엔 너무 많은 것들이 개입됨, Eventcontroller, servlet 등등...
 *     4. 여러가지를 테스트하기 편하기 때문에 만듬
 *     5. 요청을 만들고 응답을 검증 -> 테스트에 있어 핵심Class
 *     6. WebServer를 띄우지 않음 -> 빠르지않음 -> DispatcherServlet을 만들어야해서 단위테스트보단 빠르지않음
 *     7. 해당 Test는 Slice Test라 Web Bean만 등록하지 Repository는 등록하지않기 때문에 따로 Mock으로 만들어서 등록시켜야한다.
 * </pre>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest @AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired private WebApplicationContext context;

    // MockMvc setting
    @Autowired MockMvc mockMvc;

    @Autowired ObjectMapper objectMapper;



    // @MockBean EventRepository eventRepository; // Mock이라서 return되는 존재가 null이다

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Kewon")
                .description("Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 12, 19, 21, 8))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 1, 19, 21, 7))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 20, 10, 0))
                .endEventDateTime(LocalDateTime.of(2021, 1, 20, 17, 0))
                .basePrice(10000)
                .maxPrice(20000)
                .limitOfEnrollment(10000)
                .location("삼성역")
                .build();

        // eventRepository.save(event)가 호출이 되면 event를 return하라
        // Mockito.when(eventRepository.save(event)).thenReturn(event);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();


        mockMvc.perform(post("/api/events/") // perform parameter는 request
                    .contentType(MediaType.APPLICATION_JSON) // 요청형식 : JSON, APPLICATION_JSON_UTF8은 이제 사용되지 않음
                    .accept(MediaTypes.HAL_JSON) // HAL JSON형식의 응답을 원한다
                    .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated()) // response ==> .andExpect(status().is(201));
                .andExpect(jsonPath("id").exists()) // id 여부 확인
                .andExpect(header().exists(HttpHeaders.LOCATION)) // Header에 Location이 있는지
                // Spring Boot 2.2.0부터 MockMvc에서 UTF-8문자를 처리하지 않음
                // https://qastack.kr/programming/58525387/mockmvc-no-longer-handles-utf-8-characters-with-spring-boot-2-2-0-release
                .andExpect(header().string("Content-Type", "application/hal+json;charset=UTF-8")) // Content-Type이 hal json형식인지
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())   // 존재하는지
                .andExpect(jsonPath("_links.query-events").exists()) // 이벤트 목록으로
                .andExpect(jsonPath("_links.update-event").exists()) // 이벤트 수정으로
        ;



    }

    /**
     * Id나 free등 입력되면 안되는 값이 넘어올 시 Bad Request를 보여줌
     * 입력받은 값(JSON) -> Entity
     * application.properties에서 spring.jackson.deserialization.fail-on-unknown-properties값을 true로 변경
     */
    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우 Bad Request Test")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Kewon")
                .description("Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 12, 19, 21, 8))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 1, 19, 21, 7))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 20, 10, 0))
                .endEventDateTime(LocalDateTime.of(2021, 1, 20, 17, 0))
                .basePrice(10000)
                .maxPrice(20000)
                .limitOfEnrollment(10000)
                .location("삼성역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
        ;

        System.out.println("Bad Request");

    }

    /**
     * Id나 free등 입력되면 안되는 값이 넘어올 시 무시
     * 입력받은 값(JSON) -> Entity
     * application.properties에서 spring.jackson.deserialization.fail-on-unknown-properties값을 false로 변경
     */
    @Test
    public void createEvent_Ignore_other_then_Input() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Kewon")
                .description("Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 12, 19, 21, 8))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 1, 19, 21, 7))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 20, 10, 0))
                .endEventDateTime(LocalDateTime.of(2021, 1, 20, 17, 0))
                .basePrice(10000)
                .maxPrice(20000)
                .limitOfEnrollment(10000)
                .location("삼성역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
        ;

        System.out.println("Ignore");

    }

    /**
     * 값이 비어있는 경우
     */
    @Test
    @TestDescription("입력 받아야 하는 값이 비어있는 경우 Bad Request Test")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isCreated())
        ;
    }

    /**
     * 값이 잘못된 경우
     */
    @Test
    @Description("입력 받아야 하는 값이 잘못된 경우 Bad Request Test")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Kewon")
                .description("Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 12, 19, 21, 8))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 12, 10, 10, 7))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 20, 10, 0))
                .endEventDateTime(LocalDateTime.of(2021, 1, 18, 17, 0))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(10000)
                .location("삼성역")
                .build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                // 응답에 포함시킴
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectValue").exists())
        ;
    }
}

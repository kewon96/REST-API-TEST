package com.kr.restapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    /**
     * <pre>
     * @param eventDto : Entity에 Validation을 적용시키기 위해 @Valid Annotation을 추가시킨다.
     * @param errors : eventDto에 error가 발생 시 error를 담아줄 객체
     *
     * @return : 최종적으론 Response안에 있는 Event 객체 / Validation에 대한 Error가 존재할 시 Bad Request
     * </pre>
     */
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        // 만약 errors에 error가 존재하면 해당 Bad Request를 return시킨다.
        if (errors.hasErrors()) {
            // errors 객체는 json으로 변환이 되지않는다.
            return ResponseEntity.badRequest().body(errors);
        }

        // validate를 타게 해서 error가 존재 시 errors에 담아줌
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update(); // Event로 제작 후 free, offline 이식
        Event newEvent = this.eventRepository.save(event); // 저장된 Entity
        Integer eventId = newEvent.getId();

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(eventId);
        URI createdUri = selfLinkBuilder.toUri();

        List<Link> links = Arrays.asList(
                selfLinkBuilder.withSelfRel(),
                linkTo(EventController.class).withRel("query-events"),
                selfLinkBuilder.withRel("update-event")
        );

        System.out.println(links);

        EntityModel eventResource = EntityModel.of(newEvent, links);
        eventResource.add();
        eventResource.add();
        eventResource.add();

        /* 방법1 */
//        EntityModel eventResource = EntityModel.of(newEvent);
//        eventResource.add(linkTo(EventController.class).slash(eventId).withSelfRel());
//        eventResource.add(linkTo(EventController.class).withRel("query-events"));
//        eventResource.add(selfLinkBuilder.withRel("update-event"));

        /* 옛날 버전 */
//        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash("query-events");
//        URI createdUri = selfLinkBuilder.toUri();
//
//        EventResource eventResource = new EventResource(event);
//        eventResource.add(linkTo(EventController.class).withRel("query-events"));
//        eventResource.add(selfLinkBuilder.withSelfRel());
//        eventResource.add(selfLinkBuilder.withRel("update-event"));


        return ResponseEntity.created(createdUri) // Header의 Location 정보안에 createUri를 이식
                .body(eventResource);  // 받아온 event를 이식
    }

}

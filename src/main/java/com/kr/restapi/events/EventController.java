package com.kr.restapi.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * 객체를 body에 담거나 header정보 등을 setting하기 위해 ResponseEntity를 return
 */
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    @PostMapping("")
    public ResponseEntity createEvent(@RequestBody Event event) {
        URI createdUri = linkTo(EventController.class)
                             .slash("#{id}")
                             .toUri();
//        URI createdUri = linkTo(methodOn(EventController.class).createEvent(Event.builder().build()))
//                .slash("#{id}")
//                .toUri();

//        event.setId(10);

        return ResponseEntity.created(createdUri) // Header의 Location 정보안에 createUri를 이식
                .body(event);                     // 받아온 event를 이식
    }

}

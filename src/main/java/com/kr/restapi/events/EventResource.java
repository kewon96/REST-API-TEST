package com.kr.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * ResourceSupport is now RepresentationModel
 *
 * Resource is now EntityModel
 *
 * Resources is now CollectionModel
 *
 * PagedResources is now PagedModel
 */
//public class EventResource extends RepresentationModel {
public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, List<Link> links) {
        of(event, links);
    }

//    @JsonUnwrapped : return할 때 json형식으로 감싸지 않게 하는 용도
//    private Event event;
//
//    public EventResource(Event event) {
//        this.event = event;
//    }
//
//    public Event getEvent() {
//        return event;
//    }
}

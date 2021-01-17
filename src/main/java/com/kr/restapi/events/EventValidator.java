package com.kr.restapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getMaxPrice() > eventDto.getBasePrice() && eventDto.getMaxPrice() == 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");

            errors.reject("wrongPrices", "Values for prices are wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        // 이벤트 시작일자보다 전날, 이벤트등록일자보다 전날, 이벤트종료일자보다 전날이면 Error
        if( endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
            endEventDateTime.isBefore(eventDto.getBeginEventDateTime())) {

            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");

            errors.reject("wrongTime", "Values for time are wrong");

        }

        // TODO beginEventDateTime
        // TODO CloseEnrollmentDateTime
    }

}

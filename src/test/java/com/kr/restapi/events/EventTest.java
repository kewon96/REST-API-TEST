package com.kr.restapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {
    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Kewon")
                .description("It's me!")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // Given
        String name = "Kewon By 1";
        String description = "It's Clone";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}
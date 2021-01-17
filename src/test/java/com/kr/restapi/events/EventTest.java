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

    @Test
    public void testFree() {
        /* Event의 basePrice, maxPrice가 0이면 무료 */
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isTrue();


        /* Event의 basePrice가 존재하면 유료 */
        // Given
        event = Event.builder()
                .basePrice(10000)
                .maxPrice(0)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();


        /* Event의 maxPrice가 존재하면 유료 */
        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(10000)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();
    }
}
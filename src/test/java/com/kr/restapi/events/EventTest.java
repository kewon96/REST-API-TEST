package com.kr.restapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

    @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
    @MethodSource
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        /* Event의 basePrice, maxPrice가 0이면 무료 */
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    // static 있어야 동작
    private static Object[] testFree() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100, 0, false},
                new Object[]{0, 100, false}
        };
    }

    @ParameterizedTest(name = "{index} => location={0}, isOffline={1}")
    @MethodSource
    public void testOffline(String location, boolean isOffline) {
        /* Event의 basePrice, maxPrice가 0이면 무료 */
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Object[] testOffline() {
        return new Object[]{
                new Object[]{"강남역 메리츠타워", true},
                new Object[]{"", false},
                new Object[]{null, false}
        };
    }
}
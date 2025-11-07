package de.doubleslash.thingworx.mcp.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThingsToolsTest {

    @Mock
    private ThingWorxApi api;

    @InjectMocks
    private ThingsTools tools;

    @Test
    void whenListThingsThenShouldReturnApiResult() {
        when(api.get("Thingworx", "Things")).thenReturn("OK");

        String result = tools.listThings();

        assertThat(result).isEqualTo("OK");
        verify(api).get("Thingworx", "Things");
    }

    @Test
    void whenApiThrowsOnListThingsThenShouldReturnErrorString() {
        when(api.get("Thingworx", "Things")).thenThrow(new RuntimeException("boom"));

        String result = tools.listThings();

        assertThat(result)
                .startsWith("Error listing Things:")
                .contains("boom");
    }

    @Test
    void whenGetThingGivenNameThenShouldReturnMetadata() {
        when(api.get("Thingworx", "Things", "MyThing")).thenReturn("META");

        String result = tools.getThing("MyThing");

        assertThat(result).isEqualTo("META");
        verify(api).get("Thingworx", "Things", "MyThing");
    }

    @Test
    void whenThingExistsThenShouldReturnTrue() {
        when(api.exists("Thingworx", "Things", "T1")).thenReturn(true);

        boolean exists = tools.thingExists("T1");

        assertThat(exists).isTrue();
        verify(api).exists("Thingworx", "Things", "T1");
    }

    @Test
    void whenThingDoesNotExistThenShouldReturnFalse() {
        when(api.exists("Thingworx", "Things", "T2")).thenReturn(false);

        boolean exists = tools.thingExists("T2");

        assertThat(exists).isFalse();
        verify(api).exists("Thingworx", "Things", "T2");
    }
}

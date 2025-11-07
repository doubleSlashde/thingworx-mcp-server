package de.doubleslash.thingworx.mcp.server.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ThingWorxApiExceptionTest {

    @Test
    void whenCreateExceptionThenShouldExposeStatusAndMessage() {
        ThingWorxApi.ThingWorxApiException ex = new ThingWorxApi.ThingWorxApiException(404, "Not Found");

        assertThat(ex.getStatus()).isEqualTo(404);
        assertThat(ex.getMessage())
                .contains("status=404");
        assertThat(ex.getMessage())
                .contains("Not Found");
    }
}

package de.doubleslash.thingworx.mcp.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionsToolsTest {

    @Mock
    private ThingWorxApi api;

    @InjectMocks
    private CollectionsTools tools;

    @Test
    void whenListCollectionThenShouldReturnApiResult() {
        when(api.get("Thingworx", "Things")).thenReturn("THINGS");

        String result = tools.listCollection("Things");

        assertThat(result).isEqualTo("THINGS");
        verify(api).get("Thingworx", "Things");
    }
}

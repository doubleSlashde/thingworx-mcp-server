package de.doubleslash.thingworx.mcp.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesToolsTest {

    @Mock
    private ThingWorxApi api;

    @InjectMocks
    private ServicesTools tools;

    @Test
    void whenListThingServicesThenShouldReturnApiResult() {
        when(api.get("Thingworx", "Things", "MyThing", "ServiceDefinitions")).thenReturn("SERVICES");

        String result = tools.listThingServices("MyThing");

        assertThat(result).isEqualTo("SERVICES");
        verify(api).get("Thingworx", "Things", "MyThing", "ServiceDefinitions");
    }

    @Test
    void whenGetServiceParametersThenShouldReturnApiResult() {
        when(api.get("Thingworx", "Things", "MyThing", "ServiceDefinitions", "RunIt")).thenReturn("PARAMS");

        String result = tools.getServiceParameters("MyThing", "RunIt");

        assertThat(result).isEqualTo("PARAMS");
        verify(api).get("Thingworx", "Things", "MyThing", "ServiceDefinitions", "RunIt");
    }

    @Test
    void whenInvokeThingServiceThenShouldReturnApiResult() {
        when(api.exists("Thingworx", "Things", "T")).thenReturn(true);
        when(api.postJson(Map.of("a", 2), "Thingworx", "Things", "T", "Services", "Do"))
                .thenReturn("OK");

        String result = tools.invokeThingService("T", "Do", Map.of("a", 2));

        assertThat(result).isEqualTo("OK");
        verify(api).exists("Thingworx", "Things", "T");
        verify(api).postJson(Map.of("a", 2), "Thingworx", "Things", "T", "Services", "Do");
    }
}

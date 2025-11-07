package de.doubleslash.thingworx.mcp.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertiesToolsTest {

    @Mock
    private ThingWorxApi api;

    @InjectMocks
    private PropertiesTools tools;

    @Test
    void whenListThingPropertiesThenShouldReturnApiResult() {
        when(api.get("Thingworx", "Things", "MyThing", "PropertyDefinitions")).thenReturn("PROPS");

        String result = tools.listThingProperties("MyThing");

        assertThat(result).isEqualTo("PROPS");
        verify(api).get("Thingworx", "Things", "MyThing", "PropertyDefinitions");
    }

    @Test
    void whenGetThingPropertyValueThenShouldReturnApiResult() {
        when(api.get("Thingworx", "Things", "MyThing", "Properties", "MyProperty")).thenReturn("123");

        String result = tools.getThingPropertyValue("MyThing", "MyProperty");

        assertThat(result).isEqualTo("123");
        verify(api).get("Thingworx", "Things", "MyThing", "Properties", "MyProperty");
    }
}

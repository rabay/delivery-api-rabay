package com.deliverytech.delivery_api.mapper.converters;

import com.deliverytech.delivery_api.model.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.spi.MappingContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatusPedidoConverterTest {

    private StatusPedidoConverter converter;
    private StatusPedidoReverseConverter reverseConverter;

    @BeforeEach
    void setUp() {
        converter = new StatusPedidoConverter();
        reverseConverter = new StatusPedidoReverseConverter();
    }

    @Test
    void shouldConvertStringToStatusPedido() {
        MappingContext<String, StatusPedido> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn("CRIADO");

        StatusPedido result = converter.convert(context);

        assertThat(result).isEqualTo(StatusPedido.CRIADO);
    }

    @Test
    void shouldReturnNullWhenStringIsNull() {
        MappingContext<String, StatusPedido> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(null);

        StatusPedido result = converter.convert(context);

        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullWhenStringIsInvalid() {
        MappingContext<String, StatusPedido> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn("INVALID_STATUS");

        StatusPedido result = converter.convert(context);

        assertThat(result).isNull();
    }

    @Test
    void shouldConvertStatusPedidoToString() {
        MappingContext<StatusPedido, String> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(StatusPedido.CONFIRMADO);

        String result = reverseConverter.convert(context);

        assertThat(result).isEqualTo("CONFIRMADO");
    }

    @Test
    void shouldReturnNullWhenStatusPedidoIsNull() {
        MappingContext<StatusPedido, String> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(null);

        String result = reverseConverter.convert(context);

        assertThat(result).isNull();
    }
}
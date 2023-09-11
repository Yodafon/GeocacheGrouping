package com.gg.loader.cache;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;
import com.gg.generated.Nuts;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.GenericMessage;

import java.io.IOException;
import java.util.List;

public class NUTSConverter implements HttpMessageConverter<Nuts> {

    public MessageConverter createMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JakartaXmlBindAnnotationModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return clazz.getCanonicalName().equals(Nuts.class.getCanonicalName());
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return List.of(MediaType.APPLICATION_JSON);
    }

    @Override
    public Nuts read(Class<? extends Nuts> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return (Nuts) createMessageConverter().fromMessage(new GenericMessage<>(inputMessage.getBody().readAllBytes()), Nuts.class);
    }

    @Override
    public void write(Nuts nuts, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}

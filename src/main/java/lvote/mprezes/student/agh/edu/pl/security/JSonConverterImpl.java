package lvote.mprezes.student.agh.edu.pl.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

/**
 * @author Krystian Majewski
 * @since 15.06.2017.
 */
public class JSonConverterImpl<T> implements JSonConverter<T> {

    private final Class<T> typeParameterClass;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public JSonConverterImpl(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public T convert(String json) throws IOException {
        ObjectReader objectReader = new ObjectMapper().reader().forType(typeParameterClass);

        return objectReader.readValue(json);
    }

    @Override
    public String convert(T object) throws JsonProcessingException {

        return objectWriter.writeValueAsString(object);
    }
}

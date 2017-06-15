package lvote.mprezes.student.agh.edu.pl.security;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * @author Krystian Majewski
 * @since 15.06.2017.
 */
public interface JSonConverter<T> {

    /**
     * Converts json object representation to Object class
     *
     * @param json
     * 		json object representation
     *
     * @return Object class created from json
     */
    T convert(String json) throws IOException;

    /**
     * Convert object to json string representation
     *
     * @param object
     * 		Object to convert
     *
     * @return json string representation
     */
    String convert(T object) throws JsonProcessingException;

}

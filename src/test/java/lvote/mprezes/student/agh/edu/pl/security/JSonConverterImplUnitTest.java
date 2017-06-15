package lvote.mprezes.student.agh.edu.pl.security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

/**
 * @author Krystian Majewski
 * @since 15.06.2017.
 */
public class JSonConverterImplUnitTest {

    private static final String EXPECTED_JSON = "{\r\n" +
        "  \"someInteger\" : 1,\r\n" +
        "  \"someString\" : \"test val\"\r\n" +
        "}";

    private final JSonConverter<SomeClassForTest> converter = new JSonConverterImpl<>(SomeClassForTest.class);


    @Test
    public void testConvertToString() throws Exception {
        String json = converter.convert(createTestData());

        Assert.assertEquals(json, EXPECTED_JSON);
    }

    @Test
    public void testConvertToObject() throws Exception {
        SomeClassForTest object = converter.convert(EXPECTED_JSON);

        Assert.assertEquals(object, createTestData());
    }

    private SomeClassForTest createTestData() {
        SomeClassForTest result = new SomeClassForTest();
        result.setSomeInteger(1);
        result.setSomeString("test val");

        return result;
    }


    private static class SomeClassForTest implements Serializable {

        private static final long serialVersionUID = 8774086294427294211L;

        private Integer someInteger;
        private String someString;

        public Integer getSomeInteger() {
            return someInteger;
        }

        public void setSomeInteger(Integer someInteger) {
            this.someInteger = someInteger;
        }

        public String getSomeString() {
            return someString;
        }

        public void setSomeString(String someString) {
            this.someString = someString;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof SomeClassForTest)) return false;

            SomeClassForTest that = (SomeClassForTest) o;

            return new EqualsBuilder()
                .append(someInteger, that.someInteger)
                .append(someString, that.someString)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                .append(someInteger)
                .append(someString)
                .toHashCode();
        }
    }

}

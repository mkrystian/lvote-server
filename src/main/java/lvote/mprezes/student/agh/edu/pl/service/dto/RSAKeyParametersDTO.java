package lvote.mprezes.student.agh.edu.pl.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Krystian Majewski
 * @since 15.06.2017.
 */
public class RSAKeyParametersDTO implements Serializable {

    private static final long serialVersionUID = -1800642635410386159L;

    private String modulus;
    private BigInteger exponent;
    private boolean isPrivate;

    public String getModulus() {
        return modulus;
    }

    public void setModulus(String modulus) {
        this.modulus = modulus;
    }

    public BigInteger getExponent() {
        return exponent;
    }

    public void setExponent(BigInteger exponent) {
        this.exponent = exponent;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RSAKeyParametersDTO)) return false;

        RSAKeyParametersDTO that = (RSAKeyParametersDTO) o;

        return new EqualsBuilder()
            .append(isPrivate, that.isPrivate)
            .append(modulus, that.modulus)
            .append(exponent, that.exponent)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(modulus)
            .append(exponent)
            .append(isPrivate)
            .toHashCode();
    }
}

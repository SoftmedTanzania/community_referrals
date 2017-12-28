package org.ei.opensrp.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ANM {
    private final String name;
    private final long fpCount;
    private final long successfulCount;
    private final long unsucessfulCount;

    public ANM(String name, long eligibleCoupleCount, long fpCount, long successfulCount, long unsucessfulCount) {
        this.name = name;
        this.fpCount = fpCount;
        this.successfulCount = successfulCount;
        this.unsucessfulCount = unsucessfulCount;
    }

    public String getName() {
        return name;
    }

    public long getFpCount() {
        return fpCount;
    }

    public long getSuccessfulCount() {
        return successfulCount;
    }

    public long getUnsucessfulCount() {
        return unsucessfulCount;
    }


    public long fpCount() {
        return fpCount;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

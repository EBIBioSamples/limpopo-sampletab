package uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.attribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNodeAttribute implements SCDNodeAttribute {
    protected volatile String attributeValue;

    private Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    public void setAttributeValue(String attributeValue) {
        // note - override default implementation, which throws NPE if name is null or empty
        this.attributeValue = attributeValue;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getNodeType() {
        return getAttributeType();
    }

    public String getNodeName() {
        return getAttributeValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractNodeAttribute that = (AbstractNodeAttribute) o;

        if (getAttributeType() != null ? !getAttributeType().equals(that.getAttributeType()) : that.getAttributeType() != null) {
            return false;
        }
        if (attributeValue != null ? !attributeValue.equals(that.attributeValue) : that.attributeValue != null) {
            return false;
        }
        if (log != null ? !log.equals(that.log) : that.log != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAttributeType() != null ? getAttributeType().hashCode() : 0;
        result = 31 * result + (attributeValue != null ? attributeValue.hashCode() : 0);
        result = 31 * result + (log != null ? log.hashCode() : 0);
        return result;
    }
}

package cz.cvut.dp.nss.wrapper.output.timeTable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jakubchalupa
 * @since 02.03.17
 */
public class TimeTableWrapper {

    @JsonProperty("id")
    private String entityId;

    private String name;

    private boolean valid;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}

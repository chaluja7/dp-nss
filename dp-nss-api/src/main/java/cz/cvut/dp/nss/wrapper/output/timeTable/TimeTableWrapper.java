package cz.cvut.dp.nss.wrapper.output.timeTable;

/**
 * @author jakubchalupa
 * @since 02.03.17
 */
public class TimeTableWrapper {

    private String id;

    private String name;

    private boolean valid;

    private boolean synchronizing;

    private String synchronizationFailMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isSynchronizing() {
        return synchronizing;
    }

    public void setSynchronizing(boolean synchronizing) {
        this.synchronizing = synchronizing;
    }

    public String getSynchronizationFailMessage() {
        return synchronizationFailMessage;
    }

    public void setSynchronizationFailMessage(String synchronizationFailMessage) {
        this.synchronizationFailMessage = synchronizationFailMessage;
    }
}

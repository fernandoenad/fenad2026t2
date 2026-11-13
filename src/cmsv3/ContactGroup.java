package cmsv3;
import java.io.Serializable;

public class ContactGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    private String groupId, groupName;
    public ContactGroup(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }
    public String getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String name) { groupName = name; }
    @Override public String toString() { return groupId + " - " + groupName; }
}

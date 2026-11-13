package cmsv3;
import java.io.Serializable;

public class Contact implements Serializable {
    private static final long serialVersionUID = 2L;
    private String firstName, lastName, phoneNumber, groupId;

    public Contact(String firstName, String lastName, String phoneNumber, String groupId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.groupId = groupId;
    }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getGroupId() { return groupId; }
    public void setFirstName(String value) { firstName = value; }
    public void setLastName(String value) { lastName = value; }
    public void setPhoneNumber(String value) { phoneNumber = value; }
    public void setGroupId(String value) { groupId = value; }
    @Override public String toString() {
        return firstName + " " + lastName + " | " + phoneNumber + " | Group: " + groupId;
    }
}

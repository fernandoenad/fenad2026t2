package cmsv3;
import java.util.List;
public interface ContactGroupDAO {
    void addGroup(ContactGroup group) throws ContactGroupException;
    List<ContactGroup> getAllGroups();
    ContactGroup findGroup(String id);
    void deleteGroup(String id) throws ContactGroupException;
}

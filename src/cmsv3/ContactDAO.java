package cmsv3;
import java.util.List;
public interface ContactDAO {
    void addContact(Contact contact) throws ContactException;
    List<Contact> getAllContacts();
    List<Contact> searchContacts(String keyword);
    void updateContact(String phone, Contact replacement) throws ContactException;
    void deleteContact(String phone) throws ContactException;
}

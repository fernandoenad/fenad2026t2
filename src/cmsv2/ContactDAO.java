package cmsv2;

import java.util.List;

public interface ContactDAO {
    void addContact(Contact contact);
    List<Contact> getAllContacts();
    List<Contact> searchContacts(String keyword);
    boolean updateContact(String phoneNumber, Contact updatedContact);
    boolean deleteContact(String phoneNumber);
}

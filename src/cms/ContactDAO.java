/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cms;

import java.util.List;

/**
 *
 * @author fernandoenad
 */
public interface ContactDAO {
    public boolean addContact(Contact newContact);
    public List<Contact> getAllContacts();
    public List<Contact> searchContacts(String keyword);
    public boolean updateContact(String phoneNumber, Contact updatedContact);
    public boolean deleteContact(String phoneNumber);
}

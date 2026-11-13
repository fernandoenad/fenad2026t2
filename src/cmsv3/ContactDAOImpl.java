package cmsv3;
import java.io.*;
import java.util.*;

public class ContactDAOImpl implements ContactDAO {
    private final List<Contact> contacts = new ArrayList<>();
    private static final String FILE = "contacts_basic.dat";

    public ContactDAOImpl() throws ContactException { load(); }

    private void save() throws ContactException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(new ArrayList<>(contacts));
        } catch (IOException e) {
            throw new ContactException("Could not save contacts: " + e.getMessage());
        }
    }

    private void load() throws ContactException {
        if (!new File(FILE).exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            Object data = in.readObject();
            if (!(data instanceof ArrayList<?>)) throw new IOException("Invalid contacts file");
            for (Object item : (ArrayList<?>) data) {
                if (!(item instanceof Contact)) throw new IOException("Invalid contact record");
                contacts.add((Contact) item);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ContactException("Could not load contacts: " + e.getMessage());
        }
    }

    public void addContact(Contact contact) throws ContactException {
        for (Contact c : contacts) if (c.getPhoneNumber().equals(contact.getPhoneNumber()))
            throw new ContactException("Phone number already exists.");
        contacts.add(contact);
        save();
    }

    public List<Contact> getAllContacts() { return new ArrayList<>(contacts); }

    public List<Contact> searchContacts(String keyword) {
        List<Contact> found = new ArrayList<>();
        for (Contact c : contacts) {
            if (c.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                c.getLastName().toLowerCase().contains(keyword.toLowerCase())) found.add(c);
        }
        return found;
    }

    public void updateContact(String phone, Contact replacement) throws ContactException {
        for (Contact other : contacts) {
            if (!other.getPhoneNumber().equals(phone) &&
                other.getPhoneNumber().equals(replacement.getPhoneNumber()))
                throw new ContactException("New phone number already exists.");
        }
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getPhoneNumber().equals(phone)) {
                contacts.set(i, replacement);
                save();
                return;
            }
        }
        throw new ContactException("Contact not found.");
    }

    public void deleteContact(String phone) throws ContactException {
        if (!contacts.removeIf(c -> c.getPhoneNumber().equals(phone)))
            throw new ContactException("Contact not found.");
        save();
    }
}

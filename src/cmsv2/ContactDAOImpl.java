package cmsv2;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl implements ContactDAO {
    private static final Path DATA_FILE = Paths.get("contacts.dat");
    private final List<Contact> contacts = new ArrayList<>();

    public ContactDAOImpl() {
        loadContacts();
    }

    // Deserialization: file -> ArrayList (when application starts).
    private void loadContacts() {
        if (!Files.exists(DATA_FILE)) return; // New installation: empty contact list.

        try (ObjectInputStream in = new ObjectInputStream(
                Files.newInputStream(DATA_FILE))) {
            Object stored = in.readObject();
            if (!(stored instanceof ArrayList<?>)) {
                throw new IOException("Saved data is not an ArrayList.");
            }
            ArrayList<?> loaded = (ArrayList<?>) stored;
            for (Object item : loaded) {
                if (!(item instanceof Contact)) {
                    throw new IOException("Saved data contains an invalid record.");
                }
            }
            contacts.clear();
            for (Object item : loaded) contacts.add((Contact) item);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(
                "Cannot load contacts.dat; original file was left untouched: "
                + e.getMessage(), e);
        }
    }

    // Serialization: ArrayList -> file (after successful changes).
    private void saveContacts() {
        Path temp = DATA_FILE.resolveSibling("contacts.dat.tmp");
        try {
            try (ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(temp))) {
                out.writeObject(new ArrayList<>(contacts));
            }
            try {
                Files.move(temp, DATA_FILE, StandardCopyOption.REPLACE_EXISTING,
                           StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temp, DATA_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            try { Files.deleteIfExists(temp); } catch (IOException ignored) { }
            throw new IllegalStateException("Could not save contacts: " + e.getMessage(), e);
        }
    }

    @Override
    public void addContact(Contact contact) {
        for (Contact c : contacts) {
            if (c.getPhoneNumber().equals(contact.getPhoneNumber())) {
                System.out.println("Phone number already exists.");
                return;
            }
        }
        contacts.add(contact);
        saveContacts();
        System.out.println("Contact added successfully.");
    }

    @Override
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    @Override
    public List<Contact> searchContacts(String keyword) {
        List<Contact> results = new ArrayList<>();
        String search = keyword.toLowerCase();
        for (Contact contact : contacts) {
            if (contact.getFirstName().toLowerCase().contains(search)
                    || contact.getLastName().toLowerCase().contains(search)) {
                results.add(contact);
            }
        }
        return results;
    }

    @Override
    public boolean updateContact(String phoneNumber, Contact updatedContact) {
        for (Contact contact : contacts) {
            if (!contact.getPhoneNumber().equals(phoneNumber)
                    && contact.getPhoneNumber().equals(updatedContact.getPhoneNumber())) {
                return false;
            }
        }
        for (Contact contact : contacts) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                contact.setFirstName(updatedContact.getFirstName());
                contact.setLastName(updatedContact.getLastName());
                contact.setPhoneNumber(updatedContact.getPhoneNumber());
                saveContacts();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteContact(String phoneNumber) {
        boolean removed = contacts.removeIf(
                contact -> contact.getPhoneNumber().equals(phoneNumber));
        if (removed) saveContacts();
        return removed;
    }
}

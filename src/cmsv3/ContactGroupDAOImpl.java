package cmsv3;
import java.io.*;
import java.util.*;

public class ContactGroupDAOImpl implements ContactGroupDAO {
    private final List<ContactGroup> groups = new ArrayList<>();
    private static final String FILE = "groups_basic.dat";

    public ContactGroupDAOImpl() throws ContactGroupException { load(); }

    private void save() throws ContactGroupException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(new ArrayList<>(groups));
        } catch (IOException e) {
            throw new ContactGroupException("Could not save groups: " + e.getMessage());
        }
    }

    private void load() throws ContactGroupException {
        if (!new File(FILE).exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            Object data = in.readObject();
            if (!(data instanceof ArrayList<?>)) throw new IOException("Invalid groups file");
            for (Object item : (ArrayList<?>) data) {
                if (!(item instanceof ContactGroup)) throw new IOException("Invalid group record");
                groups.add((ContactGroup) item);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ContactGroupException("Could not load groups: " + e.getMessage());
        }
    }

    public void addGroup(ContactGroup group) throws ContactGroupException {
        if (findGroup(group.getGroupId()) != null)
            throw new ContactGroupException("Group ID already exists.");
        groups.add(group);
        save();
    }
    public List<ContactGroup> getAllGroups() { return new ArrayList<>(groups); }
    public ContactGroup findGroup(String id) {
        for (ContactGroup g : groups)
            if (g.getGroupId().equalsIgnoreCase(id)) return g;
        return null;
    }
    public void deleteGroup(String id) throws ContactGroupException {
        if (!groups.removeIf(g -> g.getGroupId().equalsIgnoreCase(id)))
            throw new ContactGroupException("Group not found.");
        save();
    }
}

package cmsv3;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            ContactGroupDAO groupDAO = new ContactGroupDAOImpl();
            ContactDAO contactDAO = new ContactDAOImpl();
            int choice = -1;
            while (choice != 0) {
                System.out.println("\n1 Add group | 2 View groups | 3 Add contact | 4 View contacts");
                System.out.println("5 Search contacts | 6 Update contact | 7 Delete contact | 8 Delete group | 0 Exit");
                System.out.print("Choice: ");
                try {
                    choice = Integer.parseInt(sc.nextLine());
                    switch (choice) {
                        case 1: {
                            System.out.print("Group ID: "); String id = sc.nextLine().trim();
                            System.out.print("Group name: "); String name = sc.nextLine().trim();
                            if (id.isEmpty() || name.isEmpty()) throw new IllegalArgumentException("Group fields required.");
                            groupDAO.addGroup(new ContactGroup(id, name));
                            System.out.println("Group saved."); break;
                        }
                        case 2:
                            for (ContactGroup g : groupDAO.getAllGroups()) System.out.println(g);
                            break;
                        case 3:
                            Contact c = readContact(sc, groupDAO);
                            contactDAO.addContact(c); System.out.println("Contact saved."); break;
                        case 4:
                            for (Contact c2 : contactDAO.getAllContacts()) System.out.println(c2);
                            break;
                        case 5: {
                            System.out.print("Search name: "); String name = sc.nextLine();
                            for (Contact c2 : contactDAO.searchContacts(name)) System.out.println(c2);
                            break;
                        }
                        case 6: {
                            System.out.print("Existing phone: "); String phone = sc.nextLine();
                            contactDAO.updateContact(phone, readContact(sc, groupDAO));
                            System.out.println("Contact updated."); break;
                        }
                        case 7: {
                            System.out.print("Phone to delete: "); String phone = sc.nextLine();
                            contactDAO.deleteContact(phone); System.out.println("Contact deleted."); break;
                        }
                        case 8: {
                            System.out.print("Group ID to delete: "); String id = sc.nextLine().trim();
                            // Main checks the relationship; DAOs remain independent.
                            for (Contact member : contactDAO.getAllContacts()) {
                                if (member.getGroupId().equalsIgnoreCase(id))
                                    throw new ContactGroupException("Group still has contacts.");
                            }
                            groupDAO.deleteGroup(id); System.out.println("Group deleted."); break;
                        }
                        case 0: System.out.println("Goodbye."); break;
                        default: System.out.println("Choose 0 to 8.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a numeric menu choice.");
                } catch (ContactException | ContactGroupException | IllegalArgumentException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
        } catch (ContactException | ContactGroupException e) {
            System.out.println("Startup error: " + e.getMessage());
        }
    }

    private static Contact readContact(Scanner sc, ContactGroupDAO groupDAO) {
        System.out.print("First name: "); String first = sc.nextLine().trim();
        System.out.print("Last name: "); String last = sc.nextLine().trim();
        System.out.print("Phone number: "); String phone = sc.nextLine().trim();
        System.out.print("Group ID (blank = unassigned): "); String group = sc.nextLine().trim();
        if (first.isEmpty() || last.isEmpty() || phone.isEmpty())
            throw new IllegalArgumentException("Name and phone are required.");
        if (!group.isEmpty() && groupDAO.findGroup(group) == null)
            throw new IllegalArgumentException("Group does not exist.");
        return new Contact(first, last, phone, group);
    }
}

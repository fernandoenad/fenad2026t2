package cmsv2;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactDAO dao;
        try {
            dao = new ContactDAOImpl();
        } catch (IllegalStateException e) {
            System.err.println("Startup error: " + e.getMessage());
            scanner.close();
            return;
        }

        int choice;
        do {
            System.out.println("\n===== CONTACT MANAGEMENT =====");
            System.out.println("1. Add Contact");
            System.out.println("2. View All Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Update Contact");
            System.out.println("5. Delete Contact");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                choice = -1;
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("First Name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Last Name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Phone Number: ");
                        String phone = scanner.nextLine();
                        dao.addContact(new Contact(firstName, lastName, phone));
                        break;
                    case 2:
                        displayContacts(dao.getAllContacts());
                        break;
                    case 3:
                        System.out.print("Enter name to search: ");
                        String keyword = scanner.nextLine();
                        displayContacts(dao.searchContacts(keyword));
                        break;
                    case 4:
                        System.out.print("Enter existing phone number: ");
                        String oldPhone = scanner.nextLine();
                        System.out.print("New First Name: ");
                        String newFirst = scanner.nextLine();
                        System.out.print("New Last Name: ");
                        String newLast = scanner.nextLine();
                        System.out.print("New Phone Number: ");
                        String newPhone = scanner.nextLine();
                        boolean updated = dao.updateContact(oldPhone,
                                new Contact(newFirst, newLast, newPhone));
                        System.out.println(updated ? "Contact updated successfully."
                                : "Update failed: contact not found or phone number already exists.");
                        break;
                    case 5:
                        System.out.print("Enter phone number to delete: ");
                        String deletePhone = scanner.nextLine();
                        boolean deleted = dao.deleteContact(deletePhone);
                        System.out.println(deleted ? "Contact deleted successfully."
                                : "Contact not found.");
                        break;
                    case 0:
                        System.out.println("Exiting application.");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (IllegalStateException e) {
                System.err.println("Storage error: " + e.getMessage());
                System.err.println("Please resolve the file problem before changing more records.");
                break;
            }
        } while (choice != 0);
        scanner.close();
    }

    private static void displayContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\n--- CONTACT LIST ---");
        for (Contact contact : contacts) {
            System.out.println(contact);
        }
    }
}

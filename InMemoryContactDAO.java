import java.util.ArrayList;
import java.util.List;

public class InMemoryContactDAO implements ContactDAO {
    private List<Contact> contacts;

    public InMemoryContactDAO() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void updateContact(Contact contact) {
        int index = contacts.indexOf(contact);
        if (index != -1) {
            contacts.set(index, contact);
        }
    }

    public void deleteContact(Contact contact) {
        contacts.remove(contact);
    }

    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }
}
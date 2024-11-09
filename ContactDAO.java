import java.util.List;

public interface ContactDAO {
    void addContact(Contact contact);
    void updateContact(Contact contact);
    void deleteContact(Contact contact);
    List<Contact> getAllContacts();
}
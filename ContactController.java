import java.util.List;

public class ContactController {
    private ContactDAO contactDAO;

    public ContactController(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    public void addContact(String name, String phone, String email) {
        Contact contact = new Contact(name, phone, email);
        contactDAO.addContact(contact);
    }

    public void updateContact(Contact contact) {
        contactDAO.updateContact(contact);
    }

    public void deleteContact(Contact contact) {
        contactDAO.deleteContact(contact);
    }

    public List<Contact> getAllContacts() {
        return contactDAO.getAllContacts();
    }
}
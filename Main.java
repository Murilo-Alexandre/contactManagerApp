public class Main {
    public static void main(String[] args) {
        ContactDAO contactDAO = new InMemoryContactDAO();
        ContactController controller = new ContactController(contactDAO);
        new SwingInterface(controller); // Inicia a interface swing
    }
}
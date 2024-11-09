import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwingInterface {
    private ContactController controller;
    private JFrame frame;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;

    public SwingInterface(ContactController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Gerenciador de Contatos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // painel input
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Nome:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Telefone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        frame.add(inputPanel, BorderLayout.NORTH);

        // table contatos
        tableModel = new DefaultTableModel(new Object[]{"Nome", "Telefone", "Email"}, 0);
        contactTable = new JTable(tableModel);
        frame.add(new JScrollPane(contactTable), BorderLayout.CENTER);

        // botoes
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Adicionar Contato");
        JButton deleteButton = new JButton("Remover Contato");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // acoes botoes
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });

        // mascara telefone
        applyPhoneMask();

        // carregar os contatos na tabela
        loadContacts();

        frame.setVisible(true);
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        // validaçao nome vazio
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Preencha o nome.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // validacao do email "@" e "."
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(frame, "O email deve conter um '@' e um dominio. Ex: '@gmail.com'", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // se todas as validacoes passaram
        controller.addContact(name, phone, email);
        loadContacts();
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow >= 0) {
            List<Contact> contacts = controller.getAllContacts();
            Contact contact = contacts.get(selectedRow);
            controller.deleteContact(contact);
            loadContacts();
        } else {
            JOptionPane.showMessageDialog(frame, "Selecione um contato para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadContacts() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Contact> contacts = controller.getAllContacts();
        for (Contact contact : contacts) {
            tableModel.addRow(new Object[]{contact.getName(), contact.getPhone(), contact.getEmail()});
        }
    }

    // validacao de email "@" e "."
    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // aplica a mascara do telefone enquanto digitado
    private void applyPhoneMask() {
        phoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                // apaga caracteres nao numericos, exceto espaco e Delete
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume(); // ignora a tecla
                    return;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String text = phoneField.getText().replaceAll("[^0-9]", ""); // remove tudo que nao e numero

                // limita o numero de caracteres para 11
                if (text.length() > 11) {
                    text = text.substring(0, 11);
                }

                // formata o telefone enquanto digita
                if (text.length() == 10) {
                    text = "(" + text.substring(0, 2) + ") " + text.substring(2, 6) + "-" + text.substring(6);
                } else if (text.length() == 11) {
                    text = "(" + text.substring(0, 2) + ") " + text.substring(2, 7) + "-" + text.substring(7);
                } else if (text.length() > 0) {
                    if (text.length() <= 2) {
                        text = "(" + text;
                    } else if (text.length() > 2 && text.length() <= 6) {
                        text = "(" + text.substring(0, 2) + ") " + text.substring(2);
                    } else if (text.length() > 6) {
                        text = "(" + text.substring(0, 2) + ") " + text.substring(2, 6) + "-" + text.substring(6);
                    }
                }

                phoneField.setText(text); // atualiza o campo de texto com a formatação
            }
        });

        phoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String text = phoneField.getText().replaceAll("[^0-9]", ""); // remove tudo que nao e numero
                if (text.length() >= 11) {
                    e.consume(); // limita a de 11 caracteres
                }
            }
        });
    }
}

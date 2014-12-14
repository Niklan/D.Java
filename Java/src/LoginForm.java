import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login form.
 */
public class LoginForm {
    private JTextField loginField;
    private JLabel loginLabel;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JPanel loginPanel;
    private JButton login;
    private JTextField websiteUrlField;
    private JLabel websiteLabel;

    public LoginForm() {
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Saves website URL.
                DrupalApi.websiteUrl = websiteUrlField.getText();

                // Prepare JSON data.
                JSONObject sendData = new JSONObject();
                sendData.put("username", loginField.getText());
                sendData.put("password", passwordField.getText());
                sendData.toJSONString();
                DrupalApi drupal = new DrupalApi();
                try {
                    if (drupal.auth(sendData)) {
                        // Remember successful url.
                        DrupalApi.websiteUrl = websiteUrlField.getText();
                    } else {
                        System.out.println("Something goes wrong. Cause problems: wrong username, wrong password, site is not available.");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main() {
        JFrame frame = new JFrame("LoginForm");
        frame.setContentPane(new LoginForm().loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Drupal auth");
        frame.pack();
        frame.setVisible(true);
    }
}

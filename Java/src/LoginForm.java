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
                Globals.drupalWebsiteUrl = websiteUrlField.getText();

                // Prepare JSON data.
                JSONObject sendData = new JSONObject();
                sendData.put("username", loginField.getText());
                sendData.put("password", passwordField.getText());
                sendData.toJSONString();
                DrupalApi drupal = new DrupalApi();
                try {
                    if (drupal.auth(sendData)) {
                        System.out.println("Authorized, show new window.");
                    }
                    else {
                        System.out.println("Wrong username | password | url");
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

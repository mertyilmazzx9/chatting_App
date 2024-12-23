
package technologies.proven.myilmaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
	/****/
	private static final long serialVersionUID = 1L;
	private JTextField nameField, surnameField, usernameField;
	private JPasswordField passwordField;
	private JButton registerButton;

	public RegisterFrame() {
		setTitle("Kayıt Ol");
		setSize(600, 600);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel nameLabel = new JLabel("Ad:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(nameLabel, gbc);

		nameField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(nameField, gbc);

		JLabel surnameLabel = new JLabel("Soyad:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(surnameLabel, gbc);

		surnameField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(surnameField, gbc);

		JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(usernameLabel, gbc);

		usernameField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(usernameField, gbc);

		JLabel passwordLabel = new JLabel("Şifre:");
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(passwordLabel, gbc);

		passwordField = new JPasswordField(15);
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(passwordField, gbc);

		registerButton = new JButton("Kayıt Ol");
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(registerButton, gbc);


		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String surname = surnameField.getText();
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				
				if (DatabaseOperations.isUsernameTaken(username)) {
					JOptionPane.showMessageDialog(null, "Kullanıcı adı zaten mevcut. Başka bir kullanıcı adı deneyin.",
							"Hata", JOptionPane.ERROR_MESSAGE);
					usernameField.setText("");
					passwordField.setText("");
				} else {
					DatabaseOperations.registerUser(name, surname, username, password);
					new LoginFrame(); // Go back to login screen
					dispose(); // Close the register frame
				}
			}
		});
	}
}

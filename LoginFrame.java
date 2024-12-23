package technologies.proven.myilmaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
	/****/
	private static final long serialVersionUID = 1L;
	private final JTextField usernameField;
	private final JPasswordField passwordField;
	private final JButton loginButton;
	private final JRadioButton registerLabel;
	private static String loggedInUsername;
	private static int loggedInUserId;
	
	
	public LoginFrame() {
		setTitle("Giriş Yap");
		setSize(600, 600);
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		final JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(usernameLabel, gbc);

		usernameField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(usernameField, gbc);

		final JLabel passwordLabel = new JLabel("Şifre:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(passwordLabel, gbc);

		passwordField = new JPasswordField(15);
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(passwordField, gbc);

		loginButton = new JButton("Giriş Yap");
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(loginButton, gbc);

		registerLabel = new JRadioButton("kayıt ol");
		registerLabel.setForeground(Color.BLUE);
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(registerLabel, gbc);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String username = usernameField.getText();
				final String password = new String(passwordField.getPassword());
				if (DatabaseOperations.validateUser(username, password)) {
					
					loggedInUsername = username;
					
					int userId = DatabaseOperations.getUserId(username, password);
				    loggedInUserId = userId; // ID'yi sakla
				    //System.out.println(loggedInUserId);
				    
					JOptionPane.showMessageDialog(null, "Giriş Başarılı!");
					final JFrame frame = new ChatFrame();
					frame.setVisible(true);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Kullanıcı adı veya şifre yanlış.", "Hata", JOptionPane.ERROR_MESSAGE);
					usernameField.setText("");
					passwordField.setText("");
				}
			}
		});

		registerLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (registerLabel.isSelected()) {
					RegisterFrame registerFrame = new RegisterFrame();
					registerFrame.setVisible(true);
				}
			}
		});

	}
	
	public static int getUserID() {
	    return loggedInUserId; // Sadece oturum açan kullanıcıyı döndür
	}
	
	public static String[] getUsernames() {
	    return new String[] { loggedInUsername }; // Sadece oturum açan kullanıcıyı döndür
	}
	
	public static String getUsernamesString() {
	    return loggedInUsername; // Sadece oturum açan kullanıcıyı döndür
	}
}

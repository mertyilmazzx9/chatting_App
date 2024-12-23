package technologies.proven.myilmaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class OptionsFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static JComboBox<String> userComboBox;
	private JRadioButton redRadioButton;
	private JRadioButton blueRadioButton;
	private JRadioButton timeOnRadioButton;
	private JRadioButton timeOffRadioButton;
	public ChatPanel chatPanel;

	public OptionsFrame(ChatPanel chatPanel) {
		this.setChatPanel(chatPanel);

		setTitle("Settings");
		setSize(400, 300);
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel userLabel = new JLabel("Kullanıcı Seçimi:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(userLabel, gbc);
		userLabel.setVisible(false);

        userComboBox = new JComboBox<>(LoginFrame.getUsernames());
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userComboBox, gbc);
        userComboBox.setVisible(false);

		JLabel colorLabel = new JLabel("Renk Seçimi:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(colorLabel, gbc);

		redRadioButton = new JRadioButton("Kırmızı");
		blueRadioButton = new JRadioButton("Mavi");

		ButtonGroup colorGroup = new ButtonGroup();
		colorGroup.add(redRadioButton);
		colorGroup.add(blueRadioButton);

		JPanel colorPanel = new JPanel();
		colorPanel.add(redRadioButton);
		colorPanel.add(blueRadioButton);

		gbc.gridx = 1;
		gbc.gridy = 1;
		add(colorPanel, gbc);

		JLabel timeLabel = new JLabel("Mesaj Saati:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(timeLabel, gbc);

		timeOnRadioButton = new JRadioButton("Açık");
		timeOffRadioButton = new JRadioButton("Kapalı");

		ButtonGroup timeGroup = new ButtonGroup();
		timeGroup.add(timeOnRadioButton);
		timeGroup.add(timeOffRadioButton);

		JPanel timePanel = new JPanel();
		timePanel.add(timeOnRadioButton);
		timePanel.add(timeOffRadioButton);

		gbc.gridx = 1;
		gbc.gridy = 2;
		add(timePanel, gbc);

		setResizable(false);

		JButton applyButton = new JButton("Uygula");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!redRadioButton.isSelected() && !blueRadioButton.isSelected()) {
					JOptionPane.showMessageDialog(null, "Lütfen bir renk seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int userIndex = userComboBox.getSelectedIndex();
				String color = redRadioButton.isSelected() ? "red" : "blue";
				String timeDisplay = timeOnRadioButton.isSelected() ? "on" : "off";

				// Onay Dialog'u
				int result = JOptionPane.showConfirmDialog(OptionsFrame.this,
						"Değişiklikleri uygulamak istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION) {
					
					int userId = LoginFrame.getUserID();
					//System.out.println(userId);
					try {
						final boolean userOptionsExist = DatabaseOperations.userOptionsExist(userId);
						if (userOptionsExist) {
		                    // If the user exists in usersOptions table, update the settings
		                    DatabaseOperations.updateUserOptions(userId, color, timeDisplay);
		                } else {
		                    // If the user doesn't exist, insert new settings
		                    DatabaseOperations.insertUserOptions(userId, color, timeDisplay);
		                }
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					chatPanel.applySettings(userIndex, color, timeDisplay);
					
					dispose(); // Pencereyi kapat ve bellekten serbest bırak
				}
			}
		});

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		add(applyButton, gbc);
	}
	
	public ChatPanel getChatPanel() {
		return chatPanel;
	}

	public void setChatPanel(ChatPanel chatPanel) {
		this.chatPanel = chatPanel;
	}
}

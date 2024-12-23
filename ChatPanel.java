package technologies.proven.myilmaz;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatPanel extends JPanel implements Serializable {
	private static final long serialVersionUID = 1L;

	private static JTextPane text1_;
	private static JTextPane text2_;
	private JTextField message1;
	private JTextField message2;
	private ActionListener actionListener1;
	private ActionListener actionListener2;
	private JRadioButton optionsRadioButton;

	private final static String USER2_PREFIX = "Kullanıcı-2";
	private static boolean timeDisplayUser1;
	private static boolean timeDisplayUser2;
	private static String userString = LoginFrame.getUsernamesString();

	public ChatPanel() {
		setLayout(new BorderLayout());

		// Chat panelini oluşturun
		JPanel chatPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel label1 = new JLabel(userString);

		text1_ = new JTextPane();
		text2_ = new JTextPane();
		message1 = new JTextField(20);
		message2 = new JTextField(20);

		JButton button1 = new JButton("Sağa Gönder");
		JButton button2 = new JButton("Sola Gönder");

		text1_.setEditable(false);
		text2_.setEditable(false);

		actionListener1 = new SendMessageActionListener(message1, text1_, text2_, userString, this);
		actionListener2 = new SendMessageActionListener(message2, text2_, text1_, USER2_PREFIX, this);

		button1.addActionListener(actionListener1);
		message1.addActionListener(actionListener1);
		button2.addActionListener(actionListener2);
		message2.addActionListener(actionListener2);

		addComponent(chatPanel, label1, 0, 0, gbc);
		addComponent(chatPanel, message1, 0, 2, gbc);
		addComponent(chatPanel, message2, 1, 2, gbc);
		addComponent(chatPanel, button1, 0, 3, gbc);
		addComponent(chatPanel, button2, 1, 3, gbc);
		addTextPaneComponent(chatPanel, text1_, 0, 1, gbc);

		// KeyEventDispatcher'ı kaydedin
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!message1.getText().trim().isEmpty()) {
						DatabaseOperations.getMessage(1, 2);
						actionListener1.actionPerformed(new ActionEvent(message1, ActionEvent.ACTION_PERFORMED, null));
					} else if (!message2.getText().trim().isEmpty()) {
						DatabaseOperations.getMessage(2, 1);
						actionListener2.actionPerformed(new ActionEvent(message2, ActionEvent.ACTION_PERFORMED, null));
					}
					return true; // Tuşun başka bir işlem yapmasını engellemek için true döndür.
				}
				return false; // İşlemi başka odaklanmış bileşenlere iletmek için false döndür.
			}
		});

		// RadioButton ve ButtonGroup
		optionsRadioButton = new JRadioButton("Options");
		ButtonGroup optionsGroup = new ButtonGroup();
		optionsGroup.add(optionsRadioButton);

		// Options radio button için ActionListener
		optionsRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (optionsRadioButton.isSelected()) {
					OptionsFrame optionsFrame = new OptionsFrame(ChatPanel.this);
					optionsFrame.setVisible(true);
				}
			}
		});

		// Ayarlar panelini oluşturun
		JPanel optionsPanel = new JPanel(new BorderLayout());
		optionsPanel.add(optionsRadioButton, BorderLayout.NORTH);

		add(optionsPanel, BorderLayout.NORTH);
		add(chatPanel, BorderLayout.CENTER);
	}

	private void addComponent(JPanel panel, JComponent component, int gridx, int gridy, GridBagConstraints gbc) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		panel.add(component, gbc);
	}

	private void addTextPaneComponent(JPanel panel, JTextPane textPane, int gridx, int gridy, GridBagConstraints gbc) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridwidth = 2;
		JPanel textPanePanel = new JPanel(new BorderLayout());
		textPanePanel.add(new JScrollPane(textPane), BorderLayout.CENTER);
		panel.add(textPanePanel, gbc);
	}

	public static class SendMessageActionListener implements ActionListener {
		private JTextField messageField;
		private JTextPane userChatPane;
		private JTextPane otherChatPane;
		private String userPrefix;
		public ChatPanel chatPanel;

		public SendMessageActionListener(JTextField messageField, JTextPane userChatPane, JTextPane otherChatPane,
				String userPrefix, ChatPanel chatPanel) {
			this.messageField = messageField;
			this.userChatPane = userChatPane;
			this.otherChatPane = otherChatPane;
			this.userPrefix = userPrefix;
			this.chatPanel = chatPanel;
		}

//        ***************************************************
//        Mesaj gönderildiğinde önce veritabanına kaydediliyor.
//        Daha sonra veritabanından son mesaj çekilerek JTextPane'de gösteriliyor.
//        Mesajlar, kullanıcının hangi penceresindeyse ona göre hizalanıyor (sağa veya sola).
//        ***************************************************

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String messageText = messageField.getText().trim();

			if (!messageText.isEmpty()) {
				int senderId = userPrefix.equals(ChatPanel.userString) ? 1 : 2;
				int receiverId = senderId == 1 ? 2 : 1;
				// Mesajı veritabanına kaydet
				DatabaseOperations.insertMessage(senderId, receiverId, messageText);

				// Veritabanından son mesajı çek
				String text = DatabaseOperations.getMessage(senderId, receiverId);

				if (!text.isEmpty()) {
					StyledDocument doc = text1_.getStyledDocument();
					
					//red color
					SimpleAttributeSet redAttr = new SimpleAttributeSet();
					StyleConstants.setForeground(redAttr, Color.RED);
					
					//blue color
					SimpleAttributeSet blueAttr = new SimpleAttributeSet();
					StyleConstants.setForeground(blueAttr, Color.BLUE);

					//black color
					SimpleAttributeSet blackAttr = new SimpleAttributeSet();
					StyleConstants.setForeground(blackAttr, Color.BLACK);

					String formattedMessage = userPrefix + ": " + text;
					
					// kullanıcıya renk setliyor
					int userid = LoginFrame.getUserID();
					String colorString = DatabaseOperations.getUserColorByUserId(userid);
					String dateString = DatabaseOperations.getUserDateByUserId(userid);
					//System.out.println(dateString);

					// Tarihi ekle
					if ("on".equals(dateString) && senderId == 1) {
					    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM - HH:mm");
					    String formattedDateTime = LocalDateTime.now().format(formatter);
					    formattedMessage += " [" + formattedDateTime + "]";
					} else {
					    formattedMessage += " [kapalı]";
					}

					try {
						if (colorString.equals("red") && senderId == 1) {
							doc.insertString(doc.getLength(), formattedMessage, redAttr);
							appendMessage(userChatPane, formattedMessage, true);
							
						} else if (colorString.equals("blue") && senderId == 1) {
							doc.insertString(doc.getLength(), formattedMessage, blueAttr);
							appendMessage(userChatPane, formattedMessage, true);
							
						} else {
							doc.insertString(doc.getLength(), formattedMessage, blackAttr);
						}
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					// Diğer alana sola hizalı ekle
					appendMessage(otherChatPane, formattedMessage, false);

					messageField.setText(""); // TextField'ı temizle
				}
			} else {
				JOptionPane.showMessageDialog(null, "Boş mesaj gönderemezsiniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
			}
		}

		private void appendMessage(JTextPane chatPane, String message, boolean isRightAligned) {
			StyledDocument doc = chatPane.getStyledDocument();
			SimpleAttributeSet alignment = new SimpleAttributeSet();

			if (isRightAligned) {
				StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_RIGHT);
			} else {
				StyleConstants.setAlignment(alignment, StyleConstants.ALIGN_LEFT);
			}

			try {
				int length = doc.getLength();
				doc.insertString(length, "" + "\n", alignment); // boş string basıyor, panlaması için böyle bir çözüm buldum.
				doc.setParagraphAttributes(length, message.length(), alignment, false);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isTimeDisplayEnabledForUser(String userPrefix) {
		if (userPrefix.equals(userString)) {
			return timeDisplayUser1;
		} else if (userPrefix.equals(USER2_PREFIX)) {
			return timeDisplayUser2;
		}
		return false;
	}

	public void applySettings(int userIndex, String color, String timeDisplay) {

		if (userIndex == 0) {
			timeDisplayUser1 = timeDisplay.equals("on");
		} else if (userIndex == 1)  {
			timeDisplayUser1 = timeDisplay.equals("off");
		}
	}
}

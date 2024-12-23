package technologies.proven.myilmaz;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ChatApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new LoginFrame();
			frame.setVisible(true);
			
			// veritabanı bağlantısı açılır.
			DatabaseConnection.connect();
		});
	}
}
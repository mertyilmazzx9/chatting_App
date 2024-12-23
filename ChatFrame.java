package technologies.proven.myilmaz;

import javax.swing.JFrame;
//import java.awt.GridBagLayout;

public class ChatFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public ChatFrame() {

		setTitle("Chat-Proven");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		//setLayout(new GridBagLayout());

		// `ChatPanel` sınıfı, `ChatFrame`'e doğru şekilde eklendi.
		add(new ChatPanel());

		setLocationRelativeTo(null); // ekran ortada olur
		setResizable(false); // boyut ayarlamasına izin vermez.
	}
}

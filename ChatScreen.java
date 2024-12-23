package technologies.proven.myilmaz;

import javax.swing.*;
import java.awt.*;


public class ChatScreen {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Chat Screen");
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);
		
		//PANEL'İ, LABEL'I , BUTTONLARI VE TEXTFIELD'I OLUŞTURMA
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Adınızı Girin:"); 
		JTextField text = new JTextField(10); // max 10 karakter alıyor
		JButton send_button = new JButton("Send");
		JButton reset_button = new JButton("Reset");
		panel.add(label);
		panel.add(text);
		panel.add(send_button);
		panel.add(reset_button);
		
		JTextArea t_area = new JTextArea();
		
		
		//MENU KISMI
		/*
		JMenu menu= new JMenu("Options");
		
		JMenuItem a1 = new JMenuItem("File");
		JMenuItem a2 = new JMenuItem("System");
		
		JMenuBar menubar = new JMenuBar();
		
		menubar.add(menu);
		menu.add(a1);
		menu.add(a2);
		frame.setJMenuBar(menubar);
		*/
		
		// FRAME'E PANEL, TEXTAREA'LARI EKLEME
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
		frame.getContentPane().add(BorderLayout.PAGE_START, text);
		frame.getContentPane().add(BorderLayout.CENTER, t_area);
		frame.setVisible(true);
		
		

		
		frame.setLocationRelativeTo(null);
		
		
		
		
	}

}

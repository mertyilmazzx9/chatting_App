package technologies.proven.myilmaz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;


public class DatabaseOperations {

	// user ayarları varsa return true
	public static boolean userOptionsExist(int userId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM users_options WHERE userid = ?";
		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // If count > 0, options exist
			} else {
				return false;
			}
		}
	}

	// user_options'da update yapan kod
	public static void updateUserOptions(int userId, String color, String timeDisplay) {
		String sql = "UPDATE users_options SET color = ?, date = ? WHERE userID = ?";
		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, color);
			pstmt.setString(2, timeDisplay);
			pstmt.setInt(3, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// hep son mesajı getiren sql | return message
	public static String getMessage(int sender_userid, int receiver_userid) {
		String message = "";
		// Son mesajı çekmek için tarih bazında sıralama yap
		String sql = "SELECT message FROM users_message_history WHERE sender_userid = ? AND receiver_userid = ? ORDER BY date DESC LIMIT 1";

		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, sender_userid); // Gönderen kullanıcı ID'sini ekle
			pstmt.setInt(2, receiver_userid); // Alıcı kullanıcı ID'sini ekle

			ResultSet rs = pstmt.executeQuery();

			// Son mesajı çek
			if (rs.next()) {
				message = rs.getString("message");
				//System.out.println("mesaj çekildi");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	// login olan user'ın messageDate(date) bilgisini return eder | return date
	public static String getUserDateByUserId(int userID) {
		String query = "SELECT date FROM users_options WHERE userID = ?";
		String color = null;

		try (Connection conn = DatabaseConnection.connect();
				PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setInt(1, userID);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				color = resultSet.getString("date");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return color;
	}
	
	// login olan user'ın rengini(color) return eder | return color
	public static String getUserColorByUserId(int userID) {
		String query = "SELECT color FROM users_options WHERE userID = ?";
		String color = null;

		try (Connection conn = DatabaseConnection.connect();
				PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setInt(1, userID);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				color = resultSet.getString("color");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return color;
	}


	// user_options'a ayarları(color,date) insert eden kod
	public static void insertUserOptions(int userId, String color, String timeDisplay) {
		String sql = "INSERT INTO users_options (userid, color, date) VALUES (?, ?, ?)";
		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setString(2, color);
			pstmt.setString(3, timeDisplay);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	

	// user_messages_history'ye bilgileri(sender_userID, receiver_userID, message, date) insert eden kod
	public static void insertMessage(int senderId, int receiverId, String message) {
		String sql = "INSERT INTO users_message_history (sender_userID, receiver_userID, message, date) VALUES (?, ?, ?, now())";
		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, senderId);
			pstmt.setInt(2, receiverId);
			pstmt.setString(3, message);
			pstmt.executeUpdate();
			//System.out.println("insert etti");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// user'ın ID bilgisini return eden kod
	public static int getUserId(String username, String password) {
		final String hashedPassword = SHA256Hashing.generateSHA256Hash("abc" + password);
		//System.out.println(hashedPassword);
	    int userId = -1; // Kullanıcının ID'sini saklamak için
	    String sql = "SELECT id FROM users WHERE username = ? AND password = ?";

	    try (Connection conn = DatabaseConnection.connect();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, username); // Kullanıcı adını sorguya ekleyin
	        pstmt.setString(2, hashedPassword); // Şifreyi sorguya ekleyin

	        ResultSet rs = pstmt.executeQuery();

	        // Eğer kullanıcı bulunduysa, ID'yi alın
	        if (rs.next()) {
	            userId = rs.getInt("id");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return userId; // Kullanıcının ID'si döndürülür
	}
	
	//user'ı kayıt ederken çalışan kod
	public static void registerUser(String name, String surname,String username,String password ) {
		String hashedPassword = SHA256Hashing.generateSHA256Hash("abc" + password);
		String sql = "INSERT INTO users (name, surname, username, password) VALUES (?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, surname);
			pstmt.setString(3, username);
			pstmt.setString(4, hashedPassword);
			pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "Kayıt Başarılı!");
			int userId = DatabaseOperations.getUserId(username, password);
			
			DatabaseOperations.insertUserOptions(userId, "red", "off");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//kayıt olurken veritabanında username hali hazırda varsa hata verir
	static boolean isUsernameTaken(String username) {
		String sql = "SELECT * FROM users WHERE username = ?";

		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return rs.next(); // Return true if username exists
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//login olurken validasyon yaparken kullanıyorum , username ve password eşleşiyorsa true dönüyor
	public static boolean validateUser(String username, String password) {
		final String hashedPassword = SHA256Hashing.generateSHA256Hash("abc" + password);
		//System.out.println(hashedPassword);
		final String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, hashedPassword);
			final ResultSet rs = pstmt.executeQuery();
			return rs.next(); // Return true if user exists
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}

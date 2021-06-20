import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class DatabaseConnection {
	private static String url = "";
	private static String dbName = "PingPongGame";
	private static String driver = "com.mysql.jdbc.Driver";
	private static String userName = "PingPongGame";
	private static String password = "";
	private static Connection conn;

	public static Connection getDBConnection(){
		return conn;
	}

	public static void openConnection(){
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName,userName,password);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage().toString());
		}

	}

	public static void closeConnection(){
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static Vector<String> getPlayerNames() throws SQLException{
		Vector<String> players = new Vector<String>();
		
		Statement stmt = null;
		String query = "select distinct leader_board.name from PingPongGame2.leader_board";
		stmt = DatabaseConnection.getDBConnection().createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()){
			players.add(rs.getString("name"));
		}
		rs.close();
		stmt.close();
		return players;
	}
	
	public static Vector<Player> getPlayers() throws SQLException{
		Vector<Player> players = new Vector<Player>();
		
		Statement stmt = null;
		String query = "select * from PingPongGame2.leader_board order by leader_board.high_score desc";
		stmt = DatabaseConnection.getDBConnection().createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()){
			String name = rs.getString("name");
			String pid = rs.getString("id");
			int score = rs.getInt("high_score");
			ImageIcon img = null;
			img = new ImageIcon(getImage("strawberry.jpg"));
			Player player = new Player(name,score,img);
			players.add(player);
		}
		rs.close();
		stmt.close();
		return players;
	}
	
	public ImageIcon getAvi(int pid){
		ImageIcon icon = null;
		
		return icon;
	}
	
	
	public static BufferedImage getImage(String fileName){
		try {
			if (fileName.equals("")){
				return ImageIO.read(new File("blank.jpg"));
			}
			else{
				return ImageIO.read(new File("assets/"+fileName));
			}
		} catch (IOException e) {
		}
		return null;

	}
	
	public static void setScores(String player1Name, int score1, String player2Name, int score2) throws SQLException{
		if(!player1Name.equals("") && !player2Name.equals("") ){
			PreparedStatement preparedStatement = null;

			String interstString = "insert into PingPongGame2.leader_board (name,high_score) values(?,?)";
			preparedStatement = DatabaseConnection.getDBConnection().prepareStatement(interstString);

			preparedStatement.setString(1,player1Name);
			preparedStatement.setInt(2,score1);
			preparedStatement.execute();


			preparedStatement.setString(1,player2Name);
			preparedStatement.setInt(2,score2);
			preparedStatement.execute();
		}

	}
}


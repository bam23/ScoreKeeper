import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.lang.String;



public class ScoreBoard extends JFrame {
	private static final long serialVersionUID = -115910543284007058L;
	private PlayerPanel player1Panel = new PlayerPanel();
	private PlayerPanel player2Panel = new PlayerPanel();
	private Controller controller;
	private Vector<ImageIcon> singleNumbers = new Vector<ImageIcon>();
	private Vector<ImageIcon> doubleNumbers = new Vector<ImageIcon>();
	
	public ScoreBoard(String title, String player1, String player2){
		super(title);
		setLayout(new Layout());
		setSize(1920,1080);
		controller = new Controller(player1,player2);
		
		for(int i = 0; i < 10; i++){
			singleNumbers.add(new ImageIcon(DatabaseConnection.getImage(i +".jpg")));
			doubleNumbers.add(new ImageIcon(DatabaseConnection.getImage(i +"s.jpg")));
		}

		if(singleNumbers.size()<9 || doubleNumbers.size()<9){
			System.err.print("Could not retrive numbers");
			
		}
		
		controller.singleImageLabel1 = new JLabel(singleNumbers.get(0));
		player1Panel.add("0 0 100 100",controller.singleImageLabel1);

		controller.singleImageLabel2 = new JLabel(singleNumbers.get(0));
		player2Panel.add("0 0 100 100",controller.singleImageLabel2);

		Font myFont = new Font("Arial", Font.BOLD, 24);
		JLabel playerName = new JLabel(player1);
		playerName.setFont(myFont);
		playerName.setVisible(true);
		add("1 6 49 9",playerName);
		add("1 10 49 90", player1Panel);
		
		JLabel playerName2 = new JLabel(player2);
		playerName2.setFont(myFont);
		add("51 6 99 9",playerName2);
		add("51 10 99 90", player2Panel);

		addKeyListener(new PlayerInput());
	}
		
	private class PlayerPanel extends JPanel{
		private static final long serialVersionUID = 7688219143499369682L;
		private Border border = BorderFactory.createLineBorder(Color.BLACK);

		public PlayerPanel(){
			setLayout(new Layout());
			setBorder(border);
			setVisible(true);
			setBackground(Color.BLACK);
		}	
	}

	private class PlayerInput implements KeyListener {


		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar();

			if(c == 'a'){
				controller.player1PointUp();
			}
			else if(c =='l'){
				controller.player2PointUp();
			}
			else if(c == 'g'){
				controller.endGame();

			}
			else if(c =='r'){
				ScoreBoard.this.setVisible(false);
				ScoreBoard.this.dispose();
				
			}
			ScoreBoard.this.repaint();
		}
	}

	private class Controller{
		String player1Name="";
		int player1Count = -1;	
		
		String player2Name="";
		int player2Count = -1;
		
		JLabel singleImageLabel1 = new JLabel();
		JLabel singleImageLabel2 = new JLabel();
		
		JLabel doubleImageLabel1d1 = new JLabel();
		JLabel doubleImageLabel1d2 = new JLabel();
		
		JLabel doubleImageLabel2d1 = new JLabel();
		JLabel doubleImageLabel2d2 = new JLabel();
		
		public Controller(String player1, String player2){
			player1Name=player1;
			player2Name=player2;
		}
		
		public void endGame(){
			
	//Commenting out to implement local data file
	//SQL Database is unneeded for demo
			/*
				try {
					DatabaseConnection.setScores(getPlayer1Name(), getPlayer1Score(),getPlayer2Name(),getPlayer2Score());
					new LeaderBoard("Leader Board");
					DatabaseConnection.closeConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						DatabaseConnection.getDBConnection().rollback();
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(ScoreBoard.this, e1.getMessage().toString());
					}
				}
			*/

			if( getPlayer1Score() > getPlayer2Score() ) {
				//Player 1 Won
				JOptionPane.showConfirmDialog(ScoreBoard.this, "Player 1 Wins!", "Press any key to continue", JOptionPane.CLOSED_OPTION);
			}else if (getPlayer1Score() == getPlayer2Score()) {
				// Tie Game
				JOptionPane.showConfirmDialog(ScoreBoard.this, "Tie Game!", "Press any key to continue", JOptionPane.CLOSED_OPTION);
			}else {
				//Player 2 Won
				JOptionPane.showConfirmDialog(ScoreBoard.this, "Player 2 Wins!", "Press any key to continue", JOptionPane.CLOSED_OPTION);
			}
			
			//Returns game to start menu for next game (Arcade Style)
				ScoreBoard.this.setVisible(false);
				ScoreBoard.this.dispose();
		}
		
		public String getPlayer1Name(){
			return player1Name;
		}
		

		public String getPlayer2Name(){
			return player2Name;
		}
		
		public int getPlayer1Score(){
			return player1Count;
		}
		
		public int getPlayer2Score(){
			return player2Count;
		}
		
		public void player1PointUp(){
			++player1Count;
			if(player1Count<100){
				if(player1Count>9){
					if(player1Count==10){
						player1Panel.remove(singleImageLabel1);
						player1Panel.add("0 0 50 100", doubleImageLabel1d1);
						player1Panel.add("50 0 100 100",doubleImageLabel1d2);
					}
					Image img = singleNumbers.get(player1Count/10).getImage();
					Image newimg = img.getScaledInstance(player1Panel.getWidth()/2, 
							player1Panel.getHeight(),  java.awt.Image.SCALE_SMOOTH);  
					doubleImageLabel1d1.setIcon(new ImageIcon(newimg));
					
					img = singleNumbers.get(player1Count%10).getImage();
					newimg = img.getScaledInstance(player1Panel.getWidth()/2, 
							player1Panel.getHeight(),  java.awt.Image.SCALE_SMOOTH); 
					doubleImageLabel1d2.setIcon(new ImageIcon(newimg));
				}
				else{
					singleImageLabel1.setIcon(singleNumbers.get(player1Count));
				}
			}
			player1Panel.repaint();
		}

		public void player2PointUp(){
			++player2Count;
			if(player2Count<100){
				if(player2Count>9){
					if(player2Count==10){
						player2Panel.remove(singleImageLabel2);
						player2Panel.add("0 0 50 100", doubleImageLabel2d1);
						player2Panel.add("50 0 100 100",doubleImageLabel2d2);
					}
					Image img = singleNumbers.get(player2Count/10).getImage();
					Image newimg = img.getScaledInstance(player2Panel.getWidth()/2, 
							player2Panel.getHeight(),  java.awt.Image.SCALE_SMOOTH);  
					doubleImageLabel2d1.setIcon(new ImageIcon(newimg));
					
					img = singleNumbers.get(player2Count%10).getImage();
					newimg = img.getScaledInstance(player2Panel.getWidth()/2, 
							player2Panel.getHeight(),  java.awt.Image.SCALE_SMOOTH); 
					doubleImageLabel2d2.setIcon(new ImageIcon(newimg));
				}
				else{
					singleImageLabel2.setIcon(singleNumbers.get(player2Count));
				}
			}
			player2Panel.repaint();
		}
	}
}

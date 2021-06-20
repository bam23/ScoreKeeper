import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PlayerEntry extends JFrame{
	private static final long serialVersionUID = 3855823282948284310L;
	private AutoCompleteBox player1 = new AutoCompleteBox();
	private AutoCompleteBox player2 = new AutoCompleteBox();
	private JButton playButton = new JButton("Play");

	public PlayerEntry(String title){
		super(title);
		setLayout(new Layout());
		setSize(1920,1080);
		Vector<String> names = new Vector<String>();
		
		//Implement Local Data File to save names/Scores until project grows
		/*	
	   if(DatabaseConnection.getDBConnection()!=null){
			try {
				names = DatabaseConnection.getPlayerNames();
			} catch (SQLException e) {
				DatabaseConnection.closeConnection();
				DatabaseConnection.openConnection();
			}
		}
		*/
		
		setPlayers(names,player1);
		
		setPlayers(names,player2);	
		 
		JLabel p1 = new JLabel("Player 1", JLabel.CENTER);
		add("10 40 20 50", p1);
		add("10 50 20 53",player1);
		
		JLabel p2 = new JLabel("Player 2", JLabel.CENTER);
		add("80 40 90 50", p2);
		add("80 50 90 53",player2);
		
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ScoreBoard playerEntry = new ScoreBoard("Score",
						player1.getSelectedItem().toString(),
						player2.getSelectedItem().toString());
				
				playerEntry.setVisible(true);
				dispose();
				setVisible(false);

			}
		});
		add("40 40 60 60", playButton);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if(c == 'r'){
					PlayerEntry.this.setVisible(false);
					PlayerEntry.this.dispose();
					
				}
				else if(e.getKeyCode()== KeyEvent.VK_ENTER){
					ScoreBoard playerEntry = new ScoreBoard("Score",
							player1.getSelectedItem().toString(),
							player2.getSelectedItem().toString());
					
					playerEntry.setVisible(true);
					dispose();
					setVisible(false);
				}
				
			}
		});
		
		setFocusable(true);
	}
	
	private void setPlayers(Vector<String> names, AutoCompleteBox box){
		box.addItem("");
		for(String name:names){
			box.addItem(name);
		}
	}
}

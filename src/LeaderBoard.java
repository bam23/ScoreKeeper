import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;

public class LeaderBoard extends JFrame {
	private static final long serialVersionUID = -5644578062081784921L;
	private GameResultPanel gameResultPanel = new GameResultPanel();

	public LeaderBoard(String title){
		super(title);
		setLayout(new Layout());
		setSize(1920,1080);
		getContentPane().setBackground(Color.WHITE);
		add("5 0 95 30", new JLabel(new ImageIcon(DatabaseConnection.getImage("leaderboard.jpg"))));
		add("5 30 95 95", gameResultPanel);
		setVisible(true);
		addKeyListener(new PlayerInput());
		setFocusable(true);	
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

			if(c =='r'){
				LeaderBoard.this.setVisible(false);
				LeaderBoard.this.dispose();
				
			}
			LeaderBoard.this.repaint();
		}
	}
	
	private class GameResultPanel extends JPanel{
		private static final long serialVersionUID = 8376854410505367712L;
		private Border border = BorderFactory.createLineBorder(Color.BLACK);
		private JTable leaderTable;
		public GameResultPanel(){
			setLayout(new Layout());
			setBorder(border);
			leaderTable = new JTable(new LeaderTableTableModel());
			leaderTable.getColumnModel().getColumn(0).setPreferredWidth(10);		
			TableUtilities.setPreferredRowHeights(leaderTable);		
			add("0 0 100 100",new JScrollPane(leaderTable));
		}
			
		private class LeaderTableTableModel extends AbstractTableModel{	
			private static final long serialVersionUID = 8052499381994933434L;
			private Vector<Player>players;
			//private String []colNames = {" ","Name", "Score","Victores"};
			
			private String []colNames = {"Name", "Score","Victores"};
			public LeaderTableTableModel(){
				try {
					players = new Vector<Player>(DatabaseConnection.getPlayers());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public String getColumnName(int row){
				return colNames[row];
			}
			@Override
			public int getRowCount() {
				return players.size();
			}

			@Override
			public int getColumnCount() {
				// TODO Auto-generated method stub
				return colNames.length;
			}
			
			@Override
			public Class<?> getColumnClass(int colNum) {
				switch (colNum) {
//				case 0:
//					return ImageIcon.class;
				default:
					return String.class;
				}
			} 

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				// TODO Auto-generated method stub
				Player player = players.get(rowIndex);
				
//				if(columnIndex == 0){
//					return player.getAvi();
//				}
//				else if(columnIndex == 1){
//					return player.getName();
//				}
//				else if(columnIndex == 2){
//					return player.getScore();
//				}
				
			
				if(columnIndex == 0){
					return player.getName();
				}
				else if(columnIndex == 1){
					return player.getScore();
				}

				return null;
			}	
		}	
	}
}

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;

	public class StartScreen extends JFrame{
		private static final long serialVersionUID = 1L;
		private JButton startButton = new JButton("Start");
		
		public StartScreen(String title){
			super(title);
			setLayout(new Layout());
			setSize(1920,1080);
			
			//Clicking the start button
			startButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					PlayerEntry playerEntry = new PlayerEntry("Enter Players");
					playerEntry.setVisible(true);
					//dispose();
					//setVisible(false);

				}
			});
			add("40 40 60 60", startButton);
			addKeyListener(new PlayerInput());
			setFocusable(true);
			setVisible(true);
		}
		
		//Key Listener for physical button
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
				//char c = e.getKeyChar();
				
				if(e.getKeyCode()== KeyEvent.VK_ENTER){
					PlayerEntry playerEntry = new PlayerEntry("Enter Players");
					playerEntry.setVisible(true);
					//dispose();
					//setVisible(false);
					
				}
			}
		}
	}
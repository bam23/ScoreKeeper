import javax.swing.ImageIcon;


public class Player {
	private String name;
	private int pid; // Unique identifier for each player
	private int score;
	private ImageIcon avi=null;
	
	public Player(String name, int score){
		this.name = name;
		this.score = score;
	}
	
	public Player(String name, int score, ImageIcon avi){
		this.name = name;
		this.score = score;
		this.avi = avi;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public int getScore(){
		return score;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setAvi(ImageIcon avi){
		this.avi = avi;
	}
	public ImageIcon getAvi(){
		if(avi==null)
			System.out.println("Image is null");
		
		return avi;
	}
}

package ce.modelwhilework.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class Favorite {
	private TreeSet<FavoriteCard> favoriteTaskSet;
	private TreeSet<FavoriteCard> favoriteMessageSet;
	
	public Favorite(){
		this.favoriteTaskSet = new TreeSet<FavoriteCard>();
		this.favoriteMessageSet = new TreeSet<FavoriteCard>();

		///only for testing!!!
		favoriteTaskSet.add(new FavoriteCard(new Task("default favorite 1"), 2));
//		favoriteTaskSet.add(new FavoriteCard(new Task("default favorite 2"), 2));
//		favoriteTaskSet.add(new FavoriteCard(new Task("default favorite 3"), 1));
//		favoriteTaskSet.add(new FavoriteCard(new Task("default favorite 4"), 5));
//		favoriteTaskSet.add(new FavoriteCard(new Task("default favorite 5"), 4));
		
//		favoriteMessageSet.add(new FavoriteCard(new Message("default favorite msg 1", "person", true), 5));
//		favoriteMessageSet.add(new FavoriteCard(new Message("default favorite msg 2", "person", false), 6));
//		favoriteMessageSet.add(new FavoriteCard(new Message("default favorite msg 3", "person", true), 1));
//		favoriteMessageSet.add(new FavoriteCard(new Message("default favorite msg 4", "person", false), 1));
//		favoriteMessageSet.add(new FavoriteCard(new Message("default favorite msg 5", "person", true), 2));
	}
	
	public TreeSet<FavoriteCard> getFavoriteTaskSet() {
		return favoriteTaskSet;
	}
	public void setFavoriteTaskSet(TreeSet<FavoriteCard> favoriteTaskSet) {
		this.favoriteTaskSet = favoriteTaskSet;
	}
	
	public TreeSet<FavoriteCard> getFavoriteMessageSet() {
		return favoriteMessageSet;
	}
	public void setFavoriteMessageSet(TreeSet<FavoriteCard> favoriteMessageSet) {
		this.favoriteMessageSet = favoriteMessageSet;
	}
	
	public boolean setFavoriteTaskCard(Task card){
		
		FavoriteCard favCard = this.findExistingFavoriteTask(card.getTitle());
		if (favCard != null){
			this.favoriteTaskSet.remove(favCard);
			favCard.setCount(favCard.getCount() + 1);
			this.favoriteTaskSet.add(favCard);
		}
		else{
			favCard = new FavoriteCard(new Task(card.getTitle()), 1);
			this.favoriteTaskSet.add(favCard);
		}
		
		return true;
	}
	
	public boolean setFavoriteMessageCard(Message card){
		
		FavoriteCard favCard = this.findExistingFavoriteMessage(card.getTitle());
		if (favCard != null){
			this.favoriteMessageSet.remove(favCard);
			favCard.setCount(favCard.getCount() + 1);
			this.favoriteMessageSet.add(favCard);
		}
		else{
			favCard = new FavoriteCard(new Message(card.getTitle(), card.getSenderReceiver(), card.isSender()), 1);
			this.favoriteTaskSet.add(favCard);
		}
		
		return true;
	}
	
	private FavoriteCard findExistingFavoriteTask(String title){
		for(FavoriteCard f : this.favoriteTaskSet){
			if(f.getCard().getTitle().compareTo(title) == 0){
				return f;
			}
		}	
		return null;
	}
	
	private FavoriteCard findExistingFavoriteMessage(String title){
		for(FavoriteCard f : this.favoriteMessageSet){
			if(f.getCard().getTitle().compareTo(title) == 0){
				return f;
			}
		}	
		return null;
	}
	
	public ArrayList<Task> getFavoriteTasks() {
		ArrayList<Task> alTasks = new ArrayList<Task>();
		if (this.favoriteTaskSet != null){
			for (FavoriteCard t : this.favoriteTaskSet){
				alTasks.add((Task) t.getCard());
			}			
		}
		return alTasks; 
	}
	
	public ArrayList<Message> getFavoriteMessages() { 
		ArrayList<Message> alMessages = new ArrayList<Message>();
		if (this.favoriteMessageSet != null){
			for (FavoriteCard m : this.favoriteMessageSet){
				alMessages.add((Message) m.getCard());
			}			
		}		
		return alMessages; 
		
	}
	
}

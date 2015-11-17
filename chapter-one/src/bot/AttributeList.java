package bot;

import java.util.Vector;

public class AttributeList extends Attribute implements Cloneable{
	protected Vector vec;
	
	
	/* Override Methods
	 * @see bot.Attribute#clone()
	 */
	public Object clone(){
		int i;
		AttributeList rtn = new AttributeList();
		
		for(i = 0;i < vec.size();i++){
			rtn.add((Attribute)get(i).clone());
		}
		return rtn;
	}
	
	public AttributeList(){
		super("","");
		vec = new Vector();
	}
	
	synchronized public Attribute get(int id){
		if(id < vec.size()){
			return (Attribute)vec.elementAt(id);
		}
		return null;
	}
	
	synchronized public Attribute get(String id){
		int i = 0;
		while(get(i) != null){
			if(get(i).getName().equalsIgnoreCase(id)){
				return get(i);
			}
			i++;
		}
		return null;
	}
	
	synchronized public void add(Attribute a){
		vec.addElement(a);
	}
	
	synchronized public void clear(){
		vec.removeAllElements();
	}
	
	synchronized public boolean isEmpty(){
		return (vec.size() <= 0);
	}
	
	synchronized public int length(){
		return vec.size();
	}
	
	
	synchronized public void set(String name, String value){
		if(name == null){
			return;
		}
		if(value == null){
			value = "";
		}
		
		Attribute a = get(name);
		
		if(a == null){
			a = new Attribute(name, value);
			add(a);
		}else{
			a.setValue(value);
		}
	}
}

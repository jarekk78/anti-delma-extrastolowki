package antidelma;

import java.util.Iterator;

public class DotEmailGenerator implements Iterator {
	private String login;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DotEmailGenerator( String login ) {
		this.login = login;
		this.id = 0;
	}

	public int maxId() {
		int len = login.length();
		return (int)Math.pow(2, len-1);
	}
	
	@Override
	public boolean hasNext() {
		return id<maxId();
	}

	@Override
	public String next() {
		String bin = Integer.toBinaryString(id++);
		while (bin.length()<login.length()-1) bin = "0"+bin;
		StringBuilder result = new StringBuilder();
		for (int i=0;i<login.length()-1;i++) result.append(login.charAt(i)+((bin.charAt(i)=='0')?"":"."));
		result.append(login.charAt(login.length()-1));
		return result.toString();
	}
}

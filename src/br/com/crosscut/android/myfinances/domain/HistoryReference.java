package br.com.crosscut.android.myfinances.domain;

/**
 * This is a reference for standard history search
 * 
 * @author fabian.martins@gmail.com
 * @since  2010/12
 */
public enum HistoryReference implements Comparable<HistoryReference> {
	
	TODAY(0),
	LAST_7_DAYS(7),
	LAST_15_DAYS(15),
	LAST_30_DAYS(30),
	LAST_45_DAYS(45);
	
	private int daySpan;
	private String description;
	
	HistoryReference(int daySpan) {
		this.daySpan = daySpan;
	}
	
	public int getDaySpan() {
		return daySpan;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}

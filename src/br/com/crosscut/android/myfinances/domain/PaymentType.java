package br.com.crosscut.android.myfinances.domain;

/**
 * 
 * Payment Types.
 * 
 * @author fabian.martins@gmail.com
 * @since  2010/12
 * 
 */
public enum PaymentType {
	
	CASH(0),
	CREDIT_CARD(1);
	
	private int value;
	private String description;
	
	PaymentType(int value) {
		this.value = value;
	}
	
	int getValue() {
		return value;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}

}

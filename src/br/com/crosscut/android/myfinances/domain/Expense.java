package br.com.crosscut.android.myfinances.domain;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents an expense
 * 
 * @author fabian.martins@gmail.com
 * @since  2010/12
 *
 */
public class Expense implements Comparable<Expense> {

	/**
	 * Expense id.
	 */
	private Long id;
	/**
	 * Expense date.
	 */
	private Date date;
	/**
	 * Expense category
	 */
	private Category category;
	/**
	 * Payment type.
	 */
	private PaymentType paymentType;
	/**
	 * Details about the expense, provided by user.
	 */
	private String details;
	/**
	 * Expense amount.
	 */
	private Double value;
	
	public Expense() {
		this.id = Calendar.getInstance().getTime().getTime(); 
	}
	
	public Expense(Date date,Category category, Double value) {
		this();
		this.date = date;
		this.value = value;
		this.category = category;
		this.paymentType = PaymentType.CASH;
	}
	
	public Expense(Date date, Category category, PaymentType paymentType, Double value) {
		this(date,category,value);
		this.paymentType = paymentType;
	}	
	
	public Expense(Date date, Category category, PaymentType paymentType, String details, Double value) {
		this(date,category,value);
		this.paymentType = paymentType;
		this.details = details;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expense other = (Expense) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(Expense another) {
		int result = 0;
		if (this.date.after(another.date)) result = 1;
		if (this.date.before(another.date)) result = -1;
		if (this.date.equals(another.date)) result = 0;
		return result;
	}
	

}

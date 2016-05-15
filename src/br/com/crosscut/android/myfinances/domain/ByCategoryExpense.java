package br.com.crosscut.android.myfinances.domain;

import java.util.Comparator;


/**
 * This class describes a helper object that represents expenses grouped by category.
 * Objects are used in the main screen 
 * 
 * @author fabian.martins@gmail.com
 * @since  2010/12
 */
public class ByCategoryExpense {
	
	private Category category;
	private Double expenseValue;

	public static final Comparator<ByCategoryExpense> comparator = new Comparator<ByCategoryExpense>() {

		public int compare(ByCategoryExpense bce1, ByCategoryExpense bce2) {
			Comparator<Category> comparator = Category.comparator;
			Category cat1, cat2;
			cat1 = (bce1==null) ? null : bce1.getCategory();
			cat2 = (bce2==null) ? null : bce2.getCategory();
			return comparator.compare(cat1, cat2);
		}
		
	};
	
	public ByCategoryExpense(Category category, Double value) {
		this.category = category;
		this.expenseValue = value;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Double getExpenseValue() {
		return expenseValue;
	}
	public void setExpenseValue(Double expenseValue) {
		this.expenseValue = expenseValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
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
		ByCategoryExpense other = (ByCategoryExpense) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		return true;
	}
	
}

package br.com.crosscut.android.myfinances.domain;

import java.util.Comparator;

/**
 * Represents a Category
 * 
 * @author fabian.martins@gmail.com
 * @since  2010/12
 */
public class Category {
	
	// Is currently in use by some expense 
	private boolean isInUse;
	// Category name
	private String name;
	
	public Category(String name) {
		this.name = name;
		this.isInUse = false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Category other = (Category) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	public boolean isInUse() { 
		return this.isInUse; 
	}
	
	public void setInUse(boolean value) { 
		this.isInUse = value; 
	};
	
	public int compareTo(Category another) {
		int result = 0;
		if (another==null) result = 1;
		else result = this.name.compareTo(another.name);
		return result;
	}
	
	public static final Comparator<Category> comparator = new Comparator<Category>() {

		public int compare(Category category1, Category category2) {
			int result = 0;
			if (category1==null) result = -1;
			else if (category2==null) result = 1;
			else result = category1.compareTo(category2);
			return result;
		}

	};	
}

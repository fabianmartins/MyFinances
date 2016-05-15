package br.com.crosscut.android.myfinances.bo;

import java.util.Calendar;
import java.util.List;

import br.com.crosscut.android.myfinances.domain.ByCategoryExpense;
import br.com.crosscut.android.myfinances.domain.Category;
import br.com.crosscut.android.myfinances.domain.Expense;
import br.com.crosscut.android.myfinances.domain.PaymentType;

/**
 * Represents the facade for all functions consumed by activities
 * @author fm
 *
 */
public interface MyFinanceFacade {
	
	Double getTotalExpensesBetween(Calendar iniDate, Calendar endDate, PaymentType type);
	List<Expense> getExpensesBetween(Calendar iniDate, Calendar endDate);
	List<ByCategoryExpense> getTotalExpensesByCategory(Calendar initialDate, Calendar finalDate);
	boolean saveNewExpense(Calendar date, Category selectedCategory, PaymentType paymentType, String details, Double value);
	void deleteExpense(Expense expense);

	void setMainScreenUpdated();
	void setHistoryUpdated();
	void setHistoryCategoryUpdated();
	void setExpenseCategoryComboUpdated();

	boolean isEnabledMainScreenUpdate();
	boolean isEnabledHistoryUpdate();
	boolean isEnabledHistoryCategoryUpdate();
	boolean isExpenseCategoryComboUpdate();

	void enableMainScreenUpdate();
	void enableHistoryUpdate();
	void enableHistoryCategoryUpdate();
	void enableExpenseCategoryComboUpdate();
	
	List<Category> getAllCategory();
	boolean storeCategory(String newCategory);
	void store(Category category);
	void delete(Category category) throws MyFinanceException;
	boolean categoryInUse(Category category);
	void store(Expense expense);

	
	
	
}

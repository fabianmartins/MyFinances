package br.com.crosscut.android.myfinances.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import br.com.crosscut.android.myfinances.domain.ByCategoryExpense;
import br.com.crosscut.android.myfinances.domain.Category;
import br.com.crosscut.android.myfinances.domain.Expense;
import br.com.crosscut.android.myfinances.domain.PaymentType;
import br.com.crosscut.android.myfinances.util.MyFinancesUtil;
import br.com.crosscut.android.myfinances.util.Predicate;
import br.com.crosscut.android.myfinances.view.R;

public class MyFinanceFacadeImpl implements MyFinanceFacade {

	private Context context;
	
	private List<Expense> expenses = new ArrayList<Expense>();
	private List<Category> categories = new ArrayList<Category>();
	private boolean requireMainScreenUpdate = true;
	private boolean requireHistoryUpdate = true;
	private boolean requireHistoryCategoryUpdate = true;
	private boolean requireExpenseCategoryComboUpdate = false;

	public MyFinanceFacadeImpl(Context context) {
		
		this.context = context; 
		
		Category category;
		category = new Category("Alimentacao");
		category.setInUse(true);
		categories.add(category);
		expenses.add(new Expense(new Date(), category,
				PaymentType.CREDIT_CARD, "Almoço no Fazano",753.20));
		category = new Category("Combustivel");
		category.setInUse(true);
		categories.add(category);
		expenses.add(new Expense(new Date(), category,
				PaymentType.CREDIT_CARD, "Viagem para Uberlândia", 353.12));
		expenses.add(new Expense(new Date(), category,
				PaymentType.CASH, "Reabastecimento em Uberlândia logo no posto na saida para a rodovia de ituiutaba", 59.22));
		category = new Category("Padaria");
		category.setInUse(true);
		categories.add(category);
		expenses.add(new Expense(new Date(), category,
				PaymentType.CASH, "happy hour",62.15));
		category = new Category("Passeio");
		category.setInUse(true);
		categories.add(category);
		expenses.add(new Expense(new Date(), category,
				PaymentType.CASH, 1270.32));
		category = new Category("Buteco");
		category.setInUse(true);
		categories.add(category);
		expenses.add(new Expense(new Date(), category,
				PaymentType.CASH, 450.12));
		category = new Category("Presentes");
		category.setInUse(true);
		categories.add(category);
		expenses.add(new Expense(new Date(), category,
				PaymentType.CREDIT_CARD, 3450.12));
		category = new Category("Reforma");
		category.setInUse(true);
		categories.add(category);
		expenses.add(new Expense(new Date(), category,
				PaymentType.CREDIT_CARD, "Janelas", 2650.00));	
		expenses.add(new Expense(new Date(), category,
				PaymentType.CREDIT_CARD, "Torneira",125.30));	
		expenses.add(new Expense(new Date(), category,
				PaymentType.CREDIT_CARD, "Porta",712.45));
		expenses.add(new Expense(new Date(), category,
				PaymentType.CREDIT_CARD, "Tinta", 165.65));	
				
		}

	public List<Category> getAllCategory() {
		List<Category> result = new ArrayList<Category>(categories);
		Collections.sort(result,Category.comparator);
		return result;
	}

	public List<Expense> getExpensesBetween(Calendar iniDate, Calendar endDate) {
		ArrayList<Expense> result = new ArrayList<Expense>(expenses);
		Collections.sort(result);
		return result;
	}

	public Double getTotalExpensesBetween(Calendar iniDate, Calendar endDate,
			PaymentType type) {

		Double sum = 0d;
		Iterator<Expense> i = expenses.iterator();
		Expense expense;
		while (i.hasNext()) {
			expense = i.next();
			if (((type == null) && (expense.getPaymentType() == null))
					|| ((type != null) && type.equals(expense.getPaymentType())))
				sum = sum + expense.getValue();
		}
		return sum;
	}

	/**
	 * It will include "null" category for non categorized expenses.
	 */
	public List<ByCategoryExpense>  getTotalExpensesByCategory(
			Calendar iniDate, Calendar endDate) {
		ArrayList<ByCategoryExpense> result = new ArrayList<ByCategoryExpense>();
		ByCategoryExpense newItem, oldItem;
		Expense expense;
		
		Iterator<Expense> i = getExpensesBetween(iniDate, endDate).iterator();
		while (i.hasNext()) {
			expense = i.next();
			newItem = new ByCategoryExpense(expense.getCategory(),expense.getValue());
			if (result.contains(newItem)) {
				oldItem = result.get(result.indexOf(newItem));
				oldItem.setExpenseValue(oldItem.getExpenseValue()+newItem.getExpenseValue());
			}
			else result.add(newItem);
		}
		return result;
	}

	public boolean storeCategory(String name) {
		boolean result = false;
		Category newCategory = new Category(name);
		if (!categories.contains(newCategory)) {
			categories.add(newCategory);
			result = true;
		}
		return result;
	}

	public boolean saveNewExpense(Calendar date, Category selectedCategory,
			PaymentType paymentType, String details, Double value) {

		Expense e = new Expense(date.getTime(), selectedCategory, paymentType,
				details, value);
		if ((selectedCategory!=null)&& !selectedCategory.isInUse()) selectedCategory.setInUse(true);
		expenses.add(e);
		return true;
	}

	public void deleteExpense(Expense expense) {
		Category cat = expense.getCategory();
		expenses.remove(expense);
		cat.setInUse(categoryInUse(cat));
	}

	public boolean isEnabledHistoryUpdate() {
		return this.requireHistoryUpdate;
	}
	
	public void enableHistoryUpdate() {
		this.requireHistoryUpdate = true;
	}

	public boolean isEnabledMainScreenUpdate() {
		return this.requireMainScreenUpdate;
	}
	
	public void enableMainScreenUpdate() {
		this.requireMainScreenUpdate = true;
	}
	
	
	public void enableExpenseCategoryComboUpdate() {
		this.requireExpenseCategoryComboUpdate = true;
	}
	
	public boolean isExpenseCategoryComboUpdate() {
		return this.requireExpenseCategoryComboUpdate;
	}
	
	public void setExpenseCategoryComboUpdated() {
		this.requireExpenseCategoryComboUpdate = false;
	}

	public boolean isEnabledHistoryCategoryUpdate() {
		return this.requireHistoryCategoryUpdate;
	}
	
	public void enableHistoryCategoryUpdate() {
		this.requireHistoryCategoryUpdate = true;
	}

	public void setHistoryUpdated() {
		this.requireHistoryUpdate = false;
	}

	public void setMainScreenUpdated() {
		this.requireMainScreenUpdate = false;
	}

	public void setHistoryCategoryUpdated() {
		this.requireHistoryCategoryUpdate = false;
	}

	public void delete(Category category) throws MyFinanceException {
		if (!categoryInUse(category)) {
			categories.remove(category);
		}
		else throw new MyFinanceException(context.getString(R.string.exception_categoryInUse));
	}

	public boolean categoryInUse(Category category) {
		boolean result = true;
		final Category selectedCategory = category;
		List<Expense> expensesUsingCategory = 
		MyFinancesUtil.filter(expenses, new Predicate<Expense>(){

			public boolean apply(Expense expense) {
				return expense.getCategory().equals(selectedCategory);
			}});
		if (expensesUsingCategory.size()>0) result = true;
		else result = false;
		return result;
	}

	public void store(Category category) {
		if (!categories.contains(category)) categories.add(category);
	}

	public void store(Expense expense) {
		if (expenses.contains(expense)) expenses.remove(expense);
		expenses.add(expense);
	}
	
}

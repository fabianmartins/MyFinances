package br.com.crosscut.android.myfinances.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import br.com.crosscut.android.myfinances.bo.FacadeFactory;
import br.com.crosscut.android.myfinances.bo.MyFinanceFacade;
import br.com.crosscut.android.myfinances.domain.Category;
import br.com.crosscut.android.myfinances.domain.Expense;
import br.com.crosscut.android.myfinances.domain.HistoryReference;
import br.com.crosscut.android.myfinances.util.MyFinancesUtil;
import br.com.crosscut.android.myfinances.util.Predicate;
import br.com.crosscut.android.myfinances.view.R;

public class HistoryActivity extends Activity {

	/**
	 * Category to be used when this activity is called with a pre-selected
	 * category
	 */
	protected static Category selectedCallingCategory = null;

	// Menu
	static final private int VIEW_EDIT = Menu.FIRST;
	static final private int DELETE = Menu.FIRST + 1;

	protected EditText txtSelectedTotal;
	protected Spinner cmbSelectReference;
	protected Spinner cmbSelectCategory;
	protected TextView lblHistoryDetails;
	protected ListView listView;

	protected HistoryReference selectedReference = null;
	protected Object selectedCategory = null;

	protected MyFinanceFacade facade;

	protected List<Expense> gridData;

	protected Calendar initialDate;
	protected Calendar finalDate;

	protected String categoryNone;
	protected String categoryAll;

	protected Activity thisActivity;

	protected Expense selectedExpense;

	protected boolean gridNeedsRefresh = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.history_activity_title);
		setContentView(R.layout.history);
		init();
		getData();
		fillView();

	}

	protected void init() {
		facade = FacadeFactory.getMyFinanceFacade(this.getBaseContext());
		thisActivity = this;
		gridData = new ArrayList<Expense>();
		initialDate = Calendar.getInstance();
		finalDate = Calendar.getInstance();
		categoryAll = getBaseContext().getString(R.string.category_allCategory);
		categoryNone = getBaseContext().getString(R.string.category_noCategory);

		getComponentReferences();
		initComponents();
	}

	private void getComponentReferences() {
		txtSelectedTotal = (EditText) findViewById(R.id.history_txtSelectedTotal);
		cmbSelectReference = (Spinner) findViewById(R.id.history_cmbSelectReference);
		cmbSelectCategory = (Spinner) findViewById(R.id.history_cmbSelectCategory);
		lblHistoryDetails = (TextView) findViewById(R.id.history_lblHistoryDetails);
		listView = (ListView) findViewById(R.id.history_listView);
	}

	private void initComponents() {

		// Setting the Reference Spinner
		ArrayAdapter<HistoryReference> selectReferenceAdapter = new ArrayAdapter<HistoryReference>(
				this, android.R.layout.simple_spinner_item);
		selectReferenceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cmbSelectReference.setAdapter(selectReferenceAdapter);
		cmbSelectReference
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						selectedReference = (HistoryReference) parent
								.getItemAtPosition(pos);
						onSelectedReferenceChange(selectedReference);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// Do nothing
					}

				});
		populateReferenceSpinner();

		// Setting the category Spinner
		ArrayAdapter<Object> selectCategoryAdapter = new ArrayAdapter<Object>(
				this, android.R.layout.simple_spinner_item);
		selectCategoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cmbSelectCategory.setAdapter(selectCategoryAdapter);
		cmbSelectCategory
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						selectedCategory = parent.getItemAtPosition(pos);
						selectedCallingCategory = null;
						onSelectedCategoryChange(selectedCategory);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// Do nothing
					}
				});
		populateCategorySpinner();

		// Setting the ListView
		HistoryListViewAdapter listAdapter = new HistoryListViewAdapter(this,
				R.layout.historylistrow, this.gridData);
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedExpense = (Expense) listView
						.getItemAtPosition(position);
				Log.i("HISTORY", "selectedExpense=" + selectedExpense);

			}
		});

		registerForContextMenu(listView);

	}

	private void fillView() {
		populateReferenceSpinner();
		populateCategorySpinner();
		populateGrid(this.gridData);
	}

	private void getData() {
		if (facade.isEnabledHistoryCategoryUpdate()) {
			populateCategorySpinner();
			facade.setHistoryCategoryUpdated();
		}

		selectedCategory = cmbSelectCategory.getItemAtPosition(0);
		selectedReference = (HistoryReference) cmbSelectReference
				.getItemAtPosition(0);

		initialDate = Calendar.getInstance();
		finalDate = Calendar.getInstance();
		initialDate.add(Calendar.DAY_OF_MONTH, selectedReference.getDaySpan()
				* (-1));

		gridData = facade.getExpensesBetween(initialDate, finalDate);

	}

	@SuppressWarnings("unchecked")
	private void populateReferenceSpinner() {
		ArrayAdapter<HistoryReference> adapter = (ArrayAdapter<HistoryReference>) this.cmbSelectReference
				.getAdapter();
		adapter.clear();
		HistoryReference[] refs = HistoryReference.values();
		for (int i = 0; i < refs.length; i++)
			adapter.add(refs[i]);
	}

	@SuppressWarnings("unchecked")
	private void populateCategorySpinner() {
		ArrayAdapter<Object> adapter = (ArrayAdapter<Object>) this.cmbSelectCategory
				.getAdapter();
		adapter.clear();
		adapter.add(this.categoryAll);
		adapter.add(this.categoryNone);
		List<Category> categories = facade.getAllCategory();
		Iterator<Category> categoriesIterator = categories.iterator();
		while (categoriesIterator.hasNext())
			adapter.add(categoriesIterator.next());
		if (selectedCallingCategory != null)
			cmbSelectCategory.setSelection(adapter
					.getPosition(HistoryActivity.selectedCallingCategory));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemViewEdit = menu.add(0, VIEW_EDIT, Menu.NONE,
				R.string.history_menubutton_viewOrEditSelected);
		MenuItem itemDelete = menu.add(0, DELETE, Menu.NONE,
				R.string.history_menubutton_deleteSelected);

		// Assign icons
		/*
		 * itemAdd.setIcon(R.drawable.include);
		 * itemRem.setIcon(R.drawable.exclude);
		 * itemEdt.setIcon(R.drawable.edit); itemCan.setIcon(R.drawable.cancel);
		 */

		// Allocate shortcuts to each of them.
		itemViewEdit.setShortcut('1', 'v');
		itemDelete.setShortcut('2', 'D');

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case (VIEW_EDIT): {
			if (this.selectedExpense != null) {
				editSelectedExpense();
				return true;
			}
		}
		case (DELETE): {
			if (this.selectedExpense != null) {
				deleteSelectedExpense();
				return true;
			}
		}
		}
		return false;
	}

	private void editSelectedExpense() {
		gridNeedsRefresh = true;
		ExpenseActivity.setExpenseToEdit(this.selectedExpense);
		Intent intent = new Intent(this, ExpenseActivity.class);
		startActivity(intent);
	}

	private void deleteSelectedExpense() {
		AlertDialog.Builder ad = MyFinancesUtil.buildMessageBox(
				this,
				getResources().getString(
						R.string.history_dialog_deleteExpense_title),
				getResources().getString(
						R.string.history_dialog_deleteExpense_message)
						+ "\n\n"
						+ getResources().getString(
								R.string.history_dialog_deleteExpense_date)
						+ MyFinancesUtil.dateFormat(selectedExpense.getDate())
						+ "\n"
						+ getResources().getString(
								R.string.history_dialog_deleteExpense_category)
						+ selectedExpense.getCategory()
						+ "\n"
						+ getResources().getString(
								R.string.history_dialog_deleteExpense_amount)
						+ MyFinancesUtil.currencyFormat(selectedExpense
								.getValue())
						+ "\n"
						+ getResources().getString(
								R.string.history_dialog_deleteExpense_details)
						+ ((selectedExpense.getDetails() == null) ? ""
								: selectedExpense.getDetails()),

				// YesClickListener
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						facade.deleteExpense(selectedExpense);
						gridData.remove(selectedExpense);
						repopulateGrid();
					}

				},
				// NoClickListener
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
					}

				}, null);
		ad.show();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.history_contextmenu_title);
		menu.add(0, VIEW_EDIT, Menu.NONE,
				R.string.history_contextmenu_option_edit_view);
		menu.add(0, DELETE, Menu.FIRST,
				R.string.history_contextmenu_option_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		/*
		 * AdapterView.AdapterContextMenuInfo menuInfo; menuInfo =
		 * (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		 */
		super.onContextItemSelected(item);
		switch (item.getItemId()) {
		case (DELETE): {
			deleteSelectedExpense();
			return true;
		}
		case (VIEW_EDIT): {
			editSelectedExpense();
			return true;
		}
		}
		return false;
	}

	private void onSelectedReferenceChange(HistoryReference selectedReference) {
		repopulateGrid();
	}

	private void onSelectedCategoryChange(Object selectedCategory) {
		repopulateGrid();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && this.gridNeedsRefresh) {
			if (HistoryActivity.selectedCallingCategory != null) {
				@SuppressWarnings("unchecked")
				ArrayAdapter<Object> adapter = (ArrayAdapter<Object>) cmbSelectCategory
						.getAdapter();
				cmbSelectCategory.setSelection(adapter
						.getPosition(HistoryActivity.selectedCallingCategory));
			}
			repopulateGrid();
			gridNeedsRefresh = false;
		}
	}

	private void repopulateGrid() {
		final Calendar referenceIntialDate = Calendar.getInstance();
		final Calendar referenceFinalDate = Calendar.getInstance();
		referenceIntialDate.add(Calendar.DAY_OF_MONTH,
				(-1) * selectedReference.getDaySpan() - 1);
		referenceFinalDate.add(Calendar.DAY_OF_MONTH, 1);
		List<Expense> filtered = MyFinancesUtil.filter(this.gridData,
				new Predicate<Expense>() {

					public boolean apply(Expense expense) {
						return ((expense.getDate().after(referenceIntialDate
								.getTime()))
								&& (expense.getDate().before(referenceFinalDate
										.getTime())) && (
						// All categories must be included
						((selectedCategory instanceof String) && categoryAll
								.equals(selectedCategory))

						||

						// NONE selected; only expenses with no
						// categories must be included;
								((selectedCategory instanceof String)
										&& categoryNone
												.equals(selectedCategory) && (expense
										.getCategory() == null)) || (expense
								.getCategory() != null)
								&& expense.getCategory().equals(
										selectedCategory)));
					}
				});

		populateGrid(filtered);
	}

	protected void populateGrid(List<Expense> expenses) {

		HistoryListViewAdapter adapter = (HistoryListViewAdapter) listView
				.getAdapter();
		adapter.clear();
		this.listView.removeViews(0, listView.getChildCount());
		double total = 0d;

		Iterator<Expense> i = expenses.iterator();
		Expense history;
		while (i.hasNext()) {
			history = i.next();
			adapter.add(history);
			total = total + history.getValue();
		}
		txtSelectedTotal.setText(NumberFormat.getCurrencyInstance().format(
				total));
	}

	/**
	 * Used only when called from other activities
	 * 
	 * @param selectedCategory
	 */
	public static void setSelectedCallingCategory(Category selectedCategory) {
		selectedCallingCategory = selectedCategory;
	}

}

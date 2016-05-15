package br.com.crosscut.android.myfinances.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import br.com.crosscut.android.myfinances.bo.FacadeFactory;
import br.com.crosscut.android.myfinances.bo.MyFinanceFacade;
import br.com.crosscut.android.myfinances.domain.ByCategoryExpense;
import br.com.crosscut.android.myfinances.domain.PaymentType;
import br.com.crosscut.android.myfinances.util.MyFinancesUtil;

public class MyFinances extends Activity {

	// Menu
	static final private int ADD_EXPENSE = Menu.FIRST;
	static final private int HISTORY = Menu.FIRST + 1;
	static final private int SETTINGS = Menu.FIRST + 2;
	static final private int ABOUT = Menu.FIRST + 3;

	// Widgets
	private EditText txtTotalExpenses;
	private EditText txtInCash;
	private EditText txtCreditCard;
	private TextView lblInitialDate;
	private TextView lblFinalDate;
	private ListView lstExpensesByCategory;

	// Data fields
	private double totalExpenses = 0.0;
	private double totalCreditCard = 0.0;
	private double totalCash = 0.0;
	private Calendar initialDate;
	private Calendar finalDate;

	// For the grid
	private List<ByCategoryExpense> byCategoryExpenses;

	// Facade
	MyFinanceFacade facade;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle(R.string.main_activity_title);
		init();
		getComponentReferences();
		initComponents();
		fillView();
	}

	private void init() {
		MyFinancesUtil.init(this.getApplicationContext());
		facade = FacadeFactory.getMyFinanceFacade(this.getBaseContext());
	}

	/**
	 * Get the references to the components
	 */
	private void getComponentReferences() {
		this.txtTotalExpenses = (EditText) findViewById(R.id.txtTotalExpenses);
		this.txtInCash = (EditText) findViewById(R.id.txtInCash);
		this.txtCreditCard = (EditText) findViewById(R.id.txtCreditCard);
		this.lblInitialDate = (TextView) findViewById(R.id.lblInitialDate);
		this.lblFinalDate = (TextView) findViewById(R.id.lblFinalDate);
		this.lstExpensesByCategory = (ListView) findViewById(R.id.main_lstExpensesByCategory);
	}

	/**
	 * Set up the components
	 */
	private void initComponents() {

		byCategoryExpenses = new ArrayList<ByCategoryExpense>();
		ByCategoryExpenseAdapter adapter = new ByCategoryExpenseAdapter(this,
				R.layout.mainexpensesbycategory, byCategoryExpenses);
		this.lstExpensesByCategory.setAdapter(adapter);
		// When clicking on an expenses-by-category item, it calls a details view
		lstExpensesByCategory.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object selectedCategory = lstExpensesByCategory
						.getItemAtPosition(position);
				if (selectedCategory != null) {
					ByCategoryExpense bcExpense = (ByCategoryExpense) selectedCategory;
					HistoryActivity.setSelectedCallingCategory(bcExpense
							.getCategory());
					Intent intent = new Intent(getBaseContext(),
							HistoryActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	private void fillView() {
		if (facade.isEnabledMainScreenUpdate()) {
			getData();
			bindDataToView();
		}
		facade.setMainScreenUpdated();
	}

	private void getData() {
		initialDate = Calendar.getInstance();
		initialDate.add(Calendar.DAY_OF_MONTH, -7);
		finalDate = Calendar.getInstance();

		totalCash = facade.getTotalExpensesBetween(initialDate, finalDate,
				PaymentType.CASH);
		totalCreditCard = facade.getTotalExpensesBetween(initialDate,
				finalDate, PaymentType.CREDIT_CARD);
		totalExpenses = totalCash + totalCreditCard;
		byCategoryExpenses = facade.getTotalExpensesByCategory(initialDate,
				finalDate);
	}

	private void bindDataToView() {
		this.txtTotalExpenses.setText(MyFinancesUtil
				.currencyFormat(totalExpenses));
		this.txtInCash.setText(MyFinancesUtil.currencyFormat(this.totalCash));
		this.txtCreditCard.setText(MyFinancesUtil
				.currencyFormat(this.totalCreditCard));

		this.lblInitialDate.setText(MyFinancesUtil.dateFormat(initialDate
				.getTime()));
		this.lblFinalDate
				.setText(MyFinancesUtil.dateFormat(finalDate.getTime()));

		// Populating the table
		ByCategoryExpenseAdapter adapter = (ByCategoryExpenseAdapter) this.lstExpensesByCategory
				.getAdapter();
		adapter.clear();

		Collections.sort(byCategoryExpenses, ByCategoryExpense.comparator);
		Iterator<ByCategoryExpense> categoryExpenseIterator = byCategoryExpenses
				.iterator();
		while (categoryExpenseIterator.hasNext()) {
			adapter.add(categoryExpenseIterator.next());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemExpense = menu.add(0, ADD_EXPENSE, Menu.NONE,
				R.string.main_menuButton_expenses);
		MenuItem itemHistory = menu.add(0, HISTORY, Menu.NONE,
				R.string.main_menuButton_history);
		MenuItem itemSettings = menu.add(0, SETTINGS, Menu.NONE,
				R.string.main_menuButton_settings);
		MenuItem itemAbout = menu.add(0, ABOUT, Menu.NONE,
				R.string.main_menuButton_about);

		// Assign icons
		/*
		 * itemAdd.setIcon(R.drawable.include);
		 * itemRem.setIcon(R.drawable.exclude);
		 * itemEdt.setIcon(R.drawable.edit); itemCan.setIcon(R.drawable.cancel);
		 */

		// Allocate shortcuts to each of them.
		itemExpense.setShortcut('1', 'e');
		itemHistory.setShortcut('2', 'h');
		itemSettings.setShortcut('3', 's');
		itemAbout.setShortcut('0', 'a');

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case (ADD_EXPENSE): {
			Intent intent = new Intent(this, ExpenseActivity.class);
			startActivity(intent);
			return true;
		}

		case (HISTORY): {
			Intent intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
			return true;
		}

		case (SETTINGS): {
			return true;
		}

		case (ABOUT): {
			return true;
		}
		}
		return false;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			fillView();
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		fillView();
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onPause() {
		facade.enableMainScreenUpdate();
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
		fillView();
	}

	@Override
	protected void onStop() {
		facade.enableMainScreenUpdate();
		super.onStop();
	}

}
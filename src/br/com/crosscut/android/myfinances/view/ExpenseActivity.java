package br.com.crosscut.android.myfinances.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import br.com.crosscut.android.myfinances.bo.FacadeFactory;
import br.com.crosscut.android.myfinances.bo.MyFinanceFacade;
import br.com.crosscut.android.myfinances.domain.Category;
import br.com.crosscut.android.myfinances.domain.Expense;
import br.com.crosscut.android.myfinances.domain.PaymentType;
import br.com.crosscut.android.myfinances.view.R;

public class ExpenseActivity extends Activity {

	static final private int SAVE = Menu.FIRST;
	static final private int CANCEL = Menu.FIRST + 1;

	// Set up dialogs
	static final private int DATE_DIALOG_ID = 0;
	static final private int INVALID_VALUE_DIALOG_ID = 1;
	static final private int EXPENSE_STORED_DIALOG_ID = 2;

	static private Expense expenseToEdit;

	private EditText txtDate;
	private Button btnPickDate;
	private Button btnNewCategory;
	private Spinner cmbCategory;
	private RadioGroup rgpPaymentType;
	private EditText txtValue;
	private EditText txtDetails;

	private MyFinanceFacade facade;

	private List<Object> categoryList;
	private PaymentType selectedPaymentType;
	private Calendar selectedDate;
	private Calendar currentDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense);
		setTitle(R.string.expense_activity_title);
		init();
	}

	protected void init() {
		getComponentReferences();
		initComponents();
		resetViewState();
	}

	private void getComponentReferences() {
		txtDate = (EditText) findViewById(R.id.expense_txtDate);
		btnPickDate = (Button) findViewById(R.id.expense_btnPickDate);
		cmbCategory = (Spinner) findViewById(R.id.expense_cmbCategory);
		btnNewCategory = (Button) findViewById(R.id.expense_btnNewCategory);
		rgpPaymentType = (RadioGroup) findViewById(R.id.expense_rgpPaymentType);
		txtValue = (EditText) findViewById(R.id.expense_txtValue);
		txtDetails = (EditText) findViewById(R.id.expense_txtDetails);
	}

	private void initComponents() {

		facade = FacadeFactory.getMyFinanceFacade(this.getBaseContext());

		// Setting the date
		setCurrentDate();
		selectedDate = Calendar.getInstance();
		showCurrentDate();
		btnPickDate.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		btnNewCategory.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				callCategoryActivity();
			}

		});

		// Setting the category Spinner
		categoryList = new ArrayList<Object>();
		populateCategoryCombo();
		ArrayAdapter<Object> selectCategoryAdapter = new ArrayAdapter<Object>(
				this, android.R.layout.simple_spinner_item, categoryList);
		selectCategoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cmbCategory.setAdapter(selectCategoryAdapter);
		/*
		cmbCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				Object category = parent.getItemAtPosition(pos);
				if (category instanceof Category)
					selectedCategory = (Category) category;
				selectedCategory = null;
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		*/

		// Setting the radio group
		rgpPaymentType.check(R.id.expense_rbtMoney);
		rgpPaymentType
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						setSelectedPaymentType();
					}
				});
	}

	private void setSelectedPaymentType() {
		int id = this.rgpPaymentType.getCheckedRadioButtonId();
		if (id == R.id.expense_rbtCreditCard)
			selectedPaymentType = PaymentType.CREDIT_CARD;
		else
			selectedPaymentType = PaymentType.CASH;
	}

	private void populateCategoryCombo() {
		categoryList.clear();
		categoryList
				.add(getResources().getString(R.string.category_noCategory));
		categoryList.addAll(facade.getAllCategory());
		cmbCategory.setSelection(0);
	}

	private void callCategoryActivity() {
		Intent intent = new Intent(this, CategoryActivity.class);
		startActivity(intent);
	}

	private void setCurrentDate() {
		currentDate = Calendar.getInstance();
	}

	private void setSelectedDate(int year, int month, int dayOfMonth) {
		selectedDate = Calendar.getInstance();
		selectedDate.set(year, month, dayOfMonth);
		txtDate.setText(DateUtils.formatDateTime(this, selectedDate
				.getTimeInMillis(), DateUtils.FORMAT_NUMERIC_DATE));
	}

	private void showCurrentDate() {
		txtDate.setText(DateUtils.formatDateTime(this, new Date().getTime(),
				DateUtils.FORMAT_NUMERIC_DATE));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem itemSave = menu.add(0, SAVE, Menu.NONE,
				R.string.expense_menuButton_save);
		MenuItem itemCancel = menu.add(0, CANCEL, Menu.NONE,
				R.string.expense_menuButton_cancel);

		itemSave.setShortcut('1', 's');
		itemCancel.setShortcut('2', 'c');

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case (SAVE): {
				doSave();
				return true;
			}
			case (CANCEL): {
				doCancel();
				return true;
			}
		}
		return false;
	}

	private void doSave() {
		Expense toBeSaved;
		if (ExpenseActivity.expenseToEdit!=null) {
			toBeSaved = ExpenseActivity.expenseToEdit;
		}
		else toBeSaved = new Expense();
		toBeSaved.setDate(selectedDate.getTime());
		toBeSaved.setCategory((Category) this.cmbCategory.getSelectedItem());
		toBeSaved.setDetails(this.txtDetails.getText().toString());
		toBeSaved.setValue(Double.parseDouble(this.txtValue.getText().toString()));
		setSelectedPaymentType();
		toBeSaved.setPaymentType(selectedPaymentType);
		try {
			facade.store(toBeSaved);
			showDialog(EXPENSE_STORED_DIALOG_ID);
			ExpenseActivity.setExpenseToEdit(null);
			resetViewState();
		} catch (NumberFormatException nfe) {

		}
	}

	private void doCancel() {
		resetViewState();
	}
	
	private void resetViewState() {
		Expense expense = ExpenseActivity.expenseToEdit;
		if (expense != null) {
			this.txtValue.setText(expense.getValue().toString());
			txtDate.setText(DateUtils.formatDateTime(this, expense.getDate().getTime()
					, DateUtils.FORMAT_NUMERIC_DATE));
			selectedDate = Calendar.getInstance();
			selectedDate.setTime(expense.getDate());
			int cmbCategoryPosition = 0;
			if (expense.getCategory()!=null) cmbCategoryPosition = categoryList.indexOf(expense.getCategory());  
			cmbCategory.setSelection(cmbCategoryPosition);
			rgpPaymentType.check(expense.getPaymentType().equals(PaymentType.CASH) ? R.id.expense_rbtMoney : R.id.expense_rbtCreditCard);
			txtDetails.setText(expense.getDetails());
		} else {
			this.txtValue.setText("");
			this.txtValue.requestFocus();
			this.cmbCategory.setSelection(0);
			this.rgpPaymentType.check(R.id.expense_rbtMoney);
			this.txtDetails.setText("");
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			setSelectedDate(year, monthOfYear, dayOfMonth);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder;

		switch (id) {

		case DATE_DIALOG_ID:
			if (currentDate == null)
				setCurrentDate();
			int year = currentDate.get(Calendar.YEAR);
			int month = currentDate.get(Calendar.MONTH);
			int day = currentDate.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(this, mDateSetListener, year, month,
					day);

		case INVALID_VALUE_DIALOG_ID:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.expense_message_invalidValue)
					.setCancelable(false).setPositiveButton(R.string.Ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dismissDialog(INVALID_VALUE_DIALOG_ID);
								}
							});
			return builder.create();

		case EXPENSE_STORED_DIALOG_ID:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.expense_message_expenseStored)
					.setCancelable(false).setPositiveButton(R.string.Ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dismissDialog(EXPENSE_STORED_DIALOG_ID);
								}
							});
			return builder.create();
		}
		return null;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && facade.isExpenseCategoryComboUpdate()) {
			populateCategoryCombo();
			facade.setExpenseCategoryComboUpdated();
		}
		resetViewState();
	}

	public static void setExpenseToEdit(Expense expense) {
		ExpenseActivity.expenseToEdit = expense;
	}
}

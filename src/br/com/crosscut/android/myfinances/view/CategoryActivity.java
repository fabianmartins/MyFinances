package br.com.crosscut.android.myfinances.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import br.com.crosscut.android.myfinances.bo.FacadeFactory;
import br.com.crosscut.android.myfinances.bo.MyFinanceException;
import br.com.crosscut.android.myfinances.bo.MyFinanceFacade;
import br.com.crosscut.android.myfinances.domain.Category;

/**
 * Implements the UI for handling categories
 * 
 * @author fabian.martins@gmail.com
 * @since  2010/12
 */
public class CategoryActivity extends Activity {

	static final private int ADD = Menu.FIRST;
	static final private int EDIT = Menu.FIRST + 1;
	static final private int REMOVE = Menu.FIRST + 2;
	static final private int CANCEL = Menu.FIRST + 3;

	private ListView lstCategory;
	private EditText txtCategory;
	private List<Category> categories;
	private CategoryAdapter arrayAdapter;
	private Comparator<Category> categoryComparator;

	private boolean addingNew = false;
	private boolean editing = false;
	private int selectedIndex = -1;

	private MyFinanceFacade facade;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		this.setTitle(R.string.category_activity_title);
		
		categoryComparator = Category.comparator; 
			
		facade = FacadeFactory.getMyFinanceFacade(this.getBaseContext());

		lstCategory = (ListView) findViewById(R.id.category_lstCategory);
		txtCategory = (EditText) findViewById(R.id.category_txtCategory);

		categories = new ArrayList<Category>();
		arrayAdapter = new CategoryAdapter(this, R.layout.categoryrow,
				categories);
		lstCategory.setAdapter(arrayAdapter);

		txtCategory.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View view, int key, KeyEvent event) {
				Category category;

				if (event.getAction() == KeyEvent.ACTION_DOWN)
					if (((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) || (event
							.getKeyCode() == KeyEvent.KEYCODE_ENTER))
							&& !"".equals(txtCategory.getText().toString()
									.trim())) {
						if (editing) {
							category = categories.get(selectedIndex);
							category.setName(txtCategory.getText().toString()
									.trim());
							facade.store(category);
							facade.enableExpenseCategoryComboUpdate();
							arrayAdapter.notifyDataSetChanged();
							txtCategory.setText("");
							cancelEdit();
							return true;
						} else {
							category = new Category(txtCategory.getText()
									.toString().trim());
							facade.store(category);
							facade.enableExpenseCategoryComboUpdate();
							arrayAdapter.add(category);
							arrayAdapter.sort(categoryComparator);
							arrayAdapter.notifyDataSetChanged();
							txtCategory.setText("");
							cancelAdd();
							return true;
						}
					}
				return false;
			}
		});

		registerForContextMenu(lstCategory);
		updateArray();
		restoreUIState();
	}

	private void updateArray() {
		categories.clear();
		categories.addAll(facade.getAllCategory());
		arrayAdapter.sort(categoryComparator);
		arrayAdapter.notifyDataSetChanged();
	}

	private void restoreUIState() {
		this.txtCategory.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemAdd = menu.add(0, ADD, Menu.NONE,
				R.string.category_menu_add);
		MenuItem itemRem = menu.add(0, REMOVE, Menu.NONE,
				R.string.category_menu_remove);
		MenuItem itemEdt = menu.add(0, EDIT, Menu.NONE,
				R.string.category_menu_edit);
		MenuItem itemCan = menu.add(0, CANCEL, Menu.NONE,
				R.string.category_menu_cancel);

		// Assign icons
		/*
		 * itemAdd.setIcon(R.drawable.include);
		 * itemRem.setIcon(R.drawable.exclude);
		 * itemEdt.setIcon(R.drawable.edit); itemCan.setIcon(R.drawable.cancel);
		 */
		// Allocate shortcuts to each of them.
		itemAdd.setShortcut('1', 'a');
		itemRem.setShortcut('2', 'r');
		itemEdt.setShortcut('3', 'e');
		itemCan.setShortcut('3', '0');

		itemCan.setVisible(false);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.category_menutitle);
		menu.add(0, EDIT, Menu.FIRST, R.string.category_menu_edit);
		menu.add(0, REMOVE, Menu.NONE, R.string.category_menu_remove);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		selectedIndex = lstCategory.getSelectedItemPosition();
		if (addingNew) {
			// se estiver adicionando um novo item
			menu.findItem(REMOVE).setVisible(false);
			menu.findItem(EDIT).setVisible(false);
			menu.findItem(CANCEL).setVisible(true);
		} else if (editing) {
			// se estiver editando um item
			menu.findItem(REMOVE).setVisible(false);
			menu.findItem(EDIT).setVisible(false);
			menu.findItem(CANCEL).setVisible(true);
		} else {
			if (selectedIndex > -1) {
				// existe algo selecionado
				menu.findItem(REMOVE).setVisible(true);
				menu.findItem(EDIT).setVisible(true);
				menu.findItem(CANCEL).setVisible(false);
			} else {
				// n�o existe nada selecionado
				menu.findItem(REMOVE).setVisible(false);
				menu.findItem(EDIT).setVisible(false);
				menu.findItem(CANCEL).setVisible(false);
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		int index = lstCategory.getSelectedItemPosition();
		switch (item.getItemId()) {
		case (CANCEL): {
			if (addingNew) {
				cancelAdd();
				lstCategory.requestFocus();
				return true;
			} else if (editing) {
				cancelEdit();
				lstCategory.requestFocus();
				return true;
			}
		}
		case (ADD): {
			addNewCategory();
			return true;
		}
		case (EDIT): {
			editCategory(index);
			return true;
		}
		case (REMOVE): {
			removeCategory(index);
			lstCategory.requestFocus();
			return true;
		}
		}
		return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int index = menuInfo.position;
		super.onContextItemSelected(item);
		switch (item.getItemId()) {
		case (REMOVE): {
			removeCategory(index);
			return true;
		}
		case (EDIT): {
			editCategory(index);
			return true;
		}
		}
		return false;
	}

	private void cancelAdd() {
		addingNew = false;
		txtCategory.setVisibility(View.GONE);
		lstCategory.requestFocus();
	}

	private void addNewCategory() {
		addingNew = true;
		txtCategory.setVisibility(View.VISIBLE);
		txtCategory.requestFocus();
	}

	private void removeCategory(int index) {
		Category category = categories.get(index);
		try {
			facade.delete(category);
			facade.enableExpenseCategoryComboUpdate();
			arrayAdapter.remove(category);
			arrayAdapter.sort(categoryComparator);
			arrayAdapter.notifyDataSetChanged();
		} catch (MyFinanceException e) {

		}
		lstCategory.requestFocus();
	}

	private void editCategory(int index) {
		editing = true;
		selectedIndex = index;
		txtCategory.setText(categories.get(index).getName());
		txtCategory.setVisibility(View.VISIBLE);
		txtCategory.requestFocus();
	}

	private void cancelEdit() {
		editing = false;
		txtCategory.setVisibility(View.GONE);
		lstCategory.requestFocus();
	}
}
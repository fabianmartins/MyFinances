package br.com.crosscut.android.myfinances.view;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.crosscut.android.myfinances.domain.ByCategoryExpense;
import br.com.crosscut.android.myfinances.domain.Category;

/**
 * Used by the main screen to handle the list view for the (Category,Expense) set
 * @author fabian.martins@gmail.com
 * @since  2010/12
 *
 */
public class ByCategoryExpenseAdapter extends ArrayAdapter<ByCategoryExpense> {
	
	int resource;

	public ByCategoryExpenseAdapter(Context _context, int _resource,
			List<ByCategoryExpense> _items) {
		super(_context, _resource, _items);
		this.resource = _resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout row;
		ByCategoryExpense item = getItem(position);
		if (convertView == null) {
			row = new RelativeLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(resource, row, true);
		} else {
			row = (RelativeLayout) convertView;
		}
		Category currentCategory = item.getCategory();
		TextView lblCategoryName = (TextView) row.findViewById(R.id.main_lblCategoryName);
		if (currentCategory==null) 
				lblCategoryName.setText(getContext().getString(R.string.category_noCategory));
		else 	lblCategoryName.setText(currentCategory.getName());
		TextView lblCategoryValue = (TextView) row.findViewById(R.id.main_lblCategoryValue);
		lblCategoryValue.setText(NumberFormat.getCurrencyInstance().format(item.getExpenseValue()));
		return row;		
	}
}

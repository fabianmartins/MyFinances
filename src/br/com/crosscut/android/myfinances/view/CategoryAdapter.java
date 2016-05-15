package br.com.crosscut.android.myfinances.view;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.crosscut.android.myfinances.domain.Category;

/**
 * Used by category activity to populate the list view
 * @author fabianmartins
 *
 */
public class CategoryAdapter extends ArrayAdapter<Category> {
	int resource;

	public CategoryAdapter(Context _context, int _resource,
			List<Category> _items) {
		super(_context, _resource, _items);
		this.resource = _resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout categoryView;
		Category item = getItem(position);
		if (convertView == null) {
			categoryView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(resource, categoryView, true);
		} else {
			categoryView = (LinearLayout) convertView;
		}
		TextView lblCategoryName = (TextView) categoryView.findViewById(R.id.category_row_lblCategoryName);
		lblCategoryName.setText(item.getName());
		CheckBox chkCategoryInUse = (CheckBox) categoryView.findViewById(R.id.category_row_chkCategoryInUse);
		chkCategoryInUse.setChecked(item.isInUse());
		return categoryView;
	}
}
package br.com.crosscut.android.myfinances.view;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.crosscut.android.myfinances.domain.Category;
import br.com.crosscut.android.myfinances.domain.Expense;

/**
 * Implements the list view adapter for the history listview
 * 
 * @author fabianmartins
 *
 */
public class HistoryListViewAdapter extends ArrayAdapter<Expense> {
	
	private int resource;

	public HistoryListViewAdapter(Context context, int resource) {
		super(context, resource);
		this.resource = resource;
	}
	
	public HistoryListViewAdapter(HistoryActivity historyActivity,
			int resource, List<Expense> gridData) {
		super(historyActivity,resource,gridData);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout row;
		Expense expense = getItem(position);
		if (convertView == null) {
			row = new RelativeLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(resource, row, true);
		} else {
			row = (RelativeLayout) convertView;
		}
		TextView lblDate = (TextView) row.findViewById(R.id.history_listview_lblDate);
		TextView lblValue = (TextView) row.findViewById(R.id.history_listview_lblValue);
		TextView lblPaymentType = (TextView) row.findViewById(R.id.history_listview_lblPaymentType);
		TextView lblCategory = (TextView) row.findViewById(R.id.history_listview_lblCategory);
		TextView lblDescription = (TextView) row.findViewById(R.id.history_listview_lblDescription);
		
		//Setting the date
		java.text.DateFormat dateFormat = DateFormat.getDateFormat(getContext());
		lblDate.setText(dateFormat.format(expense.getDate()));
		
		// Setting the value
		lblValue.setText(NumberFormat.getCurrencyInstance().format(expense.getValue()));
		
		// Setting the payment type
		lblPaymentType.setText("("+expense.getPaymentType().toString()+")");
		
		// Setting the category
		Category currentCategory = expense.getCategory();
		if (currentCategory==null) 
				lblCategory.setText(getContext().getString(R.string.category_noCategory));
		else 	lblCategory.setText(currentCategory.getName());
		
		// Setting the description
		if (((expense.getDetails()!=null) && (!"".equals(expense.getDetails().trim())))) {
			lblDescription.setText(expense.getDetails());
			lblDescription.setVisibility(View.VISIBLE);
		}
		return row;		
	}
}

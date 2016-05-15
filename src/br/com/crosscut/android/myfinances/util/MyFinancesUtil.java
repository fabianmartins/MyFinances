package br.com.crosscut.android.myfinances.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.text.format.DateFormat;
import br.com.crosscut.android.myfinances.domain.HistoryReference;
import br.com.crosscut.android.myfinances.domain.PaymentType;
import br.com.crosscut.android.myfinances.view.R;

/**
 * 
 * @author fabian.martins@gmail.com
 * @since  2010/12
 * 
 */
public class MyFinancesUtil {
	
	private static NumberFormat currencyFormat;
	private static java.text.DateFormat dateFormat;

	public static void init(Context context) {

		PaymentType.CASH.setDescription(context.getResources().getString(
				R.string.paymentType_cash));
		PaymentType.CREDIT_CARD.setDescription(context.getResources()
				.getString(R.string.paymentType_creditCard));

		HistoryReference.TODAY.setDescription(context.getResources().getString(
				R.string.history_today));
		HistoryReference.LAST_7_DAYS.setDescription(context.getResources()
				.getString(R.string.history_last7Days));
		HistoryReference.LAST_15_DAYS.setDescription(context.getResources()
				.getString(R.string.history_last15Days));
		HistoryReference.LAST_30_DAYS.setDescription(context.getResources()
				.getString(R.string.history_last30Days));
		HistoryReference.LAST_45_DAYS.setDescription(context.getResources()
				.getString(R.string.history_last45Days));
		
		dateFormat = DateFormat.getDateFormat(context);
		currencyFormat = NumberFormat.getCurrencyInstance();
		
		
	}

	public static <T> List<T> filter(List<T> target, Predicate<T> predicate) {
		List<T> result = new ArrayList<T>();
		for (T element : target) {
			if (predicate.apply(element)) {
				result.add(element);
			}
		}
		return result;
	}

	public static AlertDialog.Builder buildMessageBox(Context context, String title,
			String message, OnClickListener yesListener,
			OnClickListener noListener, OnCancelListener cancelListener) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(message);
		if (yesListener != null)
			ad.setPositiveButton(context.getResources().getString(R.string.yes),
					yesListener);
		if (noListener != null)
			ad.setNegativeButton(context.getResources().getString(R.string.no),
					noListener);
		if (cancelListener != null) {
			ad.setCancelable(true);
			ad.setOnCancelListener(cancelListener);
		}
		return ad;
	}

	public static String dateFormat(Date date) {
		return dateFormat.format(date);
	}
	
	public static String currencyFormat(Double value) {
		return currencyFormat.format(value);
	}
}

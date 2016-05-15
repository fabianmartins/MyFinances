package br.com.crosscut.android.myfinances.bo;

import android.content.Context;

public class FacadeFactory {
	
	private static MyFinanceFacade _facadeInstance;
	
	public static MyFinanceFacade getMyFinanceFacade(Context context) {
		if (_facadeInstance==null) _facadeInstance = new MyFinanceFacadeImpl(context);
		return _facadeInstance;
	}

}

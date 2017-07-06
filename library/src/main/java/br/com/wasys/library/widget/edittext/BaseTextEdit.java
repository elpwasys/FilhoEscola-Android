package br.com.wasys.library.widget.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class BaseTextEdit extends EditText {

	private String _validationMessage = "";
	private String _validationName = "Campo";
	private int _maxLenght = 0;
	private boolean _isUpdating = false;

	public int getMaxLenght() {
		return _maxLenght;
	}

	public void setMaxLenght(int v) {
		_maxLenght = v;
	}

	public BaseTextEdit(Context ctx) {
		super(ctx);
		initialize();
	}

	public BaseTextEdit(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		initialize();
	}

	public BaseTextEdit(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
		initialize();
	}

	private void initialize() {
		this.addTextChangedListener(_textWatcher);
	}

	public String getValidationName() {
		return _validationName;
	}

	public void setValidationName(String value) {
		_validationName = value;
	}

	private TextWatcher _textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			try {
				if ((_maxLenght > 0) && (s.toString().length() >= _maxLenght)) {
					if (_isUpdating) {
						_isUpdating = false;
						return;
					}

					_isUpdating = true;
					setText(s.toString().substring(0, _maxLenght));
					setSelection(_maxLenght);

					View v = focusSearch(FOCUS_DOWN);
					if (v == null) {
						InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getWindowToken(), 0);
					}
				}
			} catch (Exception ex) {
				if (ex.getMessage() == null)
					return;
				Log.e(getClass().getName(), ex.getMessage());
			}

		}
	};
}

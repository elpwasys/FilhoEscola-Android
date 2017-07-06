package br.com.wasys.library.widget.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import br.com.wasys.library.utils.FieldUtils;

public class MaskedTextEdit extends BaseTextEdit {

	private String _inputMask;
	private boolean _isUpdating = false;
	private boolean _focusNext = false;

	public String getInputMask() {
		return this._inputMask;
	}

	public void setInputMask(String value) {
		this._inputMask = value;
	}

	public MaskedTextEdit(Context ctx) {
		super(ctx);

		initialize();
	}

	public MaskedTextEdit(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

		initialize();
	}

	public MaskedTextEdit(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);

		initialize();

	}

	private void initialize() {
		this.addTextChangedListener(_textWatcher);
	}

	public void clearMask(){
		this.removeTextChangedListener(_textWatcher);
		this._inputMask = null;
		this.setText("");
		this.addTextChangedListener(_textWatcher);
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

				if (_inputMask == null || _inputMask == "")
					return;

				if (_isUpdating) {
					_isUpdating = false;
					return;
				}

				String stringToFormat = FieldUtils.removerEspeciaisEspaco(s.toString());
				int tamString = stringToFormat.length();
				int mc = 0;
				int i = 0;
				String result = "";

				while (i < tamString) {
					if (mc >= _inputMask.length())
						break;

					if (_inputMask.charAt(mc) == '9') {
						if (!FieldUtils.isNaN(stringToFormat.charAt(i))) {
							result += stringToFormat.charAt(i);
							mc++;
							i++;
						} else
							i++;
					} else if (_inputMask.charAt(mc) == '#') {
						result += stringToFormat.charAt(i);
						mc++;
						i++;
					} else {
						result += _inputMask.charAt(mc);
						mc++;
					}
				}

				int selection = mc;
				_focusNext = result.length() == _inputMask.length();

				while (mc < _inputMask.length()) {
					if (_inputMask.charAt(mc) == '#' || _inputMask.charAt(mc) == '9') {
						result += "_";
					} else
						result += _inputMask.charAt(mc);

					mc++;
				}

				_isUpdating = true;
				setText(result);
				if (stringToFormat.length() == 0)
					setSelection(0, result.length());
				else
					setSelection(selection);

				/* && getOnFocusChangeListener() == null */
				if (_focusNext) {
					View v = focusSearch(FOCUS_DOWN);
					v.requestFocus();

					_focusNext = false;
				}

			} catch (Exception ex) {
				if (ex.getMessage() == null)
					return;
				Log.e(getClass().getName(), ex.getMessage());
			}

		}
	};

}

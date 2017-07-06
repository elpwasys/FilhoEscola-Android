package br.com.wasys.library.widget.edittext;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTextEdit extends MaskedTextEdit {

	public DateTextEdit(Context ctx) {
		super(ctx);

		initialize();
	}

	public DateTextEdit(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

		initialize();
	}

	public DateTextEdit(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);

		initialize();
	}

	private void initialize() {
		this.setInputMask("99/99/9999");
		this.setInputType(InputType.TYPE_CLASS_PHONE);
	}

	public Date getDate() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.setText(this.getText().toString().replace("/00","/19"));
            dateFormat.setLenient(false);
			return dateFormat.parse(this.getText().toString());
		} catch (Exception e) {
			return null;
		}
	}
}

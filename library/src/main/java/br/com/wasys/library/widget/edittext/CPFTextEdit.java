package br.com.wasys.library.widget.edittext;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

public class CPFTextEdit extends MaskedTextEdit {

	public CPFTextEdit(Context ctx) {
		super(ctx);

		initialize();
	}

	public CPFTextEdit(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

		initialize();
	}

	public CPFTextEdit(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);

		initialize();
	}

    private void initialize() {
		this.setHint("CPF");
		this.setInputMask("999.999.999-99");
		this.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	public String getCPF() {
		return this.getText().toString();
	}

}

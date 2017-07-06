package br.com.wasys.library.widget.edittext;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

public class CNPJTextEdit extends MaskedTextEdit {

	public CNPJTextEdit(Context ctx) {
		super(ctx);

		initialize();
	}

	public CNPJTextEdit(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

		initialize();
	}

	public CNPJTextEdit(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);

		initialize();
	}

    private void initialize() {
		this.setHint("CNPJ");
		this.setInputMask("99.999.999/9999-99");
		this.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	public String getCPF() {
		return this.getText().toString();
	}

}

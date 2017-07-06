package br.com.wasys.library.widget.edittext;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import br.com.wasys.library.utils.FieldUtils;

public class DDDPhoneTextEdit extends MaskedTextEdit {
	public DDDPhoneTextEdit(Context ctx) {
		super(ctx);

		initialize();
	}

	public DDDPhoneTextEdit(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

		initialize();
	}

	public DDDPhoneTextEdit(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);

		initialize();
	}

	private void initialize() {
		this.setInputMask("(99) 99999-9999");
		this.setInputType(InputType.TYPE_CLASS_PHONE);
	}


	public String getPhoneNumber() {
		return FieldUtils.removerEspeciaisEspaco(this.getText().toString());
	}

}

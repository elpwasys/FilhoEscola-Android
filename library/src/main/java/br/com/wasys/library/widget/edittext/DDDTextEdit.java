package br.com.wasys.library.widget.edittext;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import br.com.wasys.library.utils.FieldUtils;

public class DDDTextEdit extends MaskedTextEdit {
	public DDDTextEdit(Context ctx) {
		super(ctx);

		initialize();
	}

	public DDDTextEdit(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

		initialize();
	}

	public DDDTextEdit(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);

		initialize();
	}

	private void initialize() {
		this.setInputMask("999");
		this.setInputType(InputType.TYPE_CLASS_PHONE);
	}


	public String getPhoneNumber() {
		return FieldUtils.removerEspeciaisEspaco(this.getText().toString());
	}

}

package org.wahid.foody.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.wahid.foody.R;

import java.util.Objects;

public abstract class ShowDialog {

    public static void show(
            Context context,
            int iconRes,
            String titleText,
            String messageText,
            int buttonColor,
            String buttonText
    ) {
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("Dialog requires Activity context");
        }

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_status);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.ivStatus)).setImageResource(iconRes);
        ((TextView) dialog.findViewById(R.id.tvTitle)).setText(titleText);
        ((TextView) dialog.findViewById(R.id.tvMessage)).setText(messageText);

        Button btn = dialog.findViewById(R.id.btnAction);
        btn.setText(buttonText);
        btn.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        btn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}

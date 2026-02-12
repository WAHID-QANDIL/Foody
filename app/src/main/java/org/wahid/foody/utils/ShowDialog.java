package org.wahid.foody.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.wahid.foody.R;

import java.util.Objects;

public abstract class ShowDialog {

    public static void show(
            final Context context,
            final int iconRes,
            final String titleText,
            final String messageText,
            final int buttonColor,
            final String buttonText,
            final Runnable onOk
    ) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // If context is Activity, ensure it's not finishing
                if (context instanceof Activity) {
                    Activity act = (Activity) context;
                    if (act.isFinishing()) return;
                }

                try {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_status);
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ((ImageView) dialog.findViewById(R.id.ivStatus)).setImageResource(iconRes);
                    ((TextView) dialog.findViewById(R.id.tvTitle)).setText(titleText);
                    ((TextView) dialog.findViewById(R.id.tvMessage)).setText(messageText);

                    Button btn = dialog.findViewById(R.id.btnAction);
                    btn.setText(buttonText);
                    btn.setOnClickListener(v -> {
                        dialog.dismiss();
                        onOk.run();
                    });
                    dialog.show();
                } catch (RuntimeException ex) {
                    // Avoid crashing the app when UI cannot be created from this context/thread
                    Log.w("ShowDialog", "Failed to show dialog on UI thread", ex);
                }
            }
        });
    }
}

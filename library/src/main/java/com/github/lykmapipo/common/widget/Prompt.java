package com.github.lykmapipo.common.widget;


import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import com.github.lykmapipo.common.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * A {@link androidx.appcompat.app.AlertDialog} wrapper to present action prompts.
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class Prompt {

    // no instances allowed
    private Prompt() {
        throw new AssertionError("No instances.");
    }

    /**
     * Prompt for an action
     *
     * @param context      valid launch context
     * @param titleResId   prompt title
     * @param messageResId prompt message
     * @param listener     callback to invoke on accept or cancel
     * @since 0.1.0
     */
    public static synchronized void show(
            @NonNull Context context,
            @StringRes Integer titleResId,
            @StringRes Integer messageResId,
            @NonNull OnPromptListener listener
    ) {
        show(
                context, null, titleResId,
                messageResId, R.string.prompt_cancel_text,
                R.string.prompt_accept_text, listener
        );
    }

    /**
     * Prompt for an action
     *
     * @param context      valid launch context
     * @param themeResId   applied dialog theme
     * @param titleResId   prompt title
     * @param messageResId prompt message
     * @param listener     callback to invoke on accept or cancel
     * @since 0.1.0
     */
    public static synchronized void show(
            @NonNull Context context,
            @StyleRes Integer themeResId,
            @StringRes Integer titleResId,
            @StringRes Integer messageResId,
            @NonNull OnPromptListener listener
    ) {
        show(
                context, themeResId, titleResId,
                messageResId, R.string.prompt_cancel_text,
                R.string.prompt_accept_text, listener
        );
    }

    /**
     * Prompt for an action
     *
     * @param context         valid launch context
     * @param themeResId      applied dialog theme
     * @param titleResId      prompt title
     * @param messageResId    prompt message
     * @param cancelTextResId cancel button label
     * @param acceptTextResId accept button label
     * @param listener        callback to invoke on accept or cancel
     * @since 0.1.0
     */
    public static synchronized void show(
            @NonNull Context context,
            @StyleRes Integer themeResId,
            @StringRes Integer titleResId,
            @StringRes Integer messageResId,
            @StringRes Integer cancelTextResId,
            @StringRes Integer acceptTextResId,
            @NonNull OnPromptListener listener
    ) {
        // prepare prompt
        MaterialAlertDialogBuilder prompt = (
                themeResId != null ?
                        new MaterialAlertDialogBuilder(context, themeResId) :
                        new MaterialAlertDialogBuilder(context)
        );
        prompt.setTitle(titleResId);
        prompt.setMessage(messageResId);
        // handle cancel
        prompt.setNegativeButton(cancelTextResId, (dialog, i) -> {
            dialog.dismiss();
            listener.onClick(false);
        });
        // handle accept
        prompt.setPositiveButton(acceptTextResId, (dialog, i) -> {
            dialog.dismiss();
            listener.onClick(true);
        });
        // present prompt
        prompt.show();
    }

    /**
     * Prompt actions listener
     *
     * @since 0.1.0
     */
    public interface OnPromptListener {
        /**
         * Called when action accepted or cancelled
         *
         * @since 0.1.0
         */
        @MainThread
        void onClick(Boolean accepted);
    }
}

package com.onesmock.Util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.onesmock.R;

public class ConfirmDialogEditext  extends Dialog {

    private Context context;
    private String title;
    private String confirmButtonText;
    private String cacelButtonText;
    private ConfirmDialogEditext.ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }

    public ConfirmDialogEditext(Context context, String title, String confirmButtonText, String cacelButtonText) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.confirm_dialog_editext, null);
        setContentView(view);

        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvConfirm = (TextView) view.findViewById(R.id.confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.cancel);

        tvTitle.setText(title);
        tvConfirm.setText(confirmButtonText);
        tvCancel.setText(cacelButtonText);

        tvConfirm.setOnClickListener(new ConfirmDialogEditext.clickListener());
        tvCancel.setOnClickListener(new ConfirmDialogEditext.clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ConfirmDialogEditext.ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.confirm:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    };

}

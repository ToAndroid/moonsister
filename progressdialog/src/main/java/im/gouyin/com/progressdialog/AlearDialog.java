package im.gouyin.com.progressdialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jb on 2016/8/17.
 */
public class AlearDialog {
    private final Activity activity;
    private DialogType type;
    private Dialog dialog;

    public static enum DialogType {
        Certification_publish,Certification_vip, Certification_publish_1002, Certification_comment, Certification_im, Certification_dynamic, Certification_im_1015, bind_phone, app_logout, Certification_im_1002
    }

    public static enum clickType {
        cancel, confirm, confirm_vip
    }

    public AlearDialog(DialogType type, Activity activity) {
        this.type = type;
        this.activity = activity;
        initView();
    }

    private void initView() {
        int resID = 0;
        switch (type) {
            case Certification_publish:
                resID = R.layout.dialog_certification;
                break;
            case Certification_publish_1002:
                resID=R.layout.dialog_certification_10002;
                break;
            case Certification_comment:
                resID = R.layout.dialog_certification;
                break;
            case Certification_im:
                resID = R.layout.dialog_certification_im;
                break;
            case Certification_dynamic:
                resID = R.layout.dialog_certification_dynamic;
                break;
            case Certification_im_1015:
                resID = R.layout.certification_im_1015;
                break;
            case Certification_im_1002:
                resID = R.layout.certification_im_1002;
                break;
            case bind_phone:
                resID = R.layout.dialog_bind_phone;
                break;
            case app_logout:
                resID = R.layout.dialog_app_logout;
                break;
            case Certification_vip:
                resID =R.layout.dialog_certification_vip;
                break;
        }
        initDialog(resID);
    }

    private void initDialog(@LayoutRes int resID) {
        // 1. 布局文件转换为View对象
        LayoutInflater inflater = LayoutInflater.from(activity);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(resID, null);

        // 2. 新建对话框对象
        dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView certification_text = (TextView) layout.findViewById(R.id.certification_text);
        if (certification_text != null) {
            certification_text.setText(getContent());
        }

    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private String getContent() {
        if (activity == null)
            return "";
        String content = "";
        switch (type) {
            case Certification_publish:
                content = activity.getResources().getString(R.string.certification_publish_text);
                break;
            case Certification_publish_1002:
                content=activity.getString(R.string.certification_publish_1002_text);
                break;
            case Certification_comment:
                content = activity.getResources().getString(R.string.certification_comment_text);
                break;
            case Certification_im:
                content = "  " + activity.getResources().getString(R.string.certification_im_text);
                break;
            case Certification_dynamic:
                content = activity.getResources().getString(R.string.certification_dynamic_text);
                break;
            case Certification_im_1015:
                content = activity.getResources().getString(R.string.Certification_im_1015);
                break;
            case Certification_im_1002:
                content = activity.getResources().getString(R.string.Certification_im_1015);
                break;
            case bind_phone:
                content = activity.getResources().getString(R.string.bind_phone_text);
                break;
            case app_logout:
                content=activity.getString(R.string.app_logout_text);
                break;
            case Certification_vip:
                content=activity.getString(R.string.certification_vip);
                break;

        }
        return content;
    }

    public void setListenter(final onClickListenter listenter) {
        if (dialog != null) {
            View cancel = dialog.findViewById(R.id.cancel);
            if (cancel != null)
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listenter != null)
                            listenter.clickType(clickType.cancel);
                        if (dialog.isShowing()) {
                            dismiss();
                        }
                    }
                });
            View confirm = dialog.findViewById(R.id.confirm);
            if (confirm != null) {
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listenter != null)
                            listenter.clickType(clickType.confirm);
                    }
                });

            }
            View confirm_vip = dialog.findViewById(R.id.confirm_vip);
            if (confirm_vip != null) {
                confirm_vip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listenter != null)
                            listenter.clickType(clickType.confirm_vip);
                    }
                });
            }
        }
    }

    public interface onClickListenter {
        void clickType(clickType type);
    }
}

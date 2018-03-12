package com.poso2o.lechuan.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.print.PrintBarcodeActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.printer.PrintNumsItemData;
import com.poso2o.lechuan.util.Toast;


/**
 *
 * Created by lenovo on 2016/12/22.
 */
public class PrintBarcodeNumItemView extends BaseView {
    private View view;
    private PrintNumsItemData printNumsItemData;
    private TextView print_item_color_size;
    private TextView print_item_barcode;

    private ImageView print_item_sub;
    private ImageView print_item_add;
    private EditText print_item_num;
    private String numEdit;

    private ImageView print_item_select;
    private LinearLayout print_item_select_group;

    public PrintBarcodeNumItemView(Context context, PrintNumsItemData printNumsItemData) {
        super(context);
        this.printNumsItemData = printNumsItemData;
    }

    @Override
    public View initGroupView() {
        return view = View.inflate(context, R.layout.view_print_barcode_nums_item, null);
    }

    @Override
    public void initView() {
        print_item_color_size = (TextView) view.findViewById(R.id.print_item_color_size);
        print_item_barcode = (TextView) view.findViewById(R.id.print_item_barcode);

        print_item_sub = (ImageView) view.findViewById(R.id.print_item_sub);
        print_item_add = (ImageView) view.findViewById(R.id.print_item_add);

        print_item_num = (EditText) view.findViewById(R.id.print_item_num);

        print_item_select = (ImageView) view.findViewById(R.id.print_item_select);
        print_item_select_group= (LinearLayout) view.findViewById(R.id.print_item_select_group);

    }

    @Override
    public void initData() {
        print_item_color_size.setText(printNumsItemData.spec_name);
        print_item_barcode.setText(printNumsItemData.barcode);

        initListenner();
    }

    public void initListenner() {
        print_item_num.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (print_item_num.length() > 2) {
                    print_item_num.setText(numEdit);
                    Toast.show(context,"单个数量不能超过99");
                } else {
                    numEdit = print_item_num.getText().toString().trim();
                }

                if (context instanceof PrintBarcodeActivity) {
                    ((PrintBarcodeActivity) context).notifyAllPrintNum();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (print_item_num.getText()==null||print_item_num.getText().toString().equals("")){
                    print_item_num.setText(0+"");
                }
            }
        });

        print_item_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i;
                if (print_item_num.getText() == null || print_item_num.getText().toString().trim().equals("")) {
                    i = 0;
                } else {
                    i = Integer.parseInt(print_item_num.getText().toString().trim());
                }
                i--;
                if (i < 0) {
                    i = 0;
                    Toast.show(context,"数量不能小于0");
                }
                print_item_num.setText(i + "");
                if (i <= 0) {
                    print_item_select.setSelected(false);
                    printNumsItemData.isSelect = false;
                }
                if (context instanceof PrintBarcodeActivity) {
                    ((PrintBarcodeActivity) context).notifyAllPrintNum();
                }
            }
        });

        print_item_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i;
                if (print_item_num.getText() == null || print_item_num.getText().toString().trim().equals("")) {
                    i = 0;
                } else {
                    i = Integer.parseInt(print_item_num.getText().toString().trim());
                }

                i++;
                if (i > 99) {
                    i = 99;
                    Toast.show(context,"数量不能超过99");
                }
                print_item_num.setText(i + "");
                if (i > 0) {
                    print_item_select.setSelected(true);
                    printNumsItemData.isSelect = true;
                }
                if (context instanceof PrintBarcodeActivity) {
                    ((PrintBarcodeActivity) context).notifyAllPrintNum();
                }
            }
        });

        // 选择
        print_item_select_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (printNumsItemData.isSelect) {
                    print_item_select.setSelected(false);
                    printNumsItemData.isSelect = false;
                } else {
                    print_item_select.setSelected(true);
                    printNumsItemData.isSelect = true;
                }
                if (context instanceof PrintBarcodeActivity) {
                    ((PrintBarcodeActivity) context).notifyAllPrintNum();
                }

            }
        });
    }

    public int getNumber() {
        int i;
        if (print_item_num.getText() == null || print_item_num.getText().toString().trim().equals("")) {
            i = 0;
        } else {
            i = Integer.parseInt(print_item_num.getText().toString().trim());
        }
        return i;
    }

    /**
     * 设置数量
     * 不可以设置小于0 或大于99的数量
     */
    public void setNumber(int i) {
        if (i < 0 || i > 99) {
            Toast.show(context,"只能设置大于-1小于100的数量");
            return;
        }
        print_item_num.setText(i + "");
    }

    /**
     * 获取条码信息
     *
     * @return
     */
    public PrintNumsItemData getPrintNumsItemData() {
        return printNumsItemData;
    }

    public void setSelect(boolean b) {
        print_item_select.setSelected(b);
        printNumsItemData.isSelect = b;
    }

}

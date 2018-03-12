package com.poso2o.lechuan.bean.printer;

import com.poso2o.lechuan.bean.order.OrderDTO;

import java.io.Serializable;
import java.util.ArrayList;

public class PrinterBean implements Serializable {

	public int printer_type = 0;// 打印机类型，0：票据，1：标签
	public int ticket_type = 0;// 小票类型：0:打印测试，1：销售单，2：退货单，3：交接班，4:会员充值，5:标签
	public int printer_num = 1;// 打印张数
	public String print_message = "欢迎使用日进斗金店铺管理系统！";// 欢迎使用日进斗金店铺管理系统！
	public int open_casher = 0;// 1/2:开钱箱,3:检测打印机状态
	public ArrayList<PrintData> lablePrinterBeans;//标签数据
	public ArrayList<OrderDTO> orderDTOs;//订单数据

}

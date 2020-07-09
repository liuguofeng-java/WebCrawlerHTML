package com.xnx3.template;

import org.jvnet.substance.theme.SubstanceCremeTheme;

import com.xnx3.UI;

public class Entry {
	public static void main(String[] args) {
		new UI().UseLookAndFeelBySubstance().setCurrentTheme(new SubstanceCremeTheme());
		
		
		Global.mainUI.setVisible(true);
		Global.mainUI.getLblNewLabel_log().setText("<html><div style=\"width:100%; text-align:center; font-size:22px;\">点击查看使用说明</div>");
	}
}

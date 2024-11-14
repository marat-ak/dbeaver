package com.gnimsys.osaas.eclipse.ext.osaasjdbc.ui;

public class ComboViewerElement implements IComboViewerElement{
    final String lable;
    final String value;
	@Override
	public String getComboText() {
		// TODO Auto-generated method stub
		return lable;
	}
    public ComboViewerElement(String lable,String value) {
    	this.lable = lable;
    	this.value = value;
    }
}

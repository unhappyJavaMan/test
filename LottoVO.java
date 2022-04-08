package com.lotto.model;

import java.sql.Date;

public class LottoVO implements java.io.Serializable {
	private Integer pk_id; 
	private String winnumber;
	private String choosenumber;
	private Date time;
	private Integer result;
	
	public Integer getPk_id() {
		return pk_id;
	}
	public void setPk_id(Integer pk_id) {
		this.pk_id = pk_id;
	}
	public String getWinnumber() {
		return winnumber;
	}
	public void setWinnumber(String winnumber) {
		this.winnumber = winnumber;
	}
	public String getChoosenumber() {
		return choosenumber;
	}
	public void setChoosenumber(String choosenumber) {
		this.choosenumber = choosenumber;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	
}

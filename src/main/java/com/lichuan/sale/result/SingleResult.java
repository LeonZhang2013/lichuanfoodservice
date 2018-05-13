package com.lichuan.sale.result;


public class SingleResult<T> extends Result {

	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}

package edu.wtbu.pojo;

public class Page {
	int total;
	int startPage;
	int PageSize;
	public Page(int total, int startPage, int pageSize) {
		super();
		this.total = total;
		this.startPage = startPage;
		PageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	
	
}

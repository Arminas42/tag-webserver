package com.kuaprojects.rental.frontend.utils;

import lombok.Data;

@Data
public class PagingObj {
    private int currentPage;
    private int pageSize;

    public PagingObj(int pageSize) {
        this.currentPage = 0;
        this.pageSize = pageSize;
    }
    public PagingObj() {
        this.currentPage = 0;
        this.pageSize = 5;
    }

    public void incrementCurrentPage(){
        this.currentPage++;
    }
    public void decrementCurrentPage(){
        this.currentPage--;
    }
}

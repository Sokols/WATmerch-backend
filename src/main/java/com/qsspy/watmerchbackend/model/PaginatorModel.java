package com.qsspy.watmerchbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatorModel {

    private List<Integer> pages;
    private boolean showFirstPage = true;
    private boolean showLastPage = true;
    private int lastPage;
}

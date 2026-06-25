package org.example.cms.entity;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private int total;
    private int totalPage;
    private List<T> data;
}

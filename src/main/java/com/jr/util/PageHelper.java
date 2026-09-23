package com.jr.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageHelper<T> {
    List<T>list;
    Integer indexPage;
    Integer dataCount;
    Integer pageData;
    Integer pageCount;
}

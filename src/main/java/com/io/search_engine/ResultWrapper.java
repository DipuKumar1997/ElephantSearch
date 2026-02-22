package com.io.search_engine;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResultWrapper<T> {
    private List<T> data;
    private long timeMs;
}
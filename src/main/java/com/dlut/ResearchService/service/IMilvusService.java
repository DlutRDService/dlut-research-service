package com.dlut.ResearchService.service;

import java.util.Set;

public interface IMilvusService {
    Set<Integer> searchByString(String query, String collectionName);
}

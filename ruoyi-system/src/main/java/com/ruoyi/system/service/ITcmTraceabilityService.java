package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

public interface ITcmTraceabilityService
{
    List<Map<String, Object>> listBase(String userName);

    Map<String, Object> getBase(Long id, String userName);

    int addBase(Map<String, Object> data, String userName, Long userId);

    int editBase(Map<String, Object> data, String userName, Long userId);

    int removeBase(Long id, String userName);

    List<Map<String, Object>> listProduct(String userName);

    Map<String, Object> getProduct(Long id, String userName);

    int addProduct(Map<String, Object> data, String userName, Long userId);

    int editProduct(Map<String, Object> data, String userName, Long userId);

    int removeProduct(Long id, String userName);

    List<Map<String, Object>> listBatch(String userName);

    Map<String, Object> getBatch(Long id, String userName);

    int addBatch(Map<String, Object> data, String userName, Long userId);

    int editBatch(Map<String, Object> data, String userName, Long userId);

    int removeBatch(Long id, String userName);

    Map<String, Object> publishBatch(Long batchId, String userName, Long userId);

    List<Map<String, Object>> listProcess(String userName, Long batchId);

    int addProcess(Map<String, Object> data, String userName, Long userId);

    List<Map<String, Object>> listReview(String userName, Long batchId);

    int addReview(Map<String, Object> data, String userName, Long userId);

    Map<String, Object> queryTrace(String traceCode);
}

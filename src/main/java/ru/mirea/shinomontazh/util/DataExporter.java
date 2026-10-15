package ru.mirea.shinomontazh.util;

import ru.mirea.shinomontazh.model.WorkOrder;

import java.util.List;

public interface DataExporter {

    void export(
            List<WorkOrder> orders,
            String fileName
    ) throws Exception;
}
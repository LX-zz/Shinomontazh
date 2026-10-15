package ru.mirea.shinomontazh.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.mirea.shinomontazh.model.WorkOrder;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter implements DataExporter {

    @Override
    public void export(
            List<WorkOrder> orders,
            String fileName
    ) throws Exception {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Заказы");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Автомобиль");
            header.createCell(2).setCellValue("Госномер");
            header.createCell(3).setCellValue("Услуга");
            header.createCell(4).setCellValue("Статус");
            header.createCell(5).setCellValue("Цена");
            header.createCell(6).setCellValue("Дата создания");
            header.createCell(7).setCellValue("ID клиента");

            int rowNumber = 1;

            for (WorkOrder order : orders) {

                Row row = sheet.createRow(rowNumber);

                row.createCell(0)
                        .setCellValue(order.getId());

                row.createCell(1)
                        .setCellValue(order.getCarBrand());

                row.createCell(2)
                        .setCellValue(order.getCarNumber());

                row.createCell(3)
                        .setCellValue(order.getServiceName());

                row.createCell(4)
                        .setCellValue(order.getStatus().name());

                row.createCell(5)
                        .setCellValue(order.getPrice().doubleValue());

                row.createCell(6)
                        .setCellValue(order.getCreateDate().toString());

                row.createCell(7)
                        .setCellValue(order.getClientId());

                rowNumber++;
            }

            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            try (
                    FileOutputStream outputStream =
                            new FileOutputStream(fileName)
            ) {

                workbook.write(outputStream);
            }
        }
    }
}
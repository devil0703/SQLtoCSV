package com.example.springboot.service;
import com.opencsv.CSVWriter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileStorageService {
    @PersistenceContext
    private EntityManager entityManager;
    public void SQLtoCSV(String sqlCode, String sqlData, String csvCode, String csvData){
        excuteQueryandStore(sqlCode, csvCode);
        excuteQueryandStore(sqlData, csvData);
    }

    public void excuteQueryandStore(String sqlFile, String csvFile){
        Path sqlPath = Paths.get(sqlFile);
        try {
            String sqlQuery = Files.readString(sqlPath);
            @SuppressWarnings("unchecked")
            List<Object[]> resultList = entityManager
                    .createNativeQuery(sqlQuery)
                    .getResultList();

            if (resultList.isEmpty()) {
                return; // No data to write
            }
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
                for (Object[] record : resultList) {
                    String[] data = new String[record.length];
                    for (int i = 0; i < record.length; i++) {
                        data[i] = record[i].toString();
                    }
                    writer.writeNext(data);
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

}

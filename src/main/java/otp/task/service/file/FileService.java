package otp.task.service.file;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import otp.task.models.otp.OTPCode;
import otp.task.service.OTPCodeService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final OTPCodeService otpCodeService;

    public ByteArrayInputStream createListOTPCode(List<OTPCode> codeList){

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("OTP Codes");

            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("OTP Code");

            int rowIdx = 1;
            for (OTPCode code : codeList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(code.getCode());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException ex) {
            throw new RuntimeException("Ошибка генерации Excel-файла", ex);
        }
    }
}

package com.tienda.service.impl;

import com.tienda.service.ReporteService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private DataSource dataSource;

    @Override
    public ResponseEntity<Resource> generaReporte(
            String reporte,
            Map<String, Object> parametros,
            String tipo) throws IOException {

        // se asigna el tipo de página a generar...
        String estilo = tipo.equalsIgnoreCase("vPdf")
                ? "inline; " : "attachment; ";

        // se establece la ruta de los reportes
        String reportePath = "reportes";

        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        ClassPathResource fuente = new ClassPathResource(
                reportePath
                + File.separator
                + reporte
                + ".jasper"
        );

        InputStream elReporte = fuente.getInputStream();

        try {
            var reporteJasper = JasperFillManager.fillReport(elReporte, parametros, dataSource.getConnection());

            // A partir de acá inicia la respuesta al usuario
            MediaType mediaType = null;
            String archivoSalida = "";

            // Se debe decidir cual tipo de reporte se genera
            switch (tipo) {
                case "Pdf", "vPdf" -> { // Se genera un reporte en Pdf
                    JasperExportManager
                            .exportReportToPdfStream(
                                    reporteJasper,
                                    salida);
                    mediaType = MediaType.APPLICATION_PDF;
                    archivoSalida = reporte + ".pdf";
                }
                case "Xls" -> { // Se descargará un Excel

                    JRXlsExporter paraExcel = new JRXlsExporter();
                    paraExcel.setExporterInput(
                            new SimpleExporterInput(reporteJasper));

                    paraExcel.setExporterOutput(
                            new SimpleOutputStreamExporterOutput(salida));

                    SimpleXlsxReportConfiguration configuracion = new SimpleXlsxReportConfiguration();

                    configuracion.setDetectCellType(true);
                    configuracion.setCollapseRowSpan(true);

                    paraExcel.setConfiguration(configuracion);
                    paraExcel.exportReport();

                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    archivoSalida = reporte + ".xlsx";
                }
                case "Csv" -> { // Se descargará un texto tipo Csv

                    JRCsvExporter paraCsv = new JRCsvExporter();
                    paraCsv.setExporterInput(
                            new SimpleExporterInput(reporteJasper));

                    paraCsv.setExporterOutput(
                            new SimpleWriterExporterOutput(salida));

                    paraCsv.exportReport();

                    mediaType = MediaType.TEXT_PLAIN;
                    archivoSalida = reporte + ".csv";
                }
            }

            // A partir de acá se realiza la respuesta al usuario
            byte[] data = salida.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition", estilo + "filename=\"" + archivoSalida + "\"");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(data.length)
                    .contentType(mediaType)
                    .body(new InputStreamResource(
                            new ByteArrayInputStream(data)
                    ));

        } catch (JRException | SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}

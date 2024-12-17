package com.yf;

import com.yf.model.generate.dto.GenCodeDto;
import com.yf.model.generate.entity.GenTable;
import com.yf.model.generate.entity.GenTableFields;
import com.yf.service.impl.GenTableFieldsServiceImpl;
import com.yf.service.impl.GenTableServiceImpl;
import com.yf.utils.FreemarkerGenCodeUtil;
import com.yf.utils.GenCodeUtils;
import freemarker.template.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成测试类
 *
 * @author : YiFei
 * @since : 2024/6/14 9:14
 */
@SpringBootTest
public class GenerateCodeTest {

    @Autowired
    private Configuration configuration;

    @Autowired
    private GenTableServiceImpl genTableService;

    @Autowired
    private GenTableFieldsServiceImpl genTableFieldsService;

    @Autowired
    private FreemarkerGenCodeUtil freemarkerGenCodeUtil;

    public static void zipFiles(Map<String, byte[]> files, String zipFilePath) throws IOException {
        Path zipPath = Paths.get(zipFilePath);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                String filePath = entry.getKey();
                byte[] content = entry.getValue();

                ZipEntry zipEntry = new ZipEntry(filePath);
                zos.putNextEntry(zipEntry);
                zos.write(content);
                zos.closeEntry();
            }
        }
    }

    @Test
    public void testEntity() {
        // 1. 生成的 table_id
        Integer tableId = 26;
        // 2. 查询数据库表信息
        GenTable byId = genTableService.getById(tableId);
        // 3. 查询数据库字段信息
        List<GenTableFields> list = genTableFieldsService.lambdaQuery().eq(GenTableFields::getTableId, tableId).list();
        GenCodeDto genCodeDto = new GenCodeDto(byId, list);
        try {
            Map<String, byte[]> genCodeByte = freemarkerGenCodeUtil.getGenCodeByte(genCodeDto);
            // 打包成ZIP并保存到D盘
            String zipFilePath = "D:/output.zip";
            zipFiles(genCodeByte, zipFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void syncTable() {
//        System.out.println(genTableService.syncDatabase());
        System.out.println(GenCodeUtils.needsModify("provider"));
    }

}

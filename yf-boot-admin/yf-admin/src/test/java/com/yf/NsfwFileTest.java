package com.yf;

import com.yf.file.utils.NSFWAnalyzerUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class NsfwFileTest {

    @Autowired
    private NSFWAnalyzerUtils nsfwFileUtils;

    @Test
    public void testNsfw() throws IOException {
        File file = new File("C:\\Users\\yifei\\Pictures\\Camera Roll\\QQ图片20240707160518.gif");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/gif", input);

        System.out.println(nsfwFileUtils.getNsfwPredictions(multipartFile));
    }
}

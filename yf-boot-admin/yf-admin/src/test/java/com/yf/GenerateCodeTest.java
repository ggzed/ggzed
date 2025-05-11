package com.yf;

import com.yf.model.generate.dto.GenCodeDto;
import com.yf.model.generate.entity.GenTable;
import com.yf.model.generate.entity.GenTableFields;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 代码生成测试类
 *
 * @author : YiFei
 * @since : 2024/6/14 9:14
 */
//@SpringBootTest
public class GenerateCodeTest {

    private static final Logger log = LoggerFactory.getLogger(GenerateCodeTest.class);
    //    @Autowired
    private Configuration configuration;

    @Test
    public void testTemplate() throws IOException, TemplateException {
        GenCodeDto genCodeDto = new GenCodeDto();

        genCodeDto.setTable(GenTable.builder()
                .packageName("yf")
                .moduleName("system")
                .businessName("user")
                .className("User")
                .tableComment("用户")
                .tableName("user")
                .componentName("user")
                .functionAuthor("YiFei")
                .build());

        List<GenTableFields> genTableFields = new ArrayList<>();

        genTableFields.add(GenTableFields.builder()
                .isPk(true)
                .isForm(false)
                .isIncrement(true)
                .isQuery(false)
                .isShow(false)
                .saveFormType("input_number")
                .queryFormType("input_number")
                .queryType("eq")
                .javaTsFieldName("id")
                .javaType("Long")
                .tsType("string")
                .dictTypeName("")
                .isRequired(true)
                .showType("default")
                .showName("主键")
                .sort(1)
                .tableId(1)
                .columnName("id")
                .columnType("bigint")
                .columnComment("主键")
                .build());

        genTableFields.add(GenTableFields.builder()
                .isPk(false)
                .isForm(true)
                .isIncrement(false)
                .isQuery(true)
                .isShow(true)
                .saveFormType("input")
                .queryFormType("input")
                .queryType("eq")
                .javaTsFieldName("userName")
                .javaType("String")
                .tsType("string")
                .showType("default")

                .dictTypeName("")
                .isRequired(false)
                .showName("名字")
                .sort(2)
                .tableId(1)
                .columnName("user_name")
                .columnType("varchar")
                .columnComment("名字")
                .build());

        genTableFields.add(GenTableFields.builder()
                .isPk(false)
                .isForm(true)
                .isIncrement(false)
                .isQuery(true)
                .isShow(true)
                .saveFormType("input_number")
                .queryFormType("input_number")
                .queryType("between")
                .javaTsFieldName("age")
                .javaType("Integer")
                .tsType("int")
                .dictTypeName("")
                .isRequired(true)
                .showType("tag")
                .showName("年龄")
                .sort(3)
                .tableId(1)
                .columnName("age")
                .columnType("int")
                .columnComment("年龄")
                .build());

        genTableFields.add(GenTableFields.builder()
                .isPk(false)
                .isForm(true)
                .isIncrement(false)
                .isQuery(true)
                .isRequired(true)
                .isShow(true)
                .saveFormType("multi_select")
                .queryFormType("select")
                .queryType("in")
                .javaTsFieldName("status")
                .javaType("Integer")
                .tsType("number")
                .showType("tag")
                .dictTypeName("status")
                .isRequired(false)
                .showName("状态")
                .sort(5)
                .tableId(1)
                .columnName("status")
                .columnType("int")
                .columnComment("状态")
                .build());

        genTableFields.add(GenTableFields.builder()
                .isPk(false)
                .isForm(false)
                .isIncrement(false)
                .isQuery(true)
                .isShow(true)
                .saveFormType("datetime")
                .queryFormType("datetime")
                .queryType("between")
                .javaTsFieldName("createTime")
                .javaType("LocalDateTime")
                .tsType("string")
                .dictTypeName("")
                .isRequired(false)
                .showType("default")
                .showName("创建时间")
                .sort(4)
                .tableId(1)
                .columnName("create_time")
                .columnType("datetime")
                .columnComment("创建时间")
                .build());

        List<GenTableFields> pkList = new ArrayList<>();
        List<GenTableFields> showList = new ArrayList<>();
        List<GenTableFields> formList = new ArrayList<>();
        List<GenTableFields> queryList = new ArrayList<>();
        List<GenTableFields> entityList = new ArrayList<>();

        for (GenTableFields tableField : genTableFields) {
            if (tableField.getIsPk()) {
                pkList.add(tableField);
            }

            if (tableField.getIsShow()) {
                showList.add(tableField);
            }

            if (tableField.getIsForm()) {
                formList.add(tableField);
            }

            if (tableField.getIsQuery()) {
                queryList.add(tableField);
            }

            entityList.add(tableField);
        }

        // 构建 map
        HashMap<String, List<GenTableFields>> stringListHashMap = new HashMap<>();

        stringListHashMap.put("pk", pkList);
        stringListHashMap.put("query", queryList);
        stringListHashMap.put("show", showList);
        stringListHashMap.put("form", formList);
        stringListHashMap.put("entity", entityList);

        genCodeDto.setMapFields(stringListHashMap);

//        // 加载模板
//        Template template = configuration.getTemplate("back-end/controller/controller.ftl" );
////
////        // 渲染模板
//        Writer out = new StringWriter();
//        template.process(genCodeDto, out);
//        System.out.println(out);


        // 加载模板
//        Template template2 = configuration.getTemplate("back-end/service/service.ftl" );
//
//        // 渲染模板
//        Writer out2 = new StringWriter();
//        template2.process(genCodeDto, out2);
//        System.out.println(out2);

//         加载模板
        Template template3 = configuration.getTemplate("back-end/service/impl/service-impl.ftl");

        // 渲染模板
        Writer out3 = new StringWriter();
        template3.process(genCodeDto, out3);
        System.out.println(out3);

//        Template template4 = configuration.getTemplate("back-end/converter/converter.ftl" );
//
//        // 渲染模板
//        Writer out4 = new StringWriter();
//        template4.process(genCodeDto, out4);
//        System.out.println(out4);

//        Template template5 = configuration.getTemplate("back-end/model/entity/entity.ftl" );
//
//        // 渲染模板
//        Writer out5 = new StringWriter();
//        template5.process(genCodeDto, out5);
//        System.out.println(out5);

//        Template template6 = configuration.getTemplate("back-end/model/form/form.ftl" );
//
//        // 渲染模板
//        Writer out6 = new StringWriter();
//        template6.process(genCodeDto, out6);
//        System.out.println(out6);

//        Template template7 = configuration.getTemplate("back-end/model/query/query.ftl" );
//
//        // 渲染模板
//        Writer out7 = new StringWriter();
//        template7.process(genCodeDto, out7);
//        System.out.println(out7);

//        Template template8 = configuration.getTemplate("back-end/model/vo/vo.ftl" );
//
//        // 渲染模板
//        Writer out8 = new StringWriter();
//        template8.process(genCodeDto, out8);
//        System.out.println(out8);

        Template template9 = configuration.getTemplate("front-end/api/api.ftl");

        // 渲染模板
        Writer out9 = new StringWriter();
        template9.process(genCodeDto, out9);
        System.out.println(out9);


        Template template10 = configuration.getTemplate("front-end/api/type.ftl");

        // 渲染模板
        Writer out10 = new StringWriter();
        template10.process(genCodeDto, out10);
        System.out.println(out10);

        Template template11 = configuration.getTemplate("front-end/views/index.ftl");

        // 渲染模板
        Writer out11 = new StringWriter();
        template11.process(genCodeDto, out11);
        System.out.println(out11);

        Template template12 = configuration.getTemplate("front-end/views/components/table.ftl");

        // 渲染模板
        Writer out12 = new StringWriter();
        template12.process(genCodeDto, out12);
        System.out.println(out12);

        Template template13 = configuration.getTemplate("front-end/views/components/search.ftl");

        // 渲染模板
        Writer out13 = new StringWriter();
        template13.process(genCodeDto, out13);
        System.out.println(out13);

        Template template14 = configuration.getTemplate("front-end/views/components/interactions/dialog.ftl");

        // 渲染模板
        Writer out14 = new StringWriter();
        template14.process(genCodeDto, out14);
        System.out.println(out14);

    }
}

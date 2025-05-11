package com.yf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.constants.GenTableDefaultConstants;
import com.yf.constants.GenerateCrudCodeConstants;
import com.yf.constants.RedisKeyConstants;
import com.yf.constants.SystemConstants;
import com.yf.converter.GenerateCrudCodeConverter;
import com.yf.exception.ServiceException;
import com.yf.file.model.dto.ResourcesFile;
import com.yf.mapper.system.SysMenuMapper;
import com.yf.model.common.Option;
import com.yf.model.generate.bo.DBTableInfoBO;
import com.yf.model.generate.dto.GenCodeDto;
import com.yf.model.generate.entity.GenTable;
import com.yf.model.generate.entity.GenTableFields;
import com.yf.model.generate.form.GenTableMenuForm;
import com.yf.model.generate.query.GenCrudTablePageQuery;
import com.yf.model.system.entity.SysMenu;
import com.yf.model.system.enums.MenuTypeEnum;
import com.yf.model.vo.GenCrudTableVO;
import com.yf.model.vo.PreviewGenCodeTreeVO;
import com.yf.result.ResultCode;
import com.yf.service.IGenTableFieldsService;
import com.yf.service.IGenTableService;
import com.yf.service.IGenerateCrudCodeService;
import com.yf.utils.GenerateCrudCodeUtils;
import com.yf.utils.ResourcesFileUtil;
import com.yf.utils.TreeNodeUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.yf.constants.GenTableDefaultConstants.ENTITY_KEY;
import static com.yf.constants.GenTableDefaultConstants.FORM_KEY;
import static com.yf.constants.GenTableDefaultConstants.PK_KEY;
import static com.yf.constants.GenTableDefaultConstants.PREVIEW_TEMPLATE;
import static com.yf.constants.GenTableDefaultConstants.QUERY_KEY;
import static com.yf.constants.GenTableDefaultConstants.SHOW_KEY;
import static com.yf.constants.GenerateCrudCodeConstants.BACK_END_TEMPLATE_PATH_MAP;
import static com.yf.constants.GenerateCrudCodeConstants.BACK_TEMPLATE_ROUTING_MAP;
import static com.yf.constants.GenerateCrudCodeConstants.FORNT_TEMPLATE_ROUTING_MAP;
import static com.yf.constants.GenerateCrudCodeConstants.FRONT_END_TEMPLATE_PATH_MAP;
import static com.yf.constants.GenerateCrudCodeConstants.TEMPLATE_BUSINESS_NAME;
import static com.yf.constants.GenerateCrudCodeConstants.TEMPLATE_MODULE_NAME;

/**
 * GenTableIServiceImpl
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateCrudCodeServiceImpl implements IGenerateCrudCodeService {

    private final GenerateCrudCodeConverter generateCrudCodeConverter;
    private final IGenTableService tableService;
    private final IGenTableFieldsService tableFieldsService;
    private final SysMenuMapper menuMapper;
    private final Configuration configuration;
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 分页查询目前已经可生成的数据
     *
     * @param queryParams 查询参数
     * @return IPage<GenCrudTableVO> 分页展示内容
     */
    public IPage<GenCrudTableVO> getGenCrudTablePage(GenCrudTablePageQuery queryParams) {
        String functionAuthor = queryParams.getFunctionAuthor();
        String tableName = queryParams.getTableName();
        String remark = queryParams.getRemark();
        // 1. 分页查询目前已经可生成的数据
        Page<GenTable> page = tableService.lambdaQuery()
                .like(StringUtils.hasText(functionAuthor), GenTable::getFunctionAuthor, functionAuthor)
                .like(StringUtils.hasText(tableName), GenTable::getTableName, tableName)
                .like(StringUtils.hasText(remark), GenTable::getRemark, remark)
                .page(queryParams.lambdaMpPage(GenTable::getCreateTime, false));

        // 2. 查询表字段信息
        List<GenTable> records = page.getRecords();

        List<Integer> menuIds = records.stream()
                .map(GenTable::getMenuId)
                .distinct().toList();
        List<SysMenu> menus = menuIds.isEmpty() ? Collections.emptyList() : menuMapper.selectBatchIds(menuIds);

        Map<Integer, String> menuIdToNameMap = menus.stream()
                .collect(Collectors.toMap(SysMenu::getId, SysMenu::getName));
        // 3. 转换为VO
        Set<Integer> updateMenuGenTableId = new HashSet<>();
        List<GenCrudTableVO> result = records.stream().map(genTable -> {
            String menuName = menuIdToNameMap.get(genTable.getMenuId());
            GenCrudTableVO genCrudTableVO = generateCrudCodeConverter.genTable2GenCrudTableVo(genTable);
            if (StringUtils.hasText(menuName)) {
                genCrudTableVO.setMenuName(menuName);
            } else {
                updateMenuGenTableId.add(genTable.getId());
            }
            return genCrudTableVO;
        }).toList();
        // 4. 更新菜单数据
        if (!updateMenuGenTableId.isEmpty()) {
            tableService.lambdaUpdate()
                    .in(GenTable::getId, updateMenuGenTableId)
                    .set(GenTable::getMenuId, null)
                    .update();
        }
        // 5. 返回对于数据
        return new Page<GenCrudTableVO>(page.getCurrent(), page.getSize(), page.getTotal()).setRecords(result);
    }

    /**
     * 导入数据库表到生成表中
     *
     * @param dbTableNames 数据库表名
     */
    @Transactional
    @Override
    public void importDBToTable(List<String> dbTableNames) {
        // 后续优化 For 循环
        for (String dbTableName : dbTableNames) {
            // 1. 检查是否已经存在
            if (tableService.lambdaQuery()
                    .select(GenTable::getId)
                    .eq(GenTable::getTableName, dbTableName)
                    .exists()) {
                throw new ServiceException(ResultCode.GEN_TABLE_ALREADY_EXIST);
            }
            // 2.查询数据库表，以及对应字段信息
            DBTableInfoBO dbTableInfoBO = tableService.getDBTableInfo(dbTableName);
            if (dbTableInfoBO == null) {
                continue;
            }
            // 3. 分析后转换为 GenTable、GenTableFields （ 可先转为 DTO，再转为 Entity ）
            String tableName = dbTableInfoBO.getTableName();
            String moduleName = GenerateCrudCodeUtils.getModuleName(tableName);
            // 存储表信息
            GenTable genTable = GenTable.builder().tableName(tableName).tableComment(dbTableInfoBO.getTableComment())
                    .functionAuthor(GenTableDefaultConstants.FUNCTION_AUTHOR)
                    .backEndType(GenTableDefaultConstants.BACK_END_TYPE)
                    .frontEndType(GenTableDefaultConstants.FRONT_END_TYPE)
                    .className(GenerateCrudCodeUtils.tableToClassName(tableName))
                    .componentName(GenerateCrudCodeUtils.tableToComponentName(tableName))
                    .moduleName(moduleName).packageName(GenTableDefaultConstants.PACKAGE_NAME)
                    .businessName(GenerateCrudCodeUtils.getBusinessName(tableName))
                    .remark(GenTableDefaultConstants.IMPORT_TABLE_REMARK)
                    .build();

            tableService.save(genTable);

            Integer result = genTable.getId();
            // 存储字段信息
            List<GenTableFields> genTableFields = dbTableInfoBO.getFieldList().stream().map(dbTableFieldInfoBO -> {
                String fieldType = dbTableFieldInfoBO.getFieldType();
                String fieldName = dbTableFieldInfoBO.getFieldName();
                String fieldComment = dbTableFieldInfoBO.getFieldComment();
                // 字典类型需要用户自己选择
                return GenTableFields.builder()
                        .tableId(result)
                        .showName(fieldComment.substring(0, Math.min(8, fieldComment.length())))
                        .columnName(fieldName)
                        .columnType(fieldType)
                        .columnComment(fieldComment)
                        .javaTsFieldName(GenerateCrudCodeUtils.getJavaTsFieldName(fieldName))
                        .javaType(GenerateCrudCodeConstants.FIELD_TYPE_MAP.get(fieldType)[0])
                        .tsType(GenerateCrudCodeConstants.FIELD_TYPE_MAP.get(fieldType)[1])
                        .isPk(dbTableFieldInfoBO.getIsPk())
                        .isIncrement(dbTableFieldInfoBO.getIsIncrement())
                        .isRequired(dbTableFieldInfoBO.getIsRequired())
                        .isShow(GenerateCrudCodeUtils.judgeShowField(fieldName, fieldType, fieldComment))
                        .showType(GenerateCrudCodeUtils.judgeShowType(fieldName, fieldType, fieldComment))
                        .isForm(GenerateCrudCodeUtils.judgeFormField(fieldName, fieldType, fieldComment))
                        .saveFormType(GenerateCrudCodeUtils.judgeSaveFormType(fieldName, fieldType, fieldComment))
                        .isQuery(GenerateCrudCodeUtils.judgeQueryField(fieldName, fieldType, fieldComment))
                        .queryFormType(GenerateCrudCodeUtils.judgeQueryFormType(fieldName, fieldType, fieldComment))
                        .queryType(GenerateCrudCodeUtils.judgeQueryType(fieldName, fieldType, fieldComment))
                        .sort(dbTableFieldInfoBO.getSort())
                        .build();
            }).toList();

            tableFieldsService.saveBatch(genTableFields);
        }
    }

    /**
     * 效果图
     *
     * @param tableId 表ID
     * @return 效果图 => html
     */
    @SneakyThrows
    @Override
    public String previewGenCrudDisplay(Integer tableId) {
        // 1. 构建代码生成DTO
        GenCodeDto genCodeDto = buildGenCodeDto(tableId);
        // 2. 构建模板信息
        Template template = configuration.getTemplate(PREVIEW_TEMPLATE);
        StringWriter writer = new StringWriter();
        template.process(genCodeDto, writer);
        return writer.toString();
    }

    /**
     * 预览生成代码树
     *
     * @param tableId 表ID
     * @return 预览生成代码树
     */
    @Override
    public PreviewGenCodeTreeVO previewGenCrudCode(Integer tableId) {
        GenCodeDto genCodeDto = buildGenCodeDto(tableId);
        List<String> parentList = Collections.singletonList("");
        // 处理后端模板
        List<ResourcesFile> backEndFiles = ResourcesFileUtil.buildResourcesFileList(
                BACK_END_TEMPLATE_PATH_MAP.get(genCodeDto.getTable().getBackEndType()),
                true);
        PreviewGenCodeTreeVO backEndVO = TreeNodeUtil.buildTree(
                generateCrudCodeConverter.resourcesFile2previewVO(backEndFiles), parentList, item -> {
                    processTreeItem(item, genCodeDto, true);
                }).get(0);
        // 处理前端模板
        List<ResourcesFile> frontEndFiles = ResourcesFileUtil.buildResourcesFileList(
                FRONT_END_TEMPLATE_PATH_MAP.get(genCodeDto.getTable().getFrontEndType()),
                true);
        PreviewGenCodeTreeVO frontEndVO = TreeNodeUtil.buildTree(
                generateCrudCodeConverter.resourcesFile2previewVO(frontEndFiles), parentList, item -> {
                    processTreeItem(item, genCodeDto, false);
                }).get(0);

        // 构建最终结果
        return PreviewGenCodeTreeVO.builder()
                .id(applicationName)
                .parentId(null)
                .content("")
                .isFile(false)
                .name(applicationName)
                .children(List.of(backEndVO, frontEndVO))
                .build();
    }

    /**
     * 添加生成表菜单
     *
     * @param tableId 表ID
     * @param form    菜单表单
     * @return 是否添加成功
     */
    @Override
    @Transactional
    @CacheEvict(value = RedisKeyConstants.SYSTEM_ROUTE_CACHE_PREFIX, key = "'routes'")
    public boolean addGenTableMenu(Integer tableId, GenTableMenuForm form) {
        GenTable genTable = tableService.getById(tableId);
        String basePathInfo = genTable.getModuleName() + "/" + genTable.getBusinessName();
        String basePermissionInfo = genTable.getModuleName() + ":" + genTable.getBusinessName();
        // 1.构建菜单信息
        String menuTreePath = TreeNodeUtil.generateTreePath(SystemConstants.INTEGER_ROOT_ID, form.getParentId(),
                (currentParentId) -> menuMapper.selectOne(
                        new LambdaQueryWrapper<SysMenu>()
                                .select(SysMenu::getTreePath)
                                .eq(SysMenu::getId, currentParentId)
                ).getTreePath());
        SysMenu menu = SysMenu.builder()
                .name(form.getMenuName())
                .parentId(form.getParentId())
                .treePath(menuTreePath)
                .component(basePathInfo + "/index")
                .path(basePathInfo)
                .permission(basePermissionInfo + ":list")
                .type(MenuTypeEnum.MENU)
                .icon(form.getIcon())
                .hidden(0)
                .keepAlive(1)
                .showSingleChildren(0)
                .breadcrumb(1)
                .affix(0)
                .sort(0).build();
        // 插入菜单信息
        menuMapper.insert(menu);
        Integer menuId = menu.getId();
        // 2. 构建按钮信息
        this.saveGenMenuButton(menuId, menuTreePath, genTable, basePermissionInfo);
        // 更新 table 信息
        tableService.lambdaUpdate()
                .eq(GenTable::getId, tableId)
                .set(GenTable::getMenuId, menuId)
                .update();
        return true;
    }

    /**
     * 保存菜单按钮
     *
     * @param menuId             菜单ID
     * @param menuTreePath       菜单树形路径
     * @param genTable           表信息
     * @param basePermissionInfo 基础权限信息
     */
    private void saveGenMenuButton(Integer menuId, String menuTreePath, GenTable genTable, String basePermissionInfo) {
        List<SysMenu> buttonList = new ArrayList<>(3);
        // 插入按钮信息
        String buttonTreePath = TreeNodeUtil.generateTreePath(
                SystemConstants.INTEGER_ROOT_ID,
                menuId,
                (currentParentId) -> menuTreePath);
        SysMenu.SysMenuBuilder buttonTemplate = SysMenu.builder()
                .parentId(menuId)
                .treePath(buttonTreePath)
                .type(MenuTypeEnum.BUTTON)
                .hidden(1)
                .keepAlive(0)
                .showSingleChildren(0)
                .affix(0)
                .sort(0);
        // 增加按钮
        buttonList.add(buttonTemplate
                .name(genTable.getTableComment() + "新增")
                .permission(basePermissionInfo + ":save")
                .build()
        );
        // 删除按钮
        buttonList.add(buttonTemplate
                .name(genTable.getTableComment() + "删除")
                .permission(basePermissionInfo + ":delete")
                .build()
        );
        // 修改按钮
        buttonList.add(buttonTemplate
                .name(genTable.getTableComment() + "修改")
                .permission(basePermissionInfo + ":update")
                .build()
        );

        for (SysMenu button : buttonList) {
            menuMapper.insert(button);
        }
    }

    /**
     * Crud 新增菜单目录下拉列表
     *
     * @return 菜单列表
     */
    @Override
    public List<Option<Integer>> listMenuOptions() {
        // 1. 查询目录
        List<SysMenu> list = menuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .select(SysMenu::getId, SysMenu::getName, SysMenu::getParentId)
                .eq(SysMenu::getType, MenuTypeEnum.CATALOG)
        );
        // 2. list -> options 返回
        return TreeNodeUtil.buildTree(list.stream().map(item -> Option.<Integer>builder()
                .id(item.getId())
                .parentId(item.getParentId())
                .label(item.getName())
                .build()
        ).toList(), Collections.singletonList(0));
    }

    /**
     * 生成 Crud 代码
     *
     * @param tableId 表ID
     * @return byte[]
     */
    @Override
    public byte[] generateCrudCodeZip(Integer tableId) {
        // 构建 Freemarker 模版 所需的 DTO
        GenCodeDto genCodeDto = buildGenCodeDto(tableId);
        // 读取前后端模版文件
        List<ResourcesFile> backEndTemplateFile = ResourcesFileUtil.buildResourcesFileList(
                BACK_END_TEMPLATE_PATH_MAP.get(genCodeDto.getTable().getBackEndType()), true);
        List<ResourcesFile> frontEndTemplateFile = ResourcesFileUtil.buildResourcesFileList(
                FRONT_END_TEMPLATE_PATH_MAP.get(genCodeDto.getTable().getFrontEndType()), true);
        // 生成 Zip
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (ResourcesFile resourcesFile : backEndTemplateFile) {
                processTemplateFile(resourcesFile, genCodeDto, zos, true);
            }
            for (ResourcesFile resourcesFile : frontEndTemplateFile) {
                processTemplateFile(resourcesFile, genCodeDto, zos, false);
            }
            zos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("生成代码压缩包失败: {}", e.getMessage());
            throw new ServiceException(ResultCode.GEN_CODE_ZIP_ERROR);
        }
    }

    /**
     * 删除生成表
     *
     * @param tableIds 生成表ID集合
     * @return 是否删除成功
     */
    @Transactional
    @Override
    public boolean deleteGenTable(List<Integer> tableIds) {
        // 删除表和字段信息
        tableService.removeBatchByIds(tableIds);
        tableFieldsService.remove(new LambdaQueryWrapper<GenTableFields>()
                .in(GenTableFields::getTableId, tableIds));
        return true;
    }

    /**
     * 处理模版文件
     *
     * @param resourcesFile 模版文件
     * @param genCodeDto    模版信息
     * @param zos           ZipOutputStream
     * @param isBackEnd     是否为后端模版
     * @throws IOException ZipEntry 异常
     */
    private void processTemplateFile(ResourcesFile resourcesFile, GenCodeDto genCodeDto, ZipOutputStream zos,
                                     Boolean isBackEnd) throws IOException {
        String filePath = resourcesFile.getFilePath();
        String genPath = filePath.replace(TEMPLATE_MODULE_NAME, genCodeDto.getTable().getModuleName())
                .replace(TEMPLATE_BUSINESS_NAME, genCodeDto.getTable().getBusinessName());
        String fileName = resourcesFile.getFileName();
        if (resourcesFile.getIsFile()) {
            String fileRealName = getFileRealName(fileName, genCodeDto, isBackEnd);
            // 重构文件名再写入
            ZipEntry entry = new ZipEntry(genPath.substring(0, genPath.lastIndexOf("/") + 1) + fileRealName);
            zos.putNextEntry(entry);
            String content = getTemplateContent(filePath, genCodeDto);
            if (content != null && !content.isEmpty()) {
                zos.write(content.getBytes());
            } else {
                log.warn("模板内容为空: {}", filePath);  // 如果模板内容为空，记录一个警告日志
            }
        } else {
            // Zip 要求目录必须为 "/" 结尾
            if (!genPath.endsWith("/")) {
                genPath = genPath + "/";
            }
            ZipEntry entry = new ZipEntry(genPath);
            zos.putNextEntry(entry);
        }
        zos.closeEntry();
    }

    /**
     * 构建代码生成DTO
     *
     * @param tableId 表ID
     * @return GenCodeDto
     */
    private GenCodeDto buildGenCodeDto(Integer tableId) {
        // 1. 查询表和字段信息
        GenTable genTable = tableService.getById(tableId);
        if (genTable == null) {
            throw new ServiceException(ResultCode.GEN_TABLE_NOT_EXIST);
        }
        List<GenTableFields> tableFieldsList = tableFieldsService.lambdaQuery()
                .eq(GenTableFields::getTableId, tableId)
                .list();
        if (tableFieldsList.isEmpty()) {
            throw new ServiceException(ResultCode.GEN_TABLE_FIELDS_NOT_EXIST);
        }

        // 2. 准备代码生成DTO
        return GenCodeDto.builder().table(genTable).mapFields(tableFields2Map(tableFieldsList)).build();
    }

    /**
     * 字段列表转Map
     *
     * @param fields 字段集合
     * @return Map<String, List < GenTableFields>>
     */
    private Map<String, List<GenTableFields>> tableFields2Map(List<GenTableFields> fields) {
        Map<String, List<GenTableFields>> map = new HashMap<>(5);

        map.put(PK_KEY, fields.stream().filter(GenTableFields::getIsPk).toList());
        map.put(QUERY_KEY, fields.stream().filter(GenTableFields::getIsQuery).toList());
        map.put(SHOW_KEY, fields.stream().filter(GenTableFields::getIsShow).toList());
        map.put(FORM_KEY, fields.stream().filter(GenTableFields::getIsForm).toList());
        map.put(ENTITY_KEY, fields);  // 直接使用原列表

        return map;
    }

    /**
     * 处理树节点
     *
     * @param item       树节点
     * @param genCodeDto 模版Dto
     * @param isBackEnd  是否后端
     */
    private void processTreeItem(PreviewGenCodeTreeVO item, GenCodeDto genCodeDto, boolean isBackEnd) {
        String filePath = item.getId();
        item.setId(filePath.replace(TEMPLATE_MODULE_NAME, genCodeDto.getTable().getModuleName())
                .replace(TEMPLATE_BUSINESS_NAME, genCodeDto.getTable().getBusinessName()));

        if (!StringUtils.hasText(item.getParentId())) {
            item.setParentId(applicationName);
        } else {
            String parentFilePath = item.getParentId();
            item.setParentId(parentFilePath.replace(TEMPLATE_MODULE_NAME, genCodeDto.getTable().getModuleName())
                    .replace(TEMPLATE_BUSINESS_NAME, genCodeDto.getTable().getBusinessName()));
        }
        // 处理文件内容
        if (item.getIsFile()) {
            // 配置 content
            item.setContent(getTemplateContent(filePath, genCodeDto));
            // 配置真实文件名
            String fileRealName = getFileRealName(item.getName(), genCodeDto, isBackEnd);
            item.setName(fileRealName);
            item.setCodeLanguage(fileRealName.substring(fileRealName.lastIndexOf(".") + 1));
        } else {
            item.setName(item.getName().replace(TEMPLATE_MODULE_NAME, genCodeDto.getTable().getModuleName())
                    .replace(TEMPLATE_BUSINESS_NAME, genCodeDto.getTable().getBusinessName()));
        }
    }

    /**
     * 生成模板内容
     *
     * @param genCodeDto 模版Dto
     * @return 模板解析结果
     */

    private String getTemplateContent(String templatePath, GenCodeDto genCodeDto) {
        try {
            Template template = configuration.getTemplate(templatePath);
            StringWriter writer = new StringWriter();
            template.process(genCodeDto, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            log.error("生成代码压缩包失败: {}", e.getMessage());
            throw new ServiceException(ResultCode.GEN_CODE_ZIP_ERROR);
        }
    }

    /**
     * 获取文件名
     *
     * @param filename   模版文件名
     * @param genCodeDto 模版信息
     * @param isBackEnd  是否后端
     * @return 真实文件名
     */
    private String getFileRealName(String filename, GenCodeDto genCodeDto, boolean isBackEnd) {
        if (isBackEnd) {
            // 使用 Optional 来包装可能为 null 的模板
            String template = Optional.ofNullable(BACK_TEMPLATE_ROUTING_MAP.get(filename))
                    .orElseThrow(() -> new ServiceException(ResultCode.TEMPLATE_NOT_EXIST));
            return template.replace("{className}", genCodeDto.getTable().getClassName());
        } else {
            String template = Optional.ofNullable(FORNT_TEMPLATE_ROUTING_MAP.get(filename))
                    .orElseThrow(() -> new ServiceException(ResultCode.TEMPLATE_NOT_EXIST));
            return template.replace("{componentName}", genCodeDto.getTable().getComponentName());
        }
    }
}


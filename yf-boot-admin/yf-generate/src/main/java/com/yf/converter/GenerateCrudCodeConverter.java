package com.yf.converter;

import com.yf.file.model.dto.ResourcesFile;
import com.yf.model.generate.entity.GenTable;
import com.yf.model.vo.GenCrudTableVO;
import com.yf.model.vo.PreviewGenCodeTreeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * GenerateCrudCode 转换器
 *
 * @author : YiFei
 * @since : 2025/3/30 16:27
 */
@Mapper(componentModel = "spring")
public interface GenerateCrudCodeConverter {

    GenCrudTableVO genTable2GenCrudTableVo(GenTable genTable);

    List<PreviewGenCodeTreeVO> resourcesFile2previewVO(List<ResourcesFile> backEndFileList);

    @Mappings({
            @Mapping(target = "id", source = "filePath"),
            @Mapping(target = "parentId", source = "parentFilePath"),
            @Mapping(target = "name", source = "fileName")
    })
    PreviewGenCodeTreeVO resourcesFile2previewVO(ResourcesFile resourcesFile);
}

package com.yf.dfms.node.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.dfms.node.converter.DfmsNodeConverter;
import com.yf.dfms.node.mapper.DfmsNodeMapper;
import com.yf.dfms.node.model.entity.DfmsNode;
import com.yf.dfms.node.model.form.DfmsNodeForm;
import com.yf.dfms.node.model.query.DfmsNodePageQuery;
import com.yf.dfms.node.model.vo.DfmsNodePageVO;
import com.yf.dfms.node.service.IDfmsNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 节点信息-DfmsNodeServiceImpl
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:11
 */
@Service("dfmsNodeService" )
@RequiredArgsConstructor
public class DfmsNodeServiceImpl extends ServiceImpl<DfmsNodeMapper, DfmsNode> implements IDfmsNodeService {

    private final DfmsNodeConverter dfmsNodeConverter;

    /**
     * 查询节点信息
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<DfmsNodePageVO> getDfmsNodePage(DfmsNodePageQuery queryParams) {
        // 1. 分页查询数据
        Page<DfmsNode> page = this.getPageData(queryParams);
        // 2. 转换为 vo 后返回
        return dfmsNodeConverter.page2pageVO(page);
    }

    /**
     * 获取分页数据
     *
     * @param query 查询参数
     * @return Page
     */
    private Page<DfmsNode> getPageData(DfmsNodePageQuery query) {
        // 1. 查询数据
        return this.lambdaQuery()
                .eq(StringUtils.hasText(query.getName()), DfmsNode::getName, query.getName())
                .eq(StringUtils.hasText(query.getIp()), DfmsNode::getIp, query.getIp())
                .eq(StringUtils.hasText(query.getPort()), DfmsNode::getPort, query.getPort())
                .eq(StringUtils.hasText(query.getUsername()), DfmsNode::getUsername, query.getUsername())
                .eq(query.getRole() != null, DfmsNode::getRole, query.getRole())
                .in(!CollectionUtils.isEmpty(query.getStatus()), DfmsNode::getStatus,query.getStatus())
                .eq(query.getCpu() != null, DfmsNode::getCpu, query.getCpu())
                .eq(query.getMemory() != null, DfmsNode::getMemory, query.getMemory())
                .eq(query.getCreateBy() != null, DfmsNode::getCreateBy, query.getCreateBy())
                .between(query.getCreateTimeBegin() != null && query.getCreateTimeEnd() != null, DfmsNode::getCreateTime,
                        query.getCreateTimeBegin(),
                        query.getCreateTimeEnd())
                .between(query.getUpdateByBegin() != null && query.getUpdateByEnd() != null, DfmsNode::getUpdateBy,
                        query.getUpdateByBegin(),
                        query.getUpdateByEnd())
                .between(query.getUpdateTimeBegin() != null && query.getUpdateTimeEnd() != null, DfmsNode::getUpdateTime,
                        query.getUpdateTimeBegin(),
                        query.getUpdateTimeEnd())
                .page(query.toPage());
    }

    /**
     * 获取节点信息表单数据
     *
     * @param id 节点信息表主键
     * @return 节点信息表单数据
     */
    @Override
    public DfmsNodeForm getDfmsNodeForm(Integer id) {
        // 1. 查询对应数据
        DfmsNode dfmsNode = this.lambdaQuery()
                .eq(DfmsNode::getId, id)
                .one();
        // 2. entity 2 form
        return dfmsNodeConverter.entity2form(dfmsNode);
    }

    /**
     * 新增节点信息
     *
     * @param dfmsNodeForm 节点信息表单
     * @return 主键
     */
    @Override
    public Integer saveDfmsNode(DfmsNodeForm dfmsNodeForm) {
        // 1. form 转 entity
        DfmsNode dfmsNode = dfmsNodeConverter.form2entity(dfmsNodeForm);
        // 2. 存储数据
        this.save(dfmsNode);
        // 3. 返回主键
        return dfmsNode.getId();
    }

    /**
     * 删除节点信息
     *
     * @param ids 主键集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDfmsNode(List<Integer> ids) {
        this.lambdaUpdate()
                .in(DfmsNode::getId, ids)
                .remove();
        return true;
    }

    /**
     * 修改节点信息信息
     *
     * @param id   节点信息Id
     * @param dfmsNodeForm 节点信息表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateDfmsNode(Integer id, DfmsNodeForm dfmsNodeForm) {
        // 1. form 转 entity
        DfmsNode dfmsNode = dfmsNodeConverter.form2entity(dfmsNodeForm);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(DfmsNode::getId, id)
                .update(dfmsNode);
        return true;
    }
}

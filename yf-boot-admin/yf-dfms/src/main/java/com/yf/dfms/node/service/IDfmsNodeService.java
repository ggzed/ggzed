package com.yf.dfms.node.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.dfms.node.model.entity.DfmsNode;
import com.yf.dfms.node.model.form.DfmsNodeForm;
import com.yf.dfms.node.model.query.DfmsNodePageQuery;
import com.yf.dfms.node.model.vo.DfmsNodePageVO;

import java.util.List;

/**
 * 节点信息-DfmsNodeService
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:11
 */
public interface IDfmsNodeService extends IService<DfmsNode> {

    /**
     * 分页查询节点信息
     *
     * @param queryParams 查询参数
     * @return 节点信息分页数据
     */
    IPage<DfmsNodePageVO> getDfmsNodePage(DfmsNodePageQuery queryParams);

    /**
     * 删除节点信息
     *
     * @param ids 节点信息id集合
     * @return 是否删除成功
     */
    boolean deleteDfmsNode(List<Integer> ids);

    /**
     * 节点信息表单数据
     *
     * @param id 节点信息主键
     * @return 节点信息表单数据
     */
    DfmsNodeForm getDfmsNodeForm(Integer id);

    /**
     * 保存节点信息
     *
     * @param dfmsNodeForm 节点信息表单
     * @return 节点信息主键
     */
    Integer saveDfmsNode(DfmsNodeForm dfmsNodeForm);

    /**
     * 修改节点信息
     *
     * @param id   节点信息主键
     * @param dfmsNodeForm 节点信息表单
     * @return 是否修改成功
     */
    boolean updateDfmsNode(Integer id, DfmsNodeForm dfmsNodeForm);
}

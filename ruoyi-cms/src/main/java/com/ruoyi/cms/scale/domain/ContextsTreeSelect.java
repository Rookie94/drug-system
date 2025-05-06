package com.ruoyi.cms.scale.domain;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;

/**
 * ContextsTreeSelect树结构实体类
 * 
 * @author ruoyi
 */
public class ContextsTreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 节点禁用 */
    private boolean disabled = false;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ContextsTreeSelect> children;

    public ContextsTreeSelect()
    {

    }

    public ContextsTreeSelect(LbsContexts lbsContexts) {
        this.id = lbsContexts.getContextId();
        this.label = lbsContexts.getContextName();
        this.disabled = StringUtils.equals(UserConstants.DEPT_DISABLE, lbsContexts.getStatus());
        this.children = lbsContexts.getChildren()
                .stream()
                .map(child -> new ContextsTreeSelect((LbsContexts) child))
                .collect(Collectors.toList());
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public List<ContextsTreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<ContextsTreeSelect> children)
    {
        this.children = children;
    }
}

package com.leyou.item.pojo;

import javax.persistence.*;
import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/24 15:20
 */
@Table(name = "tb_spec_group")
public class SpecGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private String name;
    @Transient
    private List<SpecParam> parms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecParam> getParms() {
        return parms;
    }

    public void setParms(List<SpecParam> parms) {
        this.parms = parms;
    }
}

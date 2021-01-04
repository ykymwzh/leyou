package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/14 11:28
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    /**
    *根据查询条件分页查询，不输入默认全部查询
    *@Author：ykym
    * @param: [key, page, sortBy, desc]
    * @return: com.leyou.common.pojo.PageResult<com.leyou.item.pojo.Brand>
    * @Date:  12:11 2020/8/14
    */
    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page,Integer rows,String sortBy, Boolean desc) {

        //初始划example
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        //模糊查询
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }

        //添加分页条件
         PageHelper.startPage(page,rows);
        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy+" "+(desc?"desc":"asc"));
        }
        //
        List<Brand> brands = this.brandMapper.selectByExample(example);
        //包装成pageinfo
        PageInfo<Brand> info = new PageInfo<>(brands);
        //getlist返回的是结果集
        return new PageResult<>(info.getTotal(),info.getList());
    }

    /**
    *新增品牌，并完成tb_category-brand表
    *@Author：ykym
    * @param: [brand, cids]
    * @return: void
    * @Date:  20:51 2020/8/16
    */
    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增brand
        this.brandMapper.insertSelective(brand);


        //建立中间表关联，新增中间表的数据
        cids.forEach(cid->{
            this.brandMapper.insertCategoryAndBrand(cid,brand.getId());
        });

    }

    @Override
    @Transactional
    public void deleteByBrandId(Long did) {
        this.brandMapper.deleteByPrimaryKey(did);
        this.brandMapper.deleteCategoryAndBrand(did);
    }

    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return brandMapper.selectBrandsById(cid);
    }

    /**
    *根据Id查询品牌
    *@Author：ykym
    * @param: [id]
    * @return: com.leyou.item.pojo.Brand
    * @Date:  16:02 2020/9/2
    */
    @Override
    public Brand queryBrandById(Long id) {

        return this.brandMapper.selectByPrimaryKey(id);
    }
}

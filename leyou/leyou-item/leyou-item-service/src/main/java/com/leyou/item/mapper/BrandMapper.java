package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/14 11:24
 */
public interface BrandMapper extends Mapper<Brand> {
    /**
    *新增商品分类和品牌的中间表数据
    *@Author：ykym
    * @param: [cid, id]
    * @return: void
    * @Date:  20:40 2020/8/16
    */
    @Insert("Insert into tb_category_brand(category_id,brand_id) values (#{cid},#{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid,@Param("bid") Long id);
    @Delete("delete from tb_category_brand where brand_id = #{did}")
    void deleteCategoryAndBrand(@Param("did") Long did);

    /**
    *通过cid查询brand表的对应品牌
    *@Author：ykym
    * @param: [cid]
    * @return: java.util.List<com.leyou.item.pojo.Brand>
    * @Date:  0:12 2020/8/26
    */

    @Select("SELECT b.* from tb_brand b INNER JOIN tb_category_brand cb on b.id=cb.brand_id where cb.category_id=#{cid}")
    List<Brand> selectBrandsById(Long cid);
}

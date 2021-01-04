package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author:ykym
 * @Date:2020/8/25 15:49
 */
@Service
public class GoodsServiceImpl implements GoodService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Override
    public PageResult<SpuBo> querySpuBoBypage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //添加查询条件
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        //开启分页
        PageHelper.startPage(page,rows);
        //执行查询
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> info  = new PageInfo<>(spus);

        List<SpuBo> spuBos = new ArrayList<>();
        for (Spu spu : spus) {
            SpuBo spuBo = new SpuBo();
            //copy共同属性到新对象
            BeanUtils.copyProperties(spu,spuBo);
            //查询分类名称
            List<String> names = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            spuBo.setCname(StringUtils.join(names,"/"));
            //查询品牌名称
            spuBo.setBname(this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());

            spuBos.add(spuBo);
        }

        //getlist返回的是结果集
        return new PageResult<>(info.getTotal(),spuBos);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savaGoods(SpuBo spuBo) {
     try {
         //新增spu

         spuBo.setId(null);
         spuBo.setSaleable(true);
         spuBo.setValid(true);
         spuBo.setCreateTime(new Date());
         spuBo.setLastUpdateTime(spuBo.getCreateTime());
         spuMapper.insertSelective(spuBo);

         //新增spudetail
         SpuDetail spuDetail = spuBo.getSpuDetail();
         spuDetail.setSpuId(spuBo.getId());
         spuDetailMapper.insertSelective(spuDetail);

         //新增sku
         spuBo.getSkus().forEach(sku -> {
             sku.setId(null);
             sku.setSpuId(spuBo.getId());
             sku.setCreateTime(new Date());
             sku.setLastUpdateTime(sku.getCreateTime());
             skuMapper.insertSelective(sku);
             //新增stock库存
             Stock stock = new Stock();
             stock.setSkuId(sku.getId());
             stock.setStock(sku.getStock());
             stockMapper.insertSelective(stock);
         });
     }catch (Exception e){
         e.printStackTrace();
         TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
     }

        sendMessage(spuBo.getId(),"insert");
    }

    /**
    *发送消息
    *@Author：ykym
    * @param: [id, type]
    * @return: void
    * @Date:  14:45 2020/9/22
    */
    public void sendMessage(Long id,String type) {
        try {
            amqpTemplate.convertAndSend("item."+type,id);
        } catch (AmqpException e) {
            e.printStackTrace();
            LOGGER.error("{}商品消息发送异常，id:{}",type,id,e);
        }
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {

        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(record);
        skus.forEach(sku -> {
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    @Override
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //查询以前的sku
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = skuMapper.select(record);
        //删除库存
        skus.forEach(sku -> {
            stockMapper.deleteByPrimaryKey(sku.getId());
        });
        //删除sku
        skuMapper.delete(record);
        //新增sku和stock
        spuBo.getSkus().forEach(sku -> {
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insertSelective(sku);
            //新增stock库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);
        });

        //更新spu
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        spuMapper.updateByPrimaryKeySelective(spuBo);
        //更新spudetail
        spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        this.sendMessage(spuBo.getId(),"update");
    }

    @Override
    public Spu querySpuById(Long id) {
        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        return spu;
    }

    @Override
    public Sku querySkuById(Long skuId) {
        Sku sku = this.skuMapper.selectByPrimaryKey(skuId);
        return sku;
    }
}

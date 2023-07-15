package com.example.academickg.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.academickg.common.CacheKey;
import com.example.academickg.common.Result;
import com.example.academickg.entity.dao.Paper;
import com.example.academickg.mapper.PaperMapper;
import com.example.academickg.service.IPaperService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.example.academickg.entity.dto.PaperDto;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements IPaperService {

    @Resource
    private PaperMapper paperMapper;
    @Resource
    private RedisServiceImpl redisService;

    CacheKey cacheKey = null;

    public List<PaperDto> queryPaperByIdList(List<Integer> idList){

        List<PaperDto> paperDtos =  paperMapper.selectPaperByIdList(idList);
        // 加入redis
        redisService.createRedisKey(Collections.singletonList(paperDtos), cacheKey.PAPER_KEY_FIND_ID_LIST);
        return paperDtos;
    }
    public PaperDto queryPaperById(Integer id){
        PaperDto paperDto = paperMapper.selectPaperById(id);
        redisService.createRedisKey(Collections.singletonList(paperDto), cacheKey.PAPER_KEY_FIND_ID);
        return paperDto;
    }

    public Result find(){
        return null;
    }


//    public List<Paper> queryPaper(){
//
//    }




    //-------------------------------------------------------------------------------------------------//
    @Override
    public boolean save(Paper entity) {
        return IPaperService.super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<Paper> entityList) {
        return IPaperService.super.saveBatch(entityList);
    }

    @Override
    public boolean saveBatch(Collection<Paper> entityList, int batchSize) {
        return false;
    }

    public boolean saveOrUpdateBatch(List<Paper> entityList) {
        return paperMapper.saveBatch(entityList);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Paper> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean removeById(Serializable id) {
        return IPaperService.super.removeById(id);
    }

    @Override
    public boolean removeById(Serializable id, boolean useFill) {
        return IPaperService.super.removeById(id, useFill);
    }

    @Override
    public boolean removeById(Paper entity) {
        return IPaperService.super.removeById(entity);
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        return IPaperService.super.removeByMap(columnMap);
    }

    @Override
    public boolean remove(Wrapper<Paper> queryWrapper) {
        return IPaperService.super.remove(queryWrapper);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        return IPaperService.super.removeByIds(list);
    }

    @Override
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        return IPaperService.super.removeByIds(list, useFill);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return IPaperService.super.removeBatchByIds(list);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return IPaperService.super.removeBatchByIds(list, useFill);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, int batchSize) {
        return IPaperService.super.removeBatchByIds(list, batchSize);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        return IPaperService.super.removeBatchByIds(list, batchSize, useFill);
    }

    @Override
    public boolean updateById(Paper entity) {
        return IPaperService.super.updateById(entity);
    }

    @Override
    public boolean update(Wrapper<Paper> updateWrapper) {
        return IPaperService.super.update(updateWrapper);
    }

    @Override
    public boolean update(Paper entity, Wrapper<Paper> updateWrapper) {
        return IPaperService.super.update(entity, updateWrapper);
    }

    @Override
    public boolean updateBatchById(Collection<Paper> entityList) {
        return IPaperService.super.updateBatchById(entityList);
    }

    @Override
    public boolean updateBatchById(Collection<Paper> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Paper entity) {
        return false;
    }

    @Override
    public Paper getById(Serializable id) {
        return IPaperService.super.getById(id);
    }

    @Override
    public List<Paper> listByIds(Collection<? extends Serializable> idList) {
        return IPaperService.super.listByIds(idList);
    }

    @Override
    public List<Paper> listByMap(Map<String, Object> columnMap) {
        return IPaperService.super.listByMap(columnMap);
    }

    @Override
    public Paper getOne(Wrapper<Paper> queryWrapper) {
        return IPaperService.super.getOne(queryWrapper);
    }

    @Override
    public Paper getOne(Wrapper<Paper> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Paper> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Paper> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public long count() {
        return IPaperService.super.count();
    }

    @Override
    public long count(Wrapper<Paper> queryWrapper) {
        return IPaperService.super.count(queryWrapper);
    }

    @Override
    public List<Paper> list(Wrapper<Paper> queryWrapper) {
        return IPaperService.super.list(queryWrapper);
    }

    @Override
    public List<Paper> list() {
        return IPaperService.super.list();
    }

    @Override
    public <E extends IPage<Paper>> E page(E page, Wrapper<Paper> queryWrapper) {
        return IPaperService.super.page(page, queryWrapper);
    }

    @Override
    public <E extends IPage<Paper>> E page(E page) {
        return IPaperService.super.page(page);
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<Paper> queryWrapper) {
        return IPaperService.super.listMaps(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps() {
        return IPaperService.super.listMaps();
    }

    @Override
    public List<Object> listObjs() {
        return IPaperService.super.listObjs();
    }

    @Override
    public <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return IPaperService.super.listObjs(mapper);
    }

    @Override
    public List<Object> listObjs(Wrapper<Paper> queryWrapper) {
        return IPaperService.super.listObjs(queryWrapper);
    }

    @Override
    public <V> List<V> listObjs(Wrapper<Paper> queryWrapper, Function<? super Object, V> mapper) {
        return IPaperService.super.listObjs(queryWrapper, mapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<Paper> queryWrapper) {
        return IPaperService.super.pageMaps(page, queryWrapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page) {
        return IPaperService.super.pageMaps(page);
    }



    @Override
    public Class<Paper> getEntityClass() {
        return null;
    }

    @Override
    public QueryChainWrapper<Paper> query() {
        return IPaperService.super.query();
    }

    @Override
    public LambdaQueryChainWrapper<Paper> lambdaQuery() {
        return IPaperService.super.lambdaQuery();
    }

    @Override
    public LambdaQueryChainWrapper<Paper> lambdaQuery(Paper entity) {
        return IPaperService.super.lambdaQuery(entity);
    }


    @Override
    public UpdateChainWrapper<Paper> update() {
        return IPaperService.super.update();
    }

    @Override
    public LambdaUpdateChainWrapper<Paper> lambdaUpdate() {
        return IPaperService.super.lambdaUpdate();
    }

    @Override
    public boolean saveOrUpdate(Paper entity, Wrapper<Paper> updateWrapper) {
        return IPaperService.super.saveOrUpdate(entity, updateWrapper);
    }
}

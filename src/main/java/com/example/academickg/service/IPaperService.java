package com.example.academickg.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.academickg.entity.dao.Paper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public interface IPaperService extends IService<Paper> {

    @Override
    default boolean save(Paper entity) {
        return IService.super.save(entity);
    }

    @Override
    default boolean saveBatch(Collection<Paper> entityList) {
        return IService.super.saveBatch(entityList);
    }

    @Override
    boolean saveBatch(Collection<Paper> entityList, int batchSize);

    @Override
    default boolean saveOrUpdateBatch(Collection<Paper> entityList) {
        return IService.super.saveOrUpdateBatch(entityList);
    }

    @Override
    boolean saveOrUpdateBatch(Collection<Paper> entityList, int batchSize);

    @Override
    default boolean removeById(Serializable id) {
        return IService.super.removeById(id);
    }

    @Override
    default boolean removeById(Serializable id, boolean useFill) {
        return IService.super.removeById(id, useFill);
    }

    @Override
    default boolean removeById(Paper entity) {
        return IService.super.removeById(entity);
    }

    @Override
    default boolean removeByMap(Map<String, Object> columnMap) {
        return IService.super.removeByMap(columnMap);
    }

    @Override
    default boolean remove(Wrapper<Paper> queryWrapper) {
        return IService.super.remove(queryWrapper);
    }

    @Override
    default boolean removeByIds(Collection<?> list) {
        return IService.super.removeByIds(list);
    }

    @Override
    default boolean removeByIds(Collection<?> list, boolean useFill) {
        return IService.super.removeByIds(list, useFill);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list) {
        return IService.super.removeBatchByIds(list);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return IService.super.removeBatchByIds(list, useFill);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, int batchSize) {
        return IService.super.removeBatchByIds(list, batchSize);
    }

    @Override
    default boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        return IService.super.removeBatchByIds(list, batchSize, useFill);
    }

    @Override
    default boolean updateById(Paper entity) {
        return IService.super.updateById(entity);
    }

    @Override
    default boolean update(Wrapper<Paper> updateWrapper) {
        return IService.super.update(updateWrapper);
    }

    @Override
    default boolean update(Paper entity, Wrapper<Paper> updateWrapper) {
        return IService.super.update(entity, updateWrapper);
    }

    @Override
    default boolean updateBatchById(Collection<Paper> entityList) {
        return IService.super.updateBatchById(entityList);
    }

    @Override
    boolean updateBatchById(Collection<Paper> entityList, int batchSize);

    @Override
    boolean saveOrUpdate(Paper entity);

    @Override
    default Paper getById(Serializable id) {
        //System.out.println(IService.super.getById(id));
        return IService.super.getById(id);
    }

    @Override
    default List<Paper> listByIds(Collection<? extends Serializable> idList) {
        return IService.super.listByIds(idList);
    }

    @Override
    default List<Paper> listByMap(Map<String, Object> columnMap) {
        return IService.super.listByMap(columnMap);
    }

    @Override
    default Paper getOne(Wrapper<Paper> queryWrapper) {
        return IService.super.getOne(queryWrapper);
    }

    @Override
    Paper getOne(Wrapper<Paper> queryWrapper, boolean throwEx);

    @Override
    Map<String, Object> getMap(Wrapper<Paper> queryWrapper);

    @Override
    <V> V getObj(Wrapper<Paper> queryWrapper, Function<? super Object, V> mapper);

    @Override
    default long count() {
        return IService.super.count();
    }

    @Override
    default long count(Wrapper<Paper> queryWrapper) {
        return IService.super.count(queryWrapper);
    }

    @Override
    default List<Paper> list(Wrapper<Paper> queryWrapper) {
        return IService.super.list(queryWrapper);
    }

    @Override
    default List<Paper> list() {
        return IService.super.list();
    }

    @Override
    default <E extends IPage<Paper>> E page(E page, Wrapper<Paper> queryWrapper) {
        return IService.super.page(page, queryWrapper);
    }

    @Override
    default <E extends IPage<Paper>> E page(E page) {
        return IService.super.page(page);
    }

    @Override
    default List<Map<String, Object>> listMaps(Wrapper<Paper> queryWrapper) {
        return IService.super.listMaps(queryWrapper);
    }

    @Override
    default List<Map<String, Object>> listMaps() {
        return IService.super.listMaps();
    }

    @Override
    default List<Object> listObjs() {
        return IService.super.listObjs();
    }

    @Override
    default <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return IService.super.listObjs(mapper);
    }

    @Override
    default List<Object> listObjs(Wrapper<Paper> queryWrapper) {
        return IService.super.listObjs(queryWrapper);
    }

    @Override
    default <V> List<V> listObjs(Wrapper<Paper> queryWrapper, Function<? super Object, V> mapper) {
        return IService.super.listObjs(queryWrapper, mapper);
    }

    @Override
    default <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<Paper> queryWrapper) {
        return IService.super.pageMaps(page, queryWrapper);
    }

    @Override
    default <E extends IPage<Map<String, Object>>> E pageMaps(E page) {
        return IService.super.pageMaps(page);
    }

    @Override
    BaseMapper<Paper> getBaseMapper();

    @Override
    Class<Paper> getEntityClass();

    @Override
    default QueryChainWrapper<Paper> query() {
        return IService.super.query();
    }

    @Override
    default LambdaQueryChainWrapper<Paper> lambdaQuery() {
        return IService.super.lambdaQuery();
    }

    @Override
    default LambdaQueryChainWrapper<Paper> lambdaQuery(Paper entity) {
        return IService.super.lambdaQuery(entity);
    }


    @Override
    default UpdateChainWrapper<Paper> update() {
        return IService.super.update();
    }

    @Override
    default LambdaUpdateChainWrapper<Paper> lambdaUpdate() {
        return IService.super.lambdaUpdate();
    }

    @Override
    default boolean saveOrUpdate(Paper entity, Wrapper<Paper> updateWrapper) {
        return IService.super.saveOrUpdate(entity, updateWrapper);
    }
}

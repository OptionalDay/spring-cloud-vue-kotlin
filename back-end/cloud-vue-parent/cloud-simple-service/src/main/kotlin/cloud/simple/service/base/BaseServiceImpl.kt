package cloud.simple.service.base;

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.PropertyUtils
import org.apache.commons.collections.CollectionUtils
import tk.mybatis.mapper.common.Mapper
import tk.mybatis.mapper.entity.Example
import tk.mybatis.mapper.entity.Example.Criteria
import java.util.Map
import java.util.Map.Entry

abstract class BaseServiceImpl< T: BaseEntity> : BaseService<T>{
	
	//protected Mapper<T> mapper;

	open abstract fun getMapper(): Mapper<T>;


	override open fun selectOne(record: T): T ? {
		var results = getMapper().select(record);
		if(CollectionUtils.isNotEmpty(results)) return results[0]
		else return null
	}
	/**
	 * 根据实体类不为null的字段进行查询,条件全部使用=号and条件
	 * @param record 影响行数
	 * @return list 列表
	 */
	override fun select(record: T) : List<T> {
		return getMapper().select(record) as List<T>
    }

	/**
	 * 根据实体类不为null的字段进行查询，条件全部使用=号and条件，并指定排序
	 * @param record 查询条件
	 * @param orderSqlStr 排序条件 （name ase,id desc）
	 * @return List 列表
	 */
	override open fun  select(record: T, orderSqlStr: String): List<T>{
		val example: Example = Example(record.javaClass,false);
		val criteria: Criteria = example.createCriteria();
		val map: Map<String, String>
		try {
			map = BeanUtils.describe(record) as Map<String, String>;
			for ( entry in  map.entrySet()){
				if(entry.value == null || "".equals(entry.value)) continue;
				criteria.andEqualTo(entry.key, entry.value);
			}
			example.orderByClause = orderSqlStr;
			return getMapper().selectByExample(example) as List<T>;
		} catch (e: Exception ) {
			throw RuntimeException(e.message,e);
		}
	}

	/**
	 * 根据实体类不为null的字段查询总数,条件全部使用=号and条件
	 * @param record 对象
	 * @return int  影响行数
	 */
	override fun selectCount(record: T): Int {
		return getMapper().selectCount(record);
	}

	/**
	 * 根据主键进行查询,必须保证结果唯一 单个字段做主键时,可以直接写主键的值 联合主键时,key可以是实体类,也可以是Map
	 *
	 * @param key 主键
	 * @return T  影响行数
	 */
	override fun  selectByPrimaryKey(key: Any): T {
		return getMapper().selectByPrimaryKey(key);
	}

	/**
	 * 插入一条数据 支持Oracle序列,UUID,类似Mysql的INDENTITY自动增长(自动回写)
	 * 优先使用传入的参数值,参数值空时,才会使用序列、UUID,自动增长
	 *
	 * @param record 对象
	 * @return int 影响行数
	 */
	override fun  insert(record: T): Int {
		return getMapper().insert(record);
	}

	/**
	 * 插入一条数据,只插入不为null的字段,不会影响有默认值的字段
	 * 支持Oracle序列,UUID,类似Mysql的INDENTITY自动增长(自动回写)
	 * 优先使用传入的参数值,参数值空时,才会使用序列、UUID,自动增长
	 *
	 * @param record 对象
	 * @return int 影响行数
	 */
	override fun insertSelective(record: T): Int {
		return getMapper().insertSelective(record);
	}

	/**
	 * 根据实体类不为null的字段进行查询,条件全部使用=号and条件
	 *
	 * @param key 主键
	 * @return int 影响行数
	 */
	override fun delete(key: T): Int {
		return getMapper().delete(key);
	}

	/**
	 * 通过主键进行删除,这里最多只会删除一条数据 单个字段做主键时,可以直接写主键的值 联合主键时,key可以是实体类,也可以是Map
	 *
	 * @param key 主键
	 * @return int 影响行数
	 */
	override fun deleteByPrimaryKey(key: Any): Int {
		return getMapper().deleteByPrimaryKey(key);
	}

	/**
	 * 根据主键进行更新,这里最多只会更新一条数据 参数为实体类
	 *
	 * @param record 对象
	 * @return int 影响行数
	 */
	override fun updateByPrimaryKey(record: T): Int {
		return getMapper().updateByPrimaryKey(record);
	}

	/**
	 * 根据主键进行更新 只会更新不是null的数据
	 *
	 * @param record 对象
	 * @return int 影响行数
	 */
	override fun updateByPrimaryKeySelective(record: T): Int {
		return getMapper().updateByPrimaryKeySelective(record);
	}

	/**
	 * 保存或者更新，根据传入id主键是不是null来确认
	 *
	 * @param record 对象
	 * @return int 影响行数
	 */
	override fun save(record: T): Int {
		var count = 0;
		if (record.id == null) {
			count = this.insertSelective(record);
		} else {
			count = this.updateByPrimaryKeySelective(record);
		}
		return count;
	}

	/**
	 *(单表分页可排序)
	 * @param pageNum 当前页
	 * @param pageSize 页码
	 * @param record 对象
	 * @return PageInfo 分页对象
	 */
	override fun selectPage(pageNum: Int, pageSize: Int, record: T): PageInfo<T> {
		PageHelper.startPage<T>(pageNum, pageSize)
		return PageInfo<T>(getMapper().select(record));
	}

	/**
	 * (单表分页可排序)
	 * @param pageNum 当前页
	 * @param pageSize 页码
	 * @param record 对象
	 * @param orderSqlStr (如:id desc)
	 * @return PageInfo 分页对象
	 */
	override fun  selectPage(pageNum: Int, pageSize: Int, record: T,orderSqlStr: String):PageInfo<T> {
		val example = Example(record.javaClass,false);
		val criteria = example.createCriteria();
		val map: Map<String, Object>;
		try {
			map = PropertyUtils.describe(record) as Map<String, Object>;
			for(entry in map.entrySet()){
				if(entry.value == null || "".equals(entry.value)) continue;
				criteria.andEqualTo(entry.key, entry.value);
			}
			example.orderByClause= orderSqlStr;
			PageHelper.startPage<T>(pageNum, pageSize);
			val list = getMapper().selectByExample(example);
			return PageInfo<T>(list);
		} catch (e: Exception) {
			throw RuntimeException(e.message, e);
		}
	}

}
